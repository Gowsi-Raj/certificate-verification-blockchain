package com.sih.certificate.blockchain;

public interface BlockchainGateway {
    BlockchainResult storeHash(String certificateHash) throws Exception;
    BlockchainResult verifyHash(String certificateHash) throws Exception;
}
