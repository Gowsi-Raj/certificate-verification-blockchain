package com.sih.certificate.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "certificates")
public class Certificate {

    @Id
    private String id;

    private String studentName;
    private String courseName;
    private String institutionName;
    private String issueDate;

    private String certificateHash;
    private String blockchainTxHash;

    private boolean onBlockchain;
    private boolean verifiedOnChain;

    // ✅ ADD THIS
    private long blockchainTimestamp;

    // ---------------- GETTERS + SETTERS ----------------

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getInstitutionName() {
        return institutionName;
    }

    public void setInstitutionName(String institutionName) {
        this.institutionName = institutionName;
    }

    public String getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(String issueDate) {
        this.issueDate = issueDate;
    }

    public String getCertificateHash() {
        return certificateHash;
    }

    public void setCertificateHash(String certificateHash) {
        this.certificateHash = certificateHash;
    }

    public String getBlockchainTxHash() {
        return blockchainTxHash;
    }

    public void setBlockchainTxHash(String blockchainTxHash) {
        this.blockchainTxHash = blockchainTxHash;
    }

    public boolean isOnBlockchain() {
        return onBlockchain;
    }

    public void setOnBlockchain(boolean onBlockchain) {
        this.onBlockchain = onBlockchain;
    }

    public boolean isVerifiedOnChain() {
        return verifiedOnChain;
    }

    public void setVerifiedOnChain(boolean verifiedOnChain) {
        this.verifiedOnChain = verifiedOnChain;
    }

    // ✅ REQUIRED FOR YOUR SERVICE
    public long getBlockchainTimestamp() {
        return blockchainTimestamp;
    }

    public void setBlockchainTimestamp(long blockchainTimestamp) {
        this.blockchainTimestamp = blockchainTimestamp;
    }
}
