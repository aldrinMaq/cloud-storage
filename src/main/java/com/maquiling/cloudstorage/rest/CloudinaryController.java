package com.maquiling.cloudstorage.rest;

import com.cloudinary.api.ApiResponse;
import com.maquiling.cloudstorage.service.CloudinaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/cloudinary")
public class CloudinaryController {

    private final CloudinaryService cloudinaryService;

    @Autowired
    public CloudinaryController(CloudinaryService cloudinaryService) {
        this.cloudinaryService = cloudinaryService;
    }

    // Existing methods...

    @PostMapping("/create-folder")
    public ResponseEntity<?> createFolder(@RequestParam String folderPath) {
        try {
            ApiResponse response = cloudinaryService.createFolder(folderPath);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error creating folder: " + e.getMessage());
        }
    }

    @GetMapping("/images/{folderName}")
    public ResponseEntity<?> getImagesFromFolder(@PathVariable String folderName) {
        try {
            ApiResponse response = cloudinaryService.getImagesFromFolder(folderName);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error fetching images: " + e.getMessage());
        }
    }

//    @DeleteMapping("/delete-image")
//    public ResponseEntity<?> deleteImage(@RequestParam String publicId,
//                                         @RequestParam(required = false, defaultValue = "false") boolean invalidate) {
//        try {
//            Map response = cloudinaryService.deleteImage(publicId, invalidate);
//            return ResponseEntity.ok(response);
//        } catch (Exception e) {
//            return ResponseEntity.internalServerError().body("Error deleting image: " + e.getMessage());
//        }
//    }

}