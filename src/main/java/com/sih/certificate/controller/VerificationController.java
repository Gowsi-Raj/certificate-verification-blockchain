package com.sih.certificate.controller;

import com.sih.certificate.dto.VerifyResponse;
import com.sih.certificate.service.BlockchainService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class VerificationController {

    private final BlockchainService blockchain;

    public VerificationController(BlockchainService blockchain) {
        this.blockchain = blockchain;
    }

    // ✅ Browser: http://localhost:8081/verify/hello123
    @GetMapping("/verify/{hash}")
    public String verifyPage(@PathVariable String hash, Model model) {
        try {
            BlockchainService.VerifyResult vr = blockchain.verifyCertificateHash(hash);

            VerifyResponse res;
            if (!vr.exists() || vr.timestamp() == 0L) {
                res = new VerifyResponse(false, 0L, "Invalid or Tampered Certificate", hash);
                model.addAttribute("res", res);
                return "verify-error";
            }

            res = new VerifyResponse(true, vr.timestamp(), "Certificate is VALID", hash);
            model.addAttribute("res", res);
            return "verify-success";

        } catch (Exception e) {
            VerifyResponse res = new VerifyResponse(false, 0L, "Blockchain Error: " + e.getMessage(), hash);
            model.addAttribute("res", res);
            return "verify-error";
        }
    }
}