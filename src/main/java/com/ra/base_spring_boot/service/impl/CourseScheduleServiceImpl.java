package com.ra.base_spring_boot.service.impl;

import com.ra.base_spring_boot.dto.request.CourseScheduleRequest;
import com.ra.base_spring_boot.dto.response.CourseScheduleResponse;
import com.ra.base_spring_boot.model.CourseOff;
import com.ra.base_spring_boot.model.CourseSchedule;
import com.ra.base_spring_boot.repository.CourseOffRepository;
import com.ra.base_spring_boot.repository.CourseScheduleRepository;
import com.ra.base_spring_boot.service.interfaces.ICourseScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CourseScheduleServiceImpl implements ICourseScheduleService {

    private final CourseScheduleRepository courseScheduleRepository;
    private final CourseOffRepository courseOffRepository;

    @Override
    public CourseScheduleResponse addCourseSchedule(CourseScheduleRequest requestDTO) {
        CourseOff courseOff = courseOffRepository.findById(requestDTO.getCourseOffId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy khóa học với ID: " + requestDTO.getCourseOffId()));

        CourseSchedule schedule = new CourseSchedule();
        schedule.setCourseOff(courseOff);
        schedule.setSessionDate(requestDTO.getSessionDate());
        schedule.setStartTime(requestDTO.getStartTime());
        schedule.setEndTime(requestDTO.getEndTime());
        schedule.setRoom(requestDTO.getRoom());

        CourseSchedule saved = courseScheduleRepository.save(schedule);

        return CourseScheduleResponse.builder()
                .id(saved.getId())
                .courseOffId(courseOff.getId())
                .courseOffName(courseOff.getName())
                .sessionDate(saved.getSessionDate())
                .startTime(saved.getStartTime())
                .endTime(saved.getEndTime())
                .room(saved.getRoom())
                .build();
    }

    @Override
    public List<CourseScheduleResponse> getSchedulesByCourse(Long courseOffId) {
        return courseScheduleRepository.findByCourseOffId(courseOffId)
                .stream()
                .map(s -> CourseScheduleResponse.builder()
                        .id(s.getId())
                        .courseOffId(s.getCourseOff().getId())
                        .courseOffName(s.getCourseOff().getName())
                        .sessionDate(s.getSessionDate())
                        .startTime(s.getStartTime())
                        .endTime(s.getEndTime())
                        .room(s.getRoom())
                        .build())
                .collect(Collectors.toList());
    }
}
