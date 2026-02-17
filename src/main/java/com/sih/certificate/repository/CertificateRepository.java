package com.sih.certificate.repository;

import com.sih.certificate.model.Certificate;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface CertificateRepository extends MongoRepository<Certificate, String> {

    Optional<Certificate> findByCertificateHash(String certificateHash);

    List<Certificate> findByStudentName(String studentName);
}
