package com.ra.base_spring_boot.service.impl;

import com.ra.base_spring_boot.dto.request.AssetRequestDTO;
import com.ra.base_spring_boot.dto.request.StudentAssetRequestDTO;
import com.ra.base_spring_boot.dto.response.AssetResponseDTO;
import com.ra.base_spring_boot.dto.response.PaginationResponse;
import com.ra.base_spring_boot.exception.HttpForbiden;
import com.ra.base_spring_boot.exception.HttpNotFound;
import com.ra.base_spring_boot.model.Asset;
import com.ra.base_spring_boot.model.ServiceStaff;
import com.ra.base_spring_boot.model.User;
import com.ra.base_spring_boot.repository.AssetRepository;
import com.ra.base_spring_boot.repository.ServiceStaffRepository;
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
import java.util.NoSuchElementException;

@Service
public class AssetServiceImpl implements IAssetService {
    @Autowired
    private AssetRepository assetRepository;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ServiceStaffRepository serviceStaffRepository;


    @Override
    public AssetResponseDTO createAsset(AssetRequestDTO assetRequestDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        User creator = userRepository.findByUsername(username)
                .orElseThrow(() -> new HttpNotFound("User not found with username: " + username));

        Asset asset = new Asset();
        asset.setCode(assetRequestDTO.getCode());
        asset.setName(assetRequestDTO.getName());
        asset.setNotes(assetRequestDTO.getNotes());
        asset.setCreatedAt(LocalDate.now());
        asset.setUpdatedAt(LocalDate.now());
        asset.setCreator(creator);
        asset.setUpdater(creator);

        ServiceStaff serviceStaff = serviceStaffRepository.findById(assetRequestDTO.getAssignedServiceStaffId())
                .orElseThrow(() -> new HttpNotFound(
                        "Service Staff not found with id: " + assetRequestDTO.getAssignedServiceStaffId()
                ));

        User assignedUser = serviceStaff.getUser();
        if (assignedUser == null) {
            throw new RuntimeException("Assigned ServiceStaff has no associated User");
        }

        asset.setAssignedUser(assignedUser);
        Asset savedAsset = assetRepository.save(asset);

        return AssetResponseDTO.builder()
                .id(savedAsset.getId())
                .code(savedAsset.getCode())
                .name(savedAsset.getName())
                .notes(savedAsset.getNotes())
                .assignedUserId(assignedUser.getId())
                .assignedUserName(assignedUser.getFullName())
                .assignedUserEmail(assignedUser.getEmail())
                .assignedUserPhone(assignedUser.getPhoneNumber())
                .build();
    }


    @Override
    public AssetResponseDTO updateAsset(Long id, AssetRequestDTO assetRequestDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User creator = userRepository.findByUsername(username)
                .orElseThrow(() -> new HttpNotFound("User not found with username: " + username));

        Asset asset = assetRepository.findById(id)
                .orElseThrow(() -> new HttpNotFound("Asset not found with id: " + id));

        asset.setCode(assetRequestDTO.getCode());
        asset.setName(assetRequestDTO.getName());
        asset.setNotes(assetRequestDTO.getNotes());

        User assignedUser = userRepository.findById(assetRequestDTO.getAssignedServiceStaffId())
                .orElseThrow(() -> new HttpNotFound("User not found with id: " + assetRequestDTO.getAssignedServiceStaffId()));
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
                .orElseThrow(() -> new HttpNotFound("Asset not found with id: " + id));
        assetRepository.delete(asset);
    }

