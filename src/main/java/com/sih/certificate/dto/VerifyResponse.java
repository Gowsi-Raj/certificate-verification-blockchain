package com.sih.certificate.dto;

public class VerifyResponse {
    private boolean valid;
    private long blockchainTimestamp;
    private String message;
    private String certificateHash;

    public VerifyResponse() {}

    public VerifyResponse(boolean valid, long blockchainTimestamp, String message, String certificateHash) {
        this.valid = valid;
        this.blockchainTimestamp = blockchainTimestamp;
        this.message = message;
        this.certificateHash = certificateHash;
    }

    public boolean isValid() { return valid; }
    public void setValid(boolean valid) { this.valid = valid; }

    public long getBlockchainTimestamp() { return blockchainTimestamp; }
    public void setBlockchainTimestamp(long blockchainTimestamp) { this.blockchainTimestamp = blockchainTimestamp; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public String getCertificateHash() { return certificateHash; }
    public void setCertificateHash(String certificateHash) { this.certificateHash = certificateHash; }
}
