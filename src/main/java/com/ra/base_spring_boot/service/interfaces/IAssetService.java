package com.ra.base_spring_boot.service.interfaces;

import com.ra.base_spring_boot.dto.request.AssetRequestDTO;
import com.ra.base_spring_boot.dto.request.StudentAssetRequestDTO;
import com.ra.base_spring_boot.dto.response.AssetResponseDTO;
import com.ra.base_spring_boot.dto.response.PaginationResponse;

import java.util.List;

public interface IAssetService {
    AssetResponseDTO createAsset(AssetRequestDTO assetRequestDTO);

    AssetResponseDTO updateAsset(Long id, AssetRequestDTO assetRequestDTO);

    void deleteAsset(Long id);

    PaginationResponse<AssetResponseDTO> getAllAssets(
            int page, int size, String sortBy, Boolean sortDirection, String keyword);

    AssetResponseDTO getAssetById(Long id);

    AssetResponseDTO studentUploadAsset(StudentAssetRequestDTO studentAssetRequestDTO);

    AssetResponseDTO studentUpdateAsset(Long id, StudentAssetRequestDTO studentAssetRequestDTO);

    PaginationResponse<AssetResponseDTO> getAllLoginUserAssets(int page, int size, String sortBy, Boolean sortDirection);
}
