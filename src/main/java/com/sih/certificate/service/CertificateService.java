package com.sih.certificate.service;

import com.sih.certificate.dto.VerifyResponse;
import com.sih.certificate.model.Certificate;
import com.sih.certificate.repository.CertificateRepository;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Service;

@Service
public class CertificateService {

    private final CertificateRepository repo;
    private final BlockchainService blockchainService;

    public CertificateService(CertificateRepository repo, BlockchainService blockchainService) {
        this.repo = repo;
        this.blockchainService = blockchainService;
    }

    // ✅ CREATE: backend auto-generates hash (no manual hash input)
    public Certificate createCertificate(Certificate c) throws Exception {

        String data = c.getStudentName() + c.getCourseName() + c.getInstitutionName() + c.getIssueDate();
        String hash = DigestUtils.sha256Hex(data);

        c.setCertificateHash(hash);

        // store on blockchain (or simulation)
        String txHash = blockchainService.issueCertificateHash(hash);
        c.setBlockchainTxHash(txHash);
        c.setOnBlockchain(true);

        return repo.save(c);
    }

    // ✅ VERIFY: recompute from DB fields and compare
    public VerifyResponse verifyCertificate(String hashFromUrl) throws Exception {

        // 1) load cert from DB using the hash you scanned / typed
        Certificate cert = repo.findByCertificateHash(hashFromUrl)
                .orElseThrow(() -> new RuntimeException("Certificate not found in DB"));

        // 2) recompute hash from DB fields (THIS IS YOUR MAGIC)
        String data = cert.getStudentName() + cert.getCourseName() + cert.getInstitutionName() + cert.getIssueDate();
        String recomputedHash = DigestUtils.sha256Hex(data);

        // 3) if DB was tampered (name/course/date changed), recomputed != stored
        if (!recomputedHash.equals(cert.getCertificateHash())) {
            return new VerifyResponse(false, 0L, "Invalid/Tampered Certificate (DB modified)", hashFromUrl);
        }

        // 4) now check blockchain (or simulated chain)
        BlockchainService.VerifyResult vr = blockchainService.verifyCertificateHash(recomputedHash);

        if (!vr.exists()) {
            return new VerifyResponse(false, 0L, "Hash not found on Blockchain", hashFromUrl);
        }

        return new VerifyResponse(true, vr.timestamp(), "Certificate VALID ✅", hashFromUrl);
    }
}
