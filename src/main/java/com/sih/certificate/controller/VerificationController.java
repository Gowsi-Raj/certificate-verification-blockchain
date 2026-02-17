package com.sih.certificate.controller;

import com.sih.certificate.dto.VerifyResponse;
import com.sih.certificate.service.CertificateService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class VerificationController {

    private final CertificateService certificateService;

    public VerificationController(CertificateService certificateService) {
        this.certificateService = certificateService;
    }

    @GetMapping("/verify")
    public String verifyPage() {
        return "scan";
    }

    @GetMapping("/verify/{hash}")
    public String verifyByQr(@PathVariable String hash, Model model) {

        VerifyResponse resp = certificateService.verifyByHash(hash);
        model.addAttribute("resp", resp);

        if (!resp.isValid()) {
            return "verify-error";
        }

        model.addAttribute("cert", resp.getCertificate());
        return "verify-success";
    }

    // ✅ API used by UI / Postman
    @PostMapping("/api/verify")
    @ResponseBody
    public VerifyResponse verifyByHash(@RequestParam String hash) {
        return certificateService.verifyByHash(hash);
    }
}