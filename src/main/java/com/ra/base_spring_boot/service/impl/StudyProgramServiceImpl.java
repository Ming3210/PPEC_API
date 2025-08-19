package com.ra.base_spring_boot.service.impl;

import com.ra.base_spring_boot.dto.request.StudyProgramRequest;
import com.ra.base_spring_boot.dto.response.StudyProgramResponseDTO;
import com.ra.base_spring_boot.exception.HttpConflict;
import com.ra.base_spring_boot.model.CourseOff;
import com.ra.base_spring_boot.model.StudyProgram;
import com.ra.base_spring_boot.repository.CourseOffRepository;
import com.ra.base_spring_boot.repository.StudyProgramRepository;
import com.ra.base_spring_boot.service.interfaces.IStudyProgramService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class StudyProgramServiceImpl implements IStudyProgramService {
    @Autowired
    private StudyProgramRepository studyProgramRepository;

    @Autowired
    private CourseOffRepository courseOffRepository;

    @Override
    public StudyProgramResponseDTO addStudyProgram(Long courseId, StudyProgramRequest studyProgramRequest) {
        CourseOff courseOff = courseOffRepository.findById(courseId)
                .orElseThrow(() -> new NoSuchElementException("Không tìm thấy khóa học!"));

        StudyProgram studyProgram = StudyProgram.builder()
                .courseOff(courseOff)
                .headerName(studyProgramRequest.getHeaderName())
                .descriptions(studyProgramRequest.getDescriptions())
                .build();

        StudyProgram saveStudyProgram = studyProgramRepository.save(studyProgram);
        return convertToDTO(saveStudyProgram);
    }

    @Override
    public StudyProgram getStudyProgramById(Long id) {
        return studyProgramRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Không tìm thấy chương trình học!"));
    }

    @Override
    public StudyProgramResponseDTO updateStudyProgram(Long id, Long courseId, StudyProgramRequest studyProgramRequest) {
        StudyProgram studyProgram = getStudyProgramById(id);

        CourseOff courseOff = courseOffRepository.findById(courseId)
                .orElseThrow(() -> new NoSuchElementException("Không tìm thấy khóa học!"));

        studyProgram.setCourseOff(courseOff);
        studyProgram.setHeaderName(studyProgramRequest.getHeaderName());
        studyProgram.setDescriptions(studyProgramRequest.getDescriptions());

        StudyProgram updatedStudyProgram = studyProgramRepository.save(studyProgram);

        return convertToDTO(updatedStudyProgram);
    }

    @Override
    public void deleteStudyProgram(Long courseId, Long id) {
        StudyProgram studyProgram = studyProgramRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Không tìm thấy chương trình/yêu cầu đầu vào với id = " + id));

        if (!studyProgram.getCourseOff().getId().equals(courseId)) {
            throw new IllegalArgumentException("Chương trình/yêu cầu đầu vào không thuộc về Course có id = " + courseId);
        }

        studyProgramRepository.delete(studyProgram);
    }

    public StudyProgramResponseDTO convertToDTO(StudyProgram studyProgram) {
        return new StudyProgramResponseDTO(
                studyProgram.getHeaderName(),
                studyProgram.getDescriptions()
        );
    }
}
