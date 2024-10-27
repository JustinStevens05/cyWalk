package com.example.demo.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Column;

@Entity
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id; // Changed from int to Long for consistency with JpaRepository

    @Column(nullable = false)
    private String filePath;

    @Column(length = 500)
    private String description; // New field for image description

    public Image() {}

    public Image(String filePath, String description) {
        this.filePath = filePath;
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    // No setter for id since it's auto-generated

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
