package com.example.demo.controller;
import com.example.demo.config.StorageProperties;

import com.example.demo.entity.Image;
import com.example.demo.repository.ImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Optional;

@RestController
@RequestMapping("/images")
public class ImageController {

    @Autowired
    private StorageProperties storageProperties;

    @Autowired
    private ImageRepository imageRepository;

    @GetMapping(value = "/{id}", produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<byte[]> getImageById(@PathVariable Long id) {
        Optional<Image> imageOpt = imageRepository.findById(id);
        if (imageOpt.isPresent()) {
            Image image = imageOpt.get();
            File imageFile = new File(image.getFilePath());
            if (imageFile.exists()) {
                try {
                    byte[] imageBytes = Files.readAllBytes(imageFile.toPath());
                    return ResponseEntity.ok().body(imageBytes);
                } catch (IOException e) {
                    return ResponseEntity.status(500).build();
                }
            } else {
                return ResponseEntity.status(404).build();
            }
        } else {
            return ResponseEntity.status(404).build();
        }
    }

    @PostMapping
    public ResponseEntity<String> handleFileUpload(
            @RequestParam("image") MultipartFile imageFile,
            @RequestParam(value = "description", required = false) String description) {

        if (imageFile.isEmpty()) {
            return ResponseEntity.badRequest().body("No file selected to upload.");
        }

        try {

            File directory = new File(storageProperties.getDirectory());
            if (!directory.exists()) {
                directory.mkdirs();
            }

            String filename = System.currentTimeMillis() + "_" + imageFile.getOriginalFilename();
            File destinationFile = new File(directory, filename);
            imageFile.transferTo(destinationFile);

            Image image = new Image(destinationFile.getAbsolutePath(), description);
            imageRepository.save(image);

            return ResponseEntity.ok("File uploaded successfully: " + destinationFile.getAbsolutePath() +
                    "\nImage ID: " + image.getId());
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Failed to upload file: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteImageById(@PathVariable Long id) {
        Optional<Image> imageOpt = imageRepository.findById(id);
        if (imageOpt.isPresent()) {
            Image image = imageOpt.get();
            File imageFile = new File(image.getFilePath());
            if (imageFile.exists()) {
                if (imageFile.delete()) {
                    imageRepository.delete(image);
                    return ResponseEntity.ok("Image deleted successfully.");
                } else {
                    return ResponseEntity.status(500).body("Failed to delete image file.");
                }
            } else {
                imageRepository.delete(image);
                return ResponseEntity.ok("Image record deleted, but file was not found.");
            }
        } else {
            return ResponseEntity.status(404).body("Image not found.");
        }
    }

}