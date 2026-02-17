package com.sih.certificate.dto;

import com.sih.certificate.model.Certificate;

public class VerifyResponse {

    private boolean valid;       // final result
    private boolean tampered;    // true if DB data changed
    private boolean onChain;     // true if found on blockchain
    private long timestamp;      // blockchain timestamp (if exists)
    private String message;
    private Certificate certificate;

    public VerifyResponse() {}

    public VerifyResponse(boolean valid, boolean tampered, boolean onChain, long timestamp,
                          String message, Certificate certificate) {
        this.valid = valid;
        this.tampered = tampered;
        this.onChain = onChain;
        this.timestamp = timestamp;
        this.message = message;
        this.certificate = certificate;
    }

    public boolean isValid() { return valid; }
    public void setValid(boolean valid) { this.valid = valid; }

    public boolean isTampered() { return tampered; }
    public void setTampered(boolean tampered) { this.tampered = tampered; }

    public boolean isOnChain() { return onChain; }
    public void setOnChain(boolean onChain) { this.onChain = onChain; }

    public long getTimestamp() { return timestamp; }
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public Certificate getCertificate() { return certificate; }
    public void setCertificate(Certificate certificate) { this.certificate = certificate; }
}