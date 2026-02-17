package com.sih.certificate.blockchain;

import com.sih.certificate.service.HashUtil;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SimulatedBlockchainGateway implements BlockchainGateway {

    private final SimBlockRepository repo;

    public SimulatedBlockchainGateway(SimBlockRepository repo) {
        this.repo = repo;
    }

    @Override
    public BlockchainResult storeHash(String certificateHash) {
        // already exists -> return existing proof
        var existing = repo.findByDataHash(certificateHash);
        if (existing.isPresent()) {
            return BlockchainResult.ok(existing.get().getBlockHash(), existing.get().getTimestamp());
        }

        List<SimBlock> blocks = repo.findAllByOrderByIndexAsc();

        int index = blocks.size();
        String prevHash = (index == 0) ? "GENESIS" : blocks.get(index - 1).getBlockHash();
        long ts = System.currentTimeMillis();

        // include previousHash so chain works
        String blockHash = HashUtil.sha256Hex(index + "|" + certificateHash);

        SimBlock b = new SimBlock();
        b.setIndex(index);
        b.setDataHash(certificateHash);
        b.setPreviousHash(prevHash);
        b.setTimestamp(ts);
        b.setBlockHash(blockHash);

        repo.save(b);

        return BlockchainResult.ok(blockHash, ts);
    }

    @Override
    public BlockchainResult verifyHash(String certificateHash) {
        var block = repo.findByDataHash(certificateHash);
        if (block.isEmpty()) return BlockchainResult.notFound();

        // if you want strict chain validation later, we can add here.
        return BlockchainResult.ok(block.get().getBlockHash(), block.get().getTimestamp());
    }
}
