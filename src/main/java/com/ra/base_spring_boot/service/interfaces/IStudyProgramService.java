package com.ra.base_spring_boot.service.interfaces;

import com.ra.base_spring_boot.dto.request.StudyProgramRequest;
import com.ra.base_spring_boot.dto.response.StudyProgramResponseDTO;
import com.ra.base_spring_boot.model.StudyProgram;

public interface IStudyProgramService {
    StudyProgramResponseDTO addStudyProgram(Long courseId, StudyProgramRequest studyProgramRequest);

    StudyProgram getStudyProgramById(Long id);

    StudyProgramResponseDTO updateStudyProgram(Long id, Long courseId, StudyProgramRequest studyProgramRequest);

    void deleteStudyProgram(Long courseId, Long id);
}
