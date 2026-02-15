package com.sih.certificate.service;

import com.sih.certificate.blockchain.BlockchainGateway;
import org.springframework.stereotype.Service;

@Service
public class BlockchainService {

    private final BlockchainGateway gateway;

    public BlockchainService(BlockchainGateway gateway) {
        this.gateway = gateway;
    }

    public String issueCertificateHash(String hashHex) throws Exception {
        return gateway.storeHash(hashHex).txHash();
    }

    public VerifyResult verifyCertificateHash(String hashHex) throws Exception {
        var res = gateway.verifyHash(hashHex);
        return new VerifyResult(res.exists(), res.timestamp());
    }

    public record VerifyResult(boolean exists, long timestamp) {}
}
