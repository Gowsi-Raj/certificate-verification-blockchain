package com.sih.certificate.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class VerificationPageController {

    @GetMapping("/verify")
    public String verifyPage() {
        return "verify"; // loads verify.html
    }
}
