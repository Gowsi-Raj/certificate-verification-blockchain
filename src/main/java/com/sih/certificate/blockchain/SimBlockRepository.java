package com.sih.certificate.blockchain;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface SimBlockRepository extends MongoRepository<SimBlock, String> {
    Optional<SimBlock> findByDataHash(String dataHash);
    List<SimBlock> findAllByOrderByIndexAsc();
}