    @Override
    public PaginationResponse<AssetResponseDTO> getAllAssets(
            int page, int size, String sortBy, Boolean sortDirection, String keyword) {

        Sort sort = (sortDirection != null && sortDirection)
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Asset> assetPage;
        if (keyword != null && !keyword.isBlank()) {
            assetPage = assetRepository.findByCodeContainingIgnoreCaseOrNameContainingIgnoreCase(keyword, keyword, pageable);
        } else {
            assetPage = assetRepository.findAll(pageable);
        }

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

    @Override
    public AssetResponseDTO getAssetById(Long id) {
        Asset asset = assetRepository.findById(id)
                .orElseThrow(() -> new HttpNotFound("Asset not found with id: " + id));
        return AssetResponseDTO.builder()
                .id(asset.getId())
                .code(asset.getCode())
                .name(asset.getName())
                .notes(asset.getNotes())
                .assignedUserId(asset.getAssignedUser() != null ? asset.getAssignedUser().getId() : null)
                .assignedUserName(asset.getAssignedUser() != null ? asset.getAssignedUser().getFullName() : null)
                .assignedUserEmail(asset.getAssignedUser() != null ? asset.getAssignedUser().getEmail() : null)
                .assignedUserPhone(asset.getAssignedUser() != null ? asset.getAssignedUser().getPhoneNumber() : null)
                .build();
    }

    @Override
    public AssetResponseDTO studentUploadAsset(StudentAssetRequestDTO studentAssetRequestDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User creator = userRepository.findByUsername(username)
                .orElseThrow(() -> new HttpNotFound("User not found with username: " + username));
        Asset asset = new Asset();
        asset.setCode(studentAssetRequestDTO.getCode());
        asset.setName(studentAssetRequestDTO.getName());
        asset.setNotes(studentAssetRequestDTO.getNotes());
        asset.setAssignedUser(creator);
        asset.setCreatedAt(LocalDate.now());
        asset.setCreator(creator);
        asset.setUpdatedAt(null);
        asset.setUpdater(null);
        Asset savedAsset = assetRepository.save(asset);
        return AssetResponseDTO.builder()
                .id(savedAsset.getId())
                .code(savedAsset.getCode())
                .name(savedAsset.getName())
                .notes(savedAsset.getNotes())
                .assignedUserId(creator.getId())
                .assignedUserName(creator.getFullName())
                .assignedUserEmail(creator.getEmail())
                .assignedUserPhone(creator.getPhoneNumber())
                .build();
    }

    @Override
    public AssetResponseDTO studentUpdateAsset(Long id, StudentAssetRequestDTO studentAssetRequestDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User creator = userRepository.findByUsername(username)
                .orElseThrow(() -> new HttpNotFound("User not found with username: " + username));
        Asset asset = assetRepository.findById(id)
                .orElseThrow(() -> new HttpNotFound("Asset not found with id: " + id));
        if (asset.getAssignedUser() != null && !asset.getAssignedUser().getId().equals(creator.getId())) {
            throw new HttpForbiden("You are not authorized to update this asset");
        }
        asset.setCode(studentAssetRequestDTO.getCode());
        asset.setName(studentAssetRequestDTO.getName());
        asset.setNotes(studentAssetRequestDTO.getNotes());
        asset.setAssignedUser(creator);
        asset.setUpdatedAt(LocalDate.now());
        asset.setUpdater(creator);
        Asset updatedAsset = assetRepository.save(asset);
        return AssetResponseDTO.builder()
                .id(updatedAsset.getId())
                .code(updatedAsset.getCode())
                .name(updatedAsset.getName())
                .notes(updatedAsset.getNotes())
                .assignedUserId(updatedAsset.getAssignedUser() != null ? updatedAsset.getAssignedUser().getId() : null)
                .assignedUserName(updatedAsset.getAssignedUser() != null ? updatedAsset.getAssignedUser().getFullName() : null)
                .assignedUserEmail(updatedAsset.getAssignedUser() != null ? updatedAsset.getAssignedUser().getEmail() : null)
                .assignedUserPhone(updatedAsset.getAssignedUser() != null ? updatedAsset.getAssignedUser().getPhoneNumber() : null)
                .build();
    }

    @Override
    public PaginationResponse<AssetResponseDTO> getAllLoginUserAssets(int page, int size, String sortBy, Boolean sortDirection) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new HttpNotFound("User not found with username: " + username));
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy).descending());
        Page<Asset> assetPage = assetRepository.findByAssignedUser(user, pageable);
        List<AssetResponseDTO> items = assetPage.getContent()
                .stream()
                .map(asset -> AssetResponseDTO.builder()
                        .id(asset.getId())
                        .code(asset.getCode())
                        .name(asset.getName())
                        .notes(asset.getNotes())
                        .assignedUserId(asset.getAssignedUser() != null ? asset.getAssignedUser().getId() : null)
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
