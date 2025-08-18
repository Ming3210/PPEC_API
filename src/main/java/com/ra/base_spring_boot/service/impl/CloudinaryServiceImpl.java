package com.ra.base_spring_boot.service.impl;


import com.cloudinary.Cloudinary;
import com.cloudinary.Transformation;
import com.cloudinary.utils.ObjectUtils;
import com.ra.base_spring_boot.service.interfaces.ICloudinaryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class CloudinaryServiceImpl implements ICloudinaryService {

    private final Cloudinary cloudinary;

    @Override
    public String uploadImage(MultipartFile file) {
        return uploadImage(file, "general");
    }

    @Override
    public String uploadImage(MultipartFile file, String folder) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("File không được để trống");
        }

        if (!isValidImageType(Objects.requireNonNull(file.getContentType()))) {
            throw new IllegalArgumentException("Chỉ chấp nhận file ảnh (JPG, PNG, GIF, WEBP)");
        }

        if (file.getSize() > 5 * 1024 * 1024) {
            throw new IllegalArgumentException("Kích thước file không được vượt quá 5MB");
        }

        try {
            String originalFilename = file.getOriginalFilename();
            String fileExtension = originalFilename != null ?
                    originalFilename.substring(originalFilename.lastIndexOf(".")) : "";
            String uniqueFilename = UUID.randomUUID().toString() + fileExtension;

            Map<String, Object> uploadParams = ObjectUtils.asMap(
                    "folder", folder,
                    "public_id", folder + "/" + uniqueFilename.replace(".", "_"),
                    "resource_type", "image",
                    "transformation", new Transformation()
                            .quality("auto")
                            .fetchFormat("auto")
            );


            Map uploadResult = cloudinary.uploader().upload(file.getBytes(), uploadParams);
            String imageUrl = uploadResult.get("secure_url").toString();

            log.info("Successfully uploaded image to Cloudinary: {}", imageUrl);
            return imageUrl;

        } catch (IOException e) {
            log.error("Failed to upload image to Cloudinary", e);
            throw new RuntimeException("Đăng ảnh lên không thành công: " + e.getMessage(), e);
        } catch (Exception e) {
            log.error("Unexpected error during image upload", e);
            throw new RuntimeException("Lỗi không xác định khi upload ảnh: " + e.getMessage(), e);
        }
    }

    @Override
    public void deleteImage(String publicId) {
        if (publicId == null || publicId.trim().isEmpty()) {
            return;
        }

        try {
            Map result = cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
            log.info("Deleted image from Cloudinary: {}, result: {}", publicId, result.get("result"));
        } catch (Exception e) {
            log.error("Failed to delete image from Cloudinary: {}", publicId, e);
        }
    }

    @Override
    public String extractPublicIdFromUrl(String imageUrl) {
        if (imageUrl == null || imageUrl.trim().isEmpty()) {
            return null;
        }

        try {

            String[] parts = imageUrl.split("/");
            if (parts.length >= 2) {
                String filenameWithExt = parts[parts.length - 1];
                String folder = parts[parts.length - 2];
                String filename = filenameWithExt.contains(".") ?
                        filenameWithExt.substring(0, filenameWithExt.lastIndexOf(".")) : filenameWithExt;
                return folder + "/" + filename;
            }
        } catch (Exception e) {
            log.error("Failed to extract public_id from URL: {}", imageUrl, e);
        }

        return null;
    }

    private boolean isValidImageType(String contentType) {
        return contentType.equals("image/jpeg") ||
                contentType.equals("image/png") ||
                contentType.equals("image/gif") ||
                contentType.equals("image/webp") ||
                contentType.equals("image/jpg");
    }
}
