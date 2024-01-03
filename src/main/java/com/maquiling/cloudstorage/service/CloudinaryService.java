package com.maquiling.cloudstorage.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.Search;
import com.cloudinary.api.ApiResponse;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@Service
public class CloudinaryService {

    private final Cloudinary cloudinary;

    public CloudinaryService(
            @Value("${cloudinary.cloud-name}") String cloudName,
            @Value("${cloudinary.api-key}") String apiKey,
            @Value("${cloudinary.api-secret}") String apiSecret) {
        this.cloudinary = new Cloudinary(ObjectUtils.asMap(
                "cloud_name", cloudName,
                "api_key", apiKey,
                "api_secret", apiSecret,
                "secure", true));
    }

    public ApiResponse getAllImages() throws Exception {
        return cloudinary.api().resources(ObjectUtils.emptyMap());
    }

    public Map uploadFile(File file) throws IOException {
        return cloudinary.uploader().upload(file, ObjectUtils.emptyMap());
    }

    public ApiResponse createFolder(String folderPath) throws Exception {
        return cloudinary.api().createFolder(folderPath, ObjectUtils.emptyMap());
    }

    public ApiResponse getImagesFromFolder(String folderName) throws Exception {
        return cloudinary.search()
                .expression("folder:" + folderName)
                .maxResults(100)
                .execute();
    }

//    public Map deleteImage(String publicId, boolean invalidate) throws Exception {
//        Map options = ObjectUtils.asMap("invalidate", invalidate);
//        return cloudinary.uploader().destroy(publicId, options);
//    }

//    public void deleteImages(List<String> publicIds) throws Exception {
//        cloudinary.api().deleteResources(publicIds, ObjectUtils.emptyMap());
//    }

    public Map deleteResources(List<String> publicIds) throws Exception {
        // Deleting multiple resources by public IDs
        return cloudinary.api().deleteResources(publicIds, ObjectUtils.emptyMap());
    }

    public Map uploadImageToFolder(MultipartFile file, String folderPath) throws IOException {
        File uploadedFile = convertMultiPartToFile(file);
        Map uploadParams = ObjectUtils.asMap(
                "folder", folderPath,
                "use_filename", true,
                "unique_filename", false
        );
        return cloudinary.uploader().upload(uploadedFile, uploadParams);
    }

    private File convertMultiPartToFile(MultipartFile file) throws IOException {
        File convFile = new File(System.getProperty("java.io.tmpdir")+"/"+file.getOriginalFilename());
        file.transferTo(convFile);
        return convFile;
    }

    public ApiResponse searchImagesByPartialDisplayName(String folderName, String partialDisplayName) throws Exception {
        // Sanitize and prepare the search expression
        String sanitizedDisplayName = sanitizeForSearch(partialDisplayName);
        String searchQuery = "folder:" + folderName;

        // Add the display name criteria to the search expression
        if (!sanitizedDisplayName.isEmpty()) {
            // Using ':' operator for a tokenized search
            searchQuery += " AND display_name:" + sanitizedDisplayName + "*";
        }

        // Execute the search using Cloudinary's API
        return cloudinary.search()
                .expression(searchQuery)
                .maxResults(100)
                .execute();
    }

    /**
     * Sanitizes the input string for use in a search expression.
     * This method should remove any special characters or patterns that are not allowed in search queries.
     *
     * @param input The input string to sanitize.
     * @return A sanitized string safe to include in a search query.
     */
    private String sanitizeForSearch(String input) {
        // Implement necessary sanitization logic
        // For example, escaping special characters, trimming whitespace, etc.
        return input.trim(); // Placeholder - replace with actual sanitization logic as needed
    }
}
