package com.ra.base_spring_boot.controller;

import com.ra.base_spring_boot.dto.request.AssetRequestDTO;
import com.ra.base_spring_boot.dto.response.APIResponse;
import com.ra.base_spring_boot.dto.response.AssetResponseDTO;
import com.ra.base_spring_boot.service.interfaces.IAssetService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping ("/api/assets")
public class AssetController {
    @Autowired
    private IAssetService assetService;

    @GetMapping
    public ResponseEntity<APIResponse<?>> getAllAssets(@RequestParam(defaultValue = "0") Integer page,
                                                       @RequestParam(defaultValue = "10") Integer itemPage,
                                                       @RequestParam(defaultValue = "id") String sortBy,
                                                       @RequestParam(defaultValue = "true") Boolean orderBy) {
        return new ResponseEntity<>(new APIResponse<>(true, "Get all assets successfully!", assetService.getAllAssets(page, itemPage, sortBy, orderBy), HttpStatus.OK, LocalDateTime.now()), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<APIResponse<AssetResponseDTO>> createAsset(
            @Valid @RequestBody AssetRequestDTO assetRequestDTO
    ) {
        return ResponseEntity.ok(
                new APIResponse<>(
                        true,
                        "Create asset successfully!",
                        assetService.createAsset(assetRequestDTO),
                        HttpStatus.CREATED,
                        LocalDateTime.now() 
                )
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<APIResponse<AssetResponseDTO>> updateAsset(
            @PathVariable Long id,
            @Valid @RequestBody AssetRequestDTO assetRequestDTO
    ) {
        return ResponseEntity.ok(
                new APIResponse<>(
                        true,
                        "Update asset successfully!",
                        assetService.updateAsset(id, assetRequestDTO),
                        HttpStatus.OK,
                        LocalDateTime.now() 
                )
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<APIResponse<Void>> deleteAsset(@PathVariable Long id) {
        assetService.deleteAsset(id);
        return ResponseEntity.ok(
                new APIResponse<>(
                        true,
                        "Delete asset successfully!",
                        null,
                        HttpStatus.NO_CONTENT,
                        LocalDateTime.now() 
                )
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<APIResponse<AssetResponseDTO>> getAssetById(@PathVariable Long id) {
        return ResponseEntity.ok(
                new APIResponse<>(
                        true,
                        "Get asset successfully!",
                        assetService.getAssetById(id),
                        HttpStatus.OK,
                        LocalDateTime.now()
                )
        );
    }



}
