package com.ra.base_spring_boot.service.interfaces;

import com.ra.base_spring_boot.dto.request.TeachingAssistantRequestDTO;
import com.ra.base_spring_boot.dto.response.TeachingAssistantResponseDTO;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ITeachingAssistantService {

    TeachingAssistantResponseDTO createTeachingAssistant(TeachingAssistantRequestDTO requestDTO);

    TeachingAssistantResponseDTO updateTeachingAssistant(Long id, TeachingAssistantRequestDTO requestDTO);

    void deleteTeachingAssistant(Long id);

    TeachingAssistantResponseDTO getTeachingAssistantById(Long id);

    Page<TeachingAssistantResponseDTO> getAllTeachingAssistants(String keyword, int page, int size);
}

