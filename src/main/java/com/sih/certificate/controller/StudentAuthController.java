package com.sih.certificate.controller;

import com.sih.certificate.repository.CertificateRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/student")
public class StudentAuthController {

    private final CertificateRepository certificateRepository;

    public StudentAuthController(CertificateRepository certificateRepository) {
        this.certificateRepository = certificateRepository;
    }

    @GetMapping("/login")
    public String loginPage() {
        return "student-login";
    }

    @PostMapping("/login")
public String login(@RequestParam String username,
                    @RequestParam String password,
                    HttpSession session) {

    // simple demo check (you can change later)
    if (username == null || username.isBlank() || password == null || password.isBlank()) {
        return "redirect:/student/login?error";
    }

    session.setAttribute("student", username);
    return "redirect:/student/dashboard";
}


    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {

        String student = (String) session.getAttribute("student");
        if (student == null) return "redirect:/student/login";

        model.addAttribute("certificates",
                certificateRepository.findByStudentName(student));

        return "student-dashboard";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }
}
