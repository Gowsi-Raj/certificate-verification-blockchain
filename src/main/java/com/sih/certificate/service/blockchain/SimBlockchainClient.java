package com.sih.certificate.service.blockchain;

import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SimBlockchainClient implements BlockchainClient {

    private final Map<String, Long> store = new ConcurrentHashMap<>();

    @Override
    public TxResult storeCertificateHash(String hashHex) {
        long ts = Instant.now().getEpochSecond();
        store.put(hashHex, ts);
        // simulate a tx hash
        String fakeTx = "0xSIM" + Integer.toHexString(hashHex.hashCode()).replace("-", "A");
        return new TxResult(fakeTx);
    }

    @Override
    public VerifyResult verifyCertificateHash(String hashHex) {
        Long ts = store.get(hashHex);
        if (ts == null) return new VerifyResult(false, 0);
        return new VerifyResult(true, ts);
    }
}
