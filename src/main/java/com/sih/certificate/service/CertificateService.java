package com.sih.certificate.service;

import com.google.zxing.WriterException;
import com.sih.certificate.QRCodeUtil;
import com.sih.certificate.dto.VerifyResponse;
import com.sih.certificate.model.Certificate;
import com.sih.certificate.repository.CertificateRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class CertificateService {

    private final CertificateRepository repo;
    private final BlockchainService blockchainService;
    private final QRCodeUtil qrCodeUtil;

    @Value("${app.public-base-url:http://localhost:8081}")
    private String publicBaseUrl;

    public CertificateService(CertificateRepository repo,
                              BlockchainService blockchainService,
                              QRCodeUtil qrCodeUtil) {
        this.repo = repo;
        this.blockchainService = blockchainService;
        this.qrCodeUtil = qrCodeUtil;
    }

    // ✅ Used by controllers (form submit)
    public Certificate createCertificate(String studentName,
                                         String courseName,
                                         String institutionName,
                                         String issueDate) throws IOException, WriterException {

        Certificate cert = new Certificate();
        cert.setStudentName(studentName);
        cert.setCourseName(courseName);
        cert.setInstitutionName(institutionName);

        if (issueDate != null && !issueDate.isBlank()) {
            cert.setIssueDate(LocalDate.parse(issueDate.trim())); // yyyy-MM-dd
        } else {
            cert.setIssueDate(null);
        }

        return createCertificate(cert);
    }

    // ✅ Core logic (hash + blockchain + qr + save)
    public Certificate createCertificate(Certificate cert) throws IOException, WriterException {

        String canonical = canonical(
                cert.getStudentName(),
                cert.getCourseName(),
                cert.getInstitutionName(),
                cert.getIssueDate()
        );

        // ✅ NEW: SHA-256 (64 hex chars)
        String hash = sha256Hex(canonical);
        cert.setCertificateHash(hash);

        // prevent duplicates
        Optional<Certificate> existing = repo.findByCertificateHash(hash);
        if (existing.isPresent()) return existing.get();

        // blockchain issue (don’t crash app if it fails)
        try {
            blockchainService.issueCertificateHash(hash);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // QR encodes public verify URL
        String verifyUrl = trimTrailingSlash(publicBaseUrl) + "/verify/" + hash;
        String qrFile = qrCodeUtil.generateQrToFile(verifyUrl, hash);
        cert.setQrFilename(qrFile);

        return repo.save(cert);
    }

    public List<Certificate> getAllCertificates() {
        return repo.findAll();
    }

    public Optional<Certificate> findByHash(String hash) {
        if (hash == null) return Optional.empty();
        return repo.findByCertificateHash(hash.trim());
    }

    /**
     * ✅ Proper verification:
     * 1) Must exist in DB
     * 2) Recompute hash from stored fields -> detect tampering
     * 3) Verify on blockchain -> must exist on chain
     *
     * Supports old MD5 hashes (32 chars) + new SHA-256 (64 chars).
     */
    public VerifyResponse verifyByHash(String hash) {
        VerifyResponse resp = new VerifyResponse();
        resp.setValid(false);
        resp.setTampered(false);
        resp.setOnChain(false);
        resp.setTimestamp(0L);
        resp.setCertificate(null);

        if (hash == null || hash.isBlank()) {
            resp.setMessage("Hash is required");
            return resp;
        }

        String clean = hash.trim();

        Optional<Certificate> certOpt = repo.findByCertificateHash(clean);
        if (certOpt.isEmpty()) {
            resp.setMessage("Certificate not found in database");
            return resp;
        }

        Certificate cert = certOpt.get();
        resp.setCertificate(cert);

        // ---- (A) Tamper detection: recompute from stored fields ----
        String canonical = canonical(
                cert.getStudentName(),
                cert.getCourseName(),
                cert.getInstitutionName(),
                cert.getIssueDate()
        );

        String storedHash = cert.getCertificateHash();
        String recomputed;

        // Backward compatibility:
        // old: MD5 = 32 hex
        // new: SHA256 = 64 hex
        if (storedHash != null && storedHash.length() == 32) {
            // old certificates created when you used MD5
            recomputed = md5Hex(canonical);
        } else {
            // new certificates
            recomputed = sha256Hex(canonical);
        }

        if (storedHash == null || !storedHash.equalsIgnoreCase(recomputed)) {
            resp.setTampered(true);
            resp.setValid(false);
            resp.setMessage("⚠️ Certificate data has been TAMPERED (hash mismatch)");
            return resp; // stop here
        }

        // ---- (B) Blockchain verification ----
        try {
            BlockchainService.VerifyResult vr = blockchainService.verifyCertificateHash(clean);
            resp.setOnChain(vr.exists());
            resp.setTimestamp(vr.timestamp());

            if (vr.exists()) {
                resp.setValid(true);
                resp.setMessage("✅ Certificate is VALID (DB + hash match + blockchain confirmed)");
            } else {
                resp.setValid(false);
                resp.setMessage("❌ Hash not found on blockchain (not issued / invalid)");
            }
        } catch (Exception e) {
            // blockchain error -> don’t crash, but mark invalid (since blockchain is main motto)
            resp.setValid(false);
            resp.setOnChain(false);
            resp.setMessage("❌ Blockchain verification failed: " + e.getMessage());
        }

        return resp;
    }

    // ================== helpers ==================

    private String canonical(String student, String course, String inst, LocalDate date) {
        return (safe(student) + "|" + safe(course) + "|" + safe(inst) + "|" + (date == null ? "" : date))
                .trim()
                .toLowerCase();
    }

    private String safe(String s) {
        return s == null ? "" : s.trim();
    }

    private String trimTrailingSlash(String s) {
        if (s == null) return "";
        return s.endsWith("/") ? s.substring(0, s.length() - 1) : s;
    }

    private String sha256Hex(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] out = md.digest(input.getBytes(StandardCharsets.UTF_8));
            return toHex(out);
        } catch (Exception e) {
            throw new RuntimeException("SHA-256 failed", e);
        }
    }

    private String md5Hex(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] out = md.digest(input.getBytes(StandardCharsets.UTF_8));
            return toHex(out);
        } catch (Exception e) {
            throw new RuntimeException("MD5 failed", e);
        }
    }

    private String toHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder(bytes.length * 2);
        for (byte b : bytes) sb.append(String.format("%02x", b));
        return sb.toString();
    }
}