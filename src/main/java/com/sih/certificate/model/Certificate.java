package com.sih.certificate.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Document(collection = "certificates")
public class Certificate {

    @Id
    private String id;

    private String studentName;
    private String courseName;
    private String institutionName;
    private LocalDate issueDate;

    private String certificateHash;
    private String qrFilename;

    public Certificate() {}

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getStudentName() { return studentName; }
    public void setStudentName(String studentName) { this.studentName = studentName; }

    public String getCourseName() { return courseName; }
    public void setCourseName(String courseName) { this.courseName = courseName; }

    public String getInstitutionName() { return institutionName; }
    public void setInstitutionName(String institutionName) { this.institutionName = institutionName; }

    public LocalDate getIssueDate() { return issueDate; }
    public void setIssueDate(LocalDate issueDate) { this.issueDate = issueDate; }

    public String getCertificateHash() { return certificateHash; }
    public void setCertificateHash(String certificateHash) { this.certificateHash = certificateHash; }

    public String getQrFilename() { return qrFilename; }
    public void setQrFilename(String qrFilename) { this.qrFilename = qrFilename; }
}
