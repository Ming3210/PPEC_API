package com.ra.base_spring_boot.service.interfaces;


import com.ra.base_spring_boot.dto.request.SkillRequestDTO;
import com.ra.base_spring_boot.dto.request.SkillSearchFilterDTO;
import com.ra.base_spring_boot.dto.response.SkillResponseDTO;
import com.ra.base_spring_boot.dto.response.PaginationResponse;

import java.util.List;

public interface ISkillService {
    SkillResponseDTO createSkill(SkillRequestDTO skillRequestDTO);
    SkillResponseDTO getSkillById(Long id);
    SkillResponseDTO updateSkill(Long id, SkillRequestDTO skillRequestDTO);
    void deleteSkill(Long id);
    PaginationResponse<SkillResponseDTO> searchAndFilterSkills(SkillSearchFilterDTO filterDTO);
    PaginationResponse<SkillResponseDTO> getAllSkills(int page, int size, String sortBy, String sortDirection);
    boolean canDeleteSkill(Long id);
    List<SkillResponseDTO> getAllSkillsForSelect();
}
