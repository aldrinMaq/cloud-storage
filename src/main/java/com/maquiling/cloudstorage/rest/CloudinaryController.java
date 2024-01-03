package com.maquiling.cloudstorage.rest;

import com.cloudinary.api.ApiResponse;
import com.maquiling.cloudstorage.model.repo.UserRepo;
import com.maquiling.cloudstorage.service.CloudinaryService;
import com.maquiling.cloudstorage.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/cloudinary")
public class CloudinaryController {

    private final CloudinaryService cloudinaryService;

    @Autowired
    public CloudinaryController(CloudinaryService cloudinaryService) {
        this.cloudinaryService = cloudinaryService;
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

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteImages(@RequestBody List<String> publicIds) {
        try {
            Map result = cloudinaryService.deleteResources(publicIds);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error deleting images: " + e.getMessage());
        }
    }

    @GetMapping("/search")
    public ResponseEntity<?> searchImagesByDisplayName(
            @RequestParam String folderName,
            @RequestParam String displayName) {
        try {
            ApiResponse apiResponse = cloudinaryService.searchImagesByPartialDisplayName(folderName, displayName);
            return ResponseEntity.ok(apiResponse);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error during image search by display name: " + e.getMessage());
        }
    }

}