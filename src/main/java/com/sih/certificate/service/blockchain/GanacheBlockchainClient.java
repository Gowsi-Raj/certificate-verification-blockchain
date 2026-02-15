package com.sih.certificate.service.blockchain;

import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.FunctionReturnDecoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Bool;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.*;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.RawTransactionManager;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class GanacheBlockchainClient implements BlockchainClient {

    private final Web3j web3j;
    private final Credentials credentials;
    private final RawTransactionManager txManager;
    private final String contractAddress;
    private final BigInteger gasPrice;
    private final BigInteger gasLimit;

    public GanacheBlockchainClient(
            String rpcUrl,
            long chainId,
            String privateKey,
            String contractAddress,
            BigInteger gasPrice,
            BigInteger gasLimit
    ) {
        this.web3j = Web3j.build(new HttpService(rpcUrl));
        this.credentials = Credentials.create(privateKey);
        this.txManager = new RawTransactionManager(web3j, credentials, chainId);
        this.contractAddress = contractAddress;
        this.gasPrice = gasPrice;
        this.gasLimit = gasLimit;
    }

    @Override
    public TxResult storeCertificateHash(String hashHex) throws Exception {
        Function fn = new Function(
                "storeCertificate",
                Collections.singletonList(new Utf8String(hashHex)),
                Collections.emptyList()
        );

        String data = FunctionEncoder.encode(fn);

        EthSendTransaction sent = txManager.sendTransaction(
                gasPrice, gasLimit, contractAddress, data, BigInteger.ZERO
        );

        if (sent.hasError()) {
            throw new RuntimeException("TX Error: " + sent.getError().getMessage());
        }

        String txHash = sent.getTransactionHash();
        // wait for receipt
        TransactionReceipt receipt = waitForReceipt(txHash);

        return new TxResult(receipt.getTransactionHash());
    }

    @Override
    public VerifyResult verifyCertificateHash(String hashHex) throws Exception {
        Function fn = new Function(
                "verifyCertificate",
                Collections.singletonList(new Utf8String(hashHex)),
                Arrays.asList(
                        new TypeReference<Bool>() {},
                        new TypeReference<Uint256>() {}
                )
        );

        String data = FunctionEncoder.encode(fn);

        EthCall call = web3j.ethCall(
                Transaction.createEthCallTransaction(credentials.getAddress(), contractAddress, data),
                DefaultBlockParameterName.LATEST
        ).send();

        if (call.isReverted()) {
            throw new RuntimeException("eth_call reverted: " + call.getRevertReason());
        }

        List<Type> decoded = FunctionReturnDecoder.decode(call.getValue(), fn.getOutputParameters());

        // ✅ Avoid IndexOutOfBounds (your current crash)
        if (decoded == null || decoded.size() < 2) {
            return new VerifyResult(false, 0);
        }

        boolean exists = (Boolean) decoded.get(0).getValue();
        BigInteger ts = (BigInteger) decoded.get(1).getValue();

        return new VerifyResult(exists, ts.longValue());
    }

    private TransactionReceipt waitForReceipt(String txHash) throws Exception {
        for (int i = 0; i < 40; i++) {
            Optional<TransactionReceipt> receipt =
                    web3j.ethGetTransactionReceipt(txHash).send().getTransactionReceipt();
            if (receipt.isPresent()) return receipt.get();
            Thread.sleep(500);
        }
        throw new RuntimeException("No receipt after waiting: " + txHash);
    }
}
