package com.sih.certificate.controller;

import com.sih.certificate.dto.VerifyResponse;
import com.sih.certificate.model.Certificate;
import com.sih.certificate.service.CertificateService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/certificates")
@CrossOrigin("*")
public class CertificateController {

    private final CertificateService service;

    public CertificateController(CertificateService service) {
        this.service = service;
    }

    // POST: create certificate
    @PostMapping
    public Certificate create(@RequestBody Certificate certificate) throws Exception {
        return service.createCertificate(certificate);
    }

    // GET: verify certificate by hash
    @GetMapping("/verify/{hash}")
    public VerifyResponse verify(@PathVariable String hash) throws Exception {
        return service.verifyCertificate(hash);
    }
}
