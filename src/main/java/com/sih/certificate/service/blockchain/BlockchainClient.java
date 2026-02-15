package com.sih.certificate.service.blockchain;

public interface BlockchainClient {
    TxResult storeCertificateHash(String hashHex) throws Exception;
    VerifyResult verifyCertificateHash(String hashHex) throws Exception;

    record TxResult(String txHash) {}
    record VerifyResult(boolean exists, long timestamp) {}
}
