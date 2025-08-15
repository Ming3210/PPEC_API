package com.ra.base_spring_boot.service.interfaces;


import org.springframework.web.multipart.MultipartFile;

public interface ICloudinaryService {
    String uploadImage(MultipartFile file);
    String uploadImage(MultipartFile file, String folder);
    void deleteImage(String publicId);
    String extractPublicIdFromUrl(String imageUrl);
}
