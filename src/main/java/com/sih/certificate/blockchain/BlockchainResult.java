package com.sih.certificate.blockchain;

public record BlockchainResult(boolean exists, long timestamp, String txHash) {

    public static BlockchainResult ok(String txHash, long timestamp) {
        return new BlockchainResult(true, timestamp, txHash);
    }

    public static BlockchainResult notFound() {
        return new BlockchainResult(false, 0L, "");
    }
}
