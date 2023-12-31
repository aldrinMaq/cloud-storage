package com.maquiling.cloudstorage.rest;

import com.maquiling.cloudstorage.service.CloudinaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.Map;


@RestController
@RequestMapping("/api/images")
public class ImageController {

    @Autowired
    private CloudinaryService cloudinaryService;

    @GetMapping
    public ResponseEntity<?> listImages() {
        try {
            return ResponseEntity.ok(cloudinaryService.getAllImages());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error fetching images");
        }
    }

    @PostMapping("/upload")
    public ResponseEntity<?> uploadImage(@RequestParam("file") MultipartFile file) {
        try {
            File convFile = new File(System.getProperty("java.io.tmpdir")+"/"+file.getOriginalFilename());
            file.transferTo(convFile);

            Map uploadResult = cloudinaryService.uploadFile(convFile);
            return ResponseEntity.ok(uploadResult);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error uploading image: " + e.getMessage());
        }
    }

    @PostMapping("/upload-to-folder")
    public ResponseEntity<?> uploadImageToFolder(@RequestParam("file") MultipartFile file, @RequestParam("folderPath") String folderPath) {
        try {
            Map uploadResult = cloudinaryService.uploadFile(file, folderPath);
            return ResponseEntity.ok(uploadResult);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error uploading image: " + e.getMessage());
        }
    }
}
