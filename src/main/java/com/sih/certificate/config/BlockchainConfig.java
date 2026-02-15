package com.sih.certificate.config;

import com.sih.certificate.service.blockchain.BlockchainClient;
import com.sih.certificate.service.blockchain.GanacheBlockchainClient;
import com.sih.certificate.service.blockchain.SimBlockchainClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BlockchainConfig {

    @Value("${blockchain.mode:SIM}")
    private String mode;

    @Bean
    public BlockchainClient blockchainClient(
            @Value("${blockchain.rpc-url:http://127.0.0.1:7545}") String rpcUrl,
            @Value("${blockchain.chain-id:1337}") long chainId,
            @Value("${blockchain.private-key:}") String privateKey,
            @Value("${blockchain.contract-address:}") String contractAddress,
            @Value("${blockchain.gas-price:20000000000}") java.math.BigInteger gasPrice,
            @Value("${blockchain.gas-limit:3000000}") java.math.BigInteger gasLimit
    ) {
        if ("GANACHE".equalsIgnoreCase(mode)) {
            return new GanacheBlockchainClient(rpcUrl, chainId, privateKey, contractAddress, gasPrice, gasLimit);
        }
        return new SimBlockchainClient(); // default
    }
}
