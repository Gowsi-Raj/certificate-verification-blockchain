package com.sih.certificate.controller;

import com.sih.certificate.model.Certificate;
import com.sih.certificate.repository.CertificateRepository;
import com.sih.certificate.service.CertificateService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;

@Controller
@RequestMapping("/admin")
public class AdminAuthController {

    private final CertificateService certificateService;
    private final CertificateRepository certificateRepository;

    public AdminAuthController(CertificateService certificateService,
                               CertificateRepository certificateRepository) {
        this.certificateService = certificateService;
        this.certificateRepository = certificateRepository;
    }

    // ✅ Login page
    @GetMapping("/login")
    public String loginPage() {
        return "admin-login";
    }

    // ✅ Login POST
    @PostMapping("/login")
    public String login(@RequestParam String username,
                        @RequestParam String password,
                        HttpSession session) {

        if ("admin".equals(username) && "admin123".equals(password)) {
            session.setAttribute("admin", true);
            return "redirect:/admin/dashboard";
        }

        return "redirect:/admin/login?error";
    }

    // ✅ Dashboard
    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {
        if (session.getAttribute("admin") == null) return "redirect:/admin/login";

        model.addAttribute("certificates", certificateRepository.findAll());
        return "admin-dashboard";
    }

    // ✅ Create certificate (matches form action /admin/certificates/create)
    @PostMapping("/certificates/create")
    public String createCertificate(@RequestParam String studentName,
                                    @RequestParam String courseName,
                                    @RequestParam String institutionName,
                                    @RequestParam String issueDate,
                                    HttpSession session,
                                    RedirectAttributes ra) {

        if (session.getAttribute("admin") == null) return "redirect:/admin/login";

        try {
            // If you already have createCertificate(String,String,String,String) in service, use that:
            certificateService.createCertificate(studentName, courseName, institutionName, issueDate);

            ra.addFlashAttribute("msg", "Certificate created!");
            return "redirect:/admin/dashboard";
        } catch (Exception e) {
            e.printStackTrace();
            ra.addFlashAttribute("err", "Create failed: " + e.getMessage());
            return "redirect:/admin/dashboard";
        }
    }

    // ✅ DELETE certificate
    @PostMapping("/certificates/{id}/delete")
    public String deleteCertificate(@PathVariable String id,
                                    HttpSession session,
                                    RedirectAttributes ra) {

        if (session.getAttribute("admin") == null) return "redirect:/admin/login";

        try {
            certificateRepository.deleteById(id);
            ra.addFlashAttribute("msg", "Deleted successfully!");
        } catch (Exception e) {
            e.printStackTrace();
            ra.addFlashAttribute("err", "Delete failed: " + e.getMessage());
        }

        return "redirect:/admin/dashboard";
    }

    // ✅ Logout
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }
}