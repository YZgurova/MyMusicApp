package com.MyMusic.v1.api.rest;

import com.MyMusic.v1.services.AmazonClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@RequestMapping(value="/api/auth/storage")
@SecurityRequirement(name = "bearerAuth")
public class FileStorageController {
    private final AmazonClientService amazonClient;
    @Autowired
    FileStorageController(AmazonClientService amazonClient) {
        this.amazonClient = amazonClient;
    }
    @PostMapping
    @CrossOrigin(value = "*")
    public String uploadFile(@RequestPart(value = "file") MultipartFile file) throws Exception {
        return this.amazonClient.uploadFile(file);
    }
    @DeleteMapping
    public String deleteFile(@RequestPart(value = "url") String fileUrl) throws Exception {
        return this.amazonClient.deleteFileFromS3Bucket(fileUrl);
    }
}


