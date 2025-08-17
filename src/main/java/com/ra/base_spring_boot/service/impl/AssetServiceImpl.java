package com.ra.base_spring_boot.service.impl;

import com.ra.base_spring_boot.dto.request.AssetRequestDTO;
import com.ra.base_spring_boot.dto.response.AssetResponseDTO;
import com.ra.base_spring_boot.dto.response.PaginationResponse;
import com.ra.base_spring_boot.model.Asset;
import com.ra.base_spring_boot.model.User;
import com.ra.base_spring_boot.repository.AssetRepository;
import com.ra.base_spring_boot.repository.UserRepository;
import com.ra.base_spring_boot.service.interfaces.IAssetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class AssetServiceImpl implements IAssetService {
    @Autowired
    private AssetRepository assetRepository;

    @Autowired
    private UserRepository userRepository;


    @Override
    public AssetResponseDTO createAsset( AssetRequestDTO assetRequestDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User creator = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found with username: " + username));

        Asset asset = new Asset();
        asset.setCode(assetRequestDTO.getCode());
        asset.setName(assetRequestDTO.getName());
        asset.setNotes(assetRequestDTO.getNotes());
        asset.setCreatedAt(LocalDate.now());
        asset.setUpdatedAt(LocalDate.now());
        asset.setCreator(creator);
        asset.setUpdater(creator);


        User assignedUser = userRepository.findById(assetRequestDTO.getAssignedUserId())
                .orElseThrow(() -> new RuntimeException("User not found with id: " + assetRequestDTO.getAssignedUserId()));
        asset.setAssignedUser(assignedUser);
        Asset savedAsset = assetRepository.save(asset);

        return AssetResponseDTO.builder()
                .id(savedAsset.getId())
                .code(savedAsset.getCode())
                .name(savedAsset.getName())
                .notes(savedAsset.getNotes())
                .assignedUserId(assignedUser.getId())
                .assignedUserName(savedAsset.getAssignedUser() != null ? assignedUser.getFullName() : null)
                .assignedUserEmail(savedAsset.getAssignedUser() != null ? assignedUser.getEmail() : null)
                .assignedUserPhone(savedAsset.getAssignedUser() != null ? assignedUser.getPhoneNumber() : null)
                .build();
    }

    @Override
    public AssetResponseDTO updateAsset(Long id, AssetRequestDTO assetRequestDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User creator = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found with username: " + username));

        Asset asset = assetRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Asset not found with id: " + id));

        asset.setCode(assetRequestDTO.getCode());
        asset.setName(assetRequestDTO.getName());
        asset.setNotes(assetRequestDTO.getNotes());

        User assignedUser = userRepository.findById(assetRequestDTO.getAssignedUserId())
                .orElseThrow(() -> new RuntimeException("User not found with id: " + assetRequestDTO.getAssignedUserId()));
        asset.setAssignedUser(assignedUser);

        asset.setUpdatedAt(LocalDate.now());
        asset.setUpdater(creator);

        Asset updatedAsset = assetRepository.save(asset);

        return AssetResponseDTO.builder()
                .id(updatedAsset.getId())
                .code(updatedAsset.getCode())
                .name(updatedAsset.getName())
                .assignedUserId(assignedUser.getId())
                .notes(updatedAsset.getNotes())
                .assignedUserName(updatedAsset.getAssignedUser() != null ? assignedUser.getFullName() : null)
                .assignedUserEmail(updatedAsset.getAssignedUser() != null ? assignedUser.getEmail() : null)
                .assignedUserPhone(updatedAsset.getAssignedUser() != null ? assignedUser.getPhoneNumber() : null)
                .build();
    }

    @Override
    public void deleteAsset(Long id) {
        Asset asset = assetRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Asset not found with id: " + id));
        assetRepository.delete(asset);
    }

    @Override
    public PaginationResponse<AssetResponseDTO> getAllAssets(int page, int size, String sortBy, Boolean sortDirection) {
        Sort sort = (sortDirection != null && sortDirection)
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Asset> assetPage = assetRepository.findAll(pageable);

        List<AssetResponseDTO> items = assetPage.getContent()
                .stream()
                .map(asset -> AssetResponseDTO.builder()
                        .id(asset.getId())
                        .code(asset.getCode())
                        .name(asset.getName())
                        .notes(asset.getNotes())
                        .assignedUserName(asset.getAssignedUser() != null ? asset.getAssignedUser().getFullName() : null)
                        .assignedUserEmail(asset.getAssignedUser() != null ? asset.getAssignedUser().getEmail() : null)
                        .assignedUserPhone(asset.getAssignedUser() != null ? asset.getAssignedUser().getPhoneNumber() : null)
                        .build()
                ).toList();

        return new PaginationResponse<>(
                items,
                assetPage.getNumber(),
                assetPage.getSize(),
                assetPage.getTotalPages(),
                assetPage.getTotalElements()
        );
    }

}
