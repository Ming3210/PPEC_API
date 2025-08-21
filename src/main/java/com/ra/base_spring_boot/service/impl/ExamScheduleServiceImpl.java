package com.ra.base_spring_boot.service.impl;

import com.ra.base_spring_boot.dto.request.ExamScheduleRequest;
import com.ra.base_spring_boot.dto.response.ExamScheduleDTO;
import com.ra.base_spring_boot.exception.HttpConflict;
import com.ra.base_spring_boot.model.CourseOff;
import com.ra.base_spring_boot.model.Exam;
import com.ra.base_spring_boot.model.Partner;
import com.ra.base_spring_boot.repository.CourseOffRepository;
import com.ra.base_spring_boot.repository.ExamRepository;
import com.ra.base_spring_boot.repository.PartnerRepository;
import com.ra.base_spring_boot.service.interfaces.IExamScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class ExamScheduleServiceImpl implements IExamScheduleService {
    private final ExamRepository examRepository;
    private final PartnerRepository partnerRepository;
    private final CourseOffRepository courseOffRepository;

    @Override
    public ExamScheduleDTO addExamSchedule(Long courseId, ExamScheduleRequest examScheduleRequest) {
        CourseOff courseOff = getCourseOffById(courseId);

        Partner partner = getPartnerById(examScheduleRequest.getPartnerId());

        if (examRepository.existsByExamCode(examScheduleRequest.getExamCode())) {
            throw new HttpConflict("Mã kỳ thi đã tồn tại!");
        }

        Exam exam = Exam.builder()
                .courseOff(courseOff)
                .partner(partner)
                .title(examScheduleRequest.getTitle())
                .examCode(examScheduleRequest.getExamCode())
                .description(examScheduleRequest.getDescription())
                .examDate(examScheduleRequest.getExamDate())
                .status(examScheduleRequest.getStatus())
                .build();

        Exam saveExam = examRepository.save(exam);
        return convertToDTO(saveExam);
    }

    @Override
    public Exam getExamById(Long id) {
        return examRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Không tìm thấy lịch thi!"));
    }

    @Override
    public ExamScheduleDTO updateExamSchedule(Long id, Long courseId, ExamScheduleRequest examScheduleRequest) {
        Exam exam = getExamById(id);
        CourseOff courseOff = getCourseOffById(courseId);
        Partner partner = getPartnerById(examScheduleRequest.getPartnerId());

        exam.setCourseOff(courseOff);
        exam.setExamCode(examScheduleRequest.getExamCode());
        exam.setTitle(examScheduleRequest.getTitle());
        exam.setDescription(examScheduleRequest.getDescription());
        exam.setExamDate(examScheduleRequest.getExamDate());
        exam.setStatus(examScheduleRequest.getStatus());
        exam.setPartner(partner);

        Exam updatedExam = examRepository.save(exam);
        return convertToDTO(updatedExam);
    }

    @Override
    public void deleteExamById(Long courseId, Long examScheduleId) {
        Exam exam = getExamById(examScheduleId);

        if (!exam.getCourseOff().getId().equals(courseId)) {
            throw new IllegalArgumentException("Lịch thi không thuộc về Course có id = " + courseId);
        }

        examRepository.delete(exam);
    }

    public ExamScheduleDTO convertToDTO(Exam exam){
        CourseOff courseOff = getCourseOffById(exam.getCourseOff().getId());

        Partner partner = getPartnerById(exam.getPartner().getId());

        return ExamScheduleDTO.builder()
                .examCode(exam.getExamCode())
                .courseName(courseOff.getName())
                .partnerName(partner.getName())
                .title(exam.getTitle())
                .description(exam.getDescription())
                .examDate(exam.getExamDate())
                .status(exam.getStatus())
                .build();
    }

    public Partner getPartnerById(Long partnerId){
        return partnerRepository.findById(partnerId).orElseThrow(() -> new NoSuchElementException("Không tìm thấy đối tác!"));
    }

    public CourseOff getCourseOffById(Long courseId){
        return courseOffRepository.findById(courseId).orElseThrow(()-> new NoSuchElementException("Không tìm thấy khóa học!"));
    }
}
