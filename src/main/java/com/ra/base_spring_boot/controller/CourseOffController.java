package com.ra.base_spring_boot.controller;

import com.ra.base_spring_boot.dto.request.CourseOffRequest;
import com.ra.base_spring_boot.dto.response.APIResponse;
import com.ra.base_spring_boot.dto.response.CourseOffDTO;
import com.ra.base_spring_boot.dto.response.PaginationDTO;
import com.ra.base_spring_boot.dto.response.PaginationResponse;
import com.ra.base_spring_boot.model.CourseOff;
import com.ra.base_spring_boot.service.interfaces.CourseOffService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/course-offs")
public class CourseOffController {
    @Autowired
    private CourseOffService courseOffService;

    @GetMapping
    public ResponseEntity<APIResponse<PaginationResponse<CourseOffDTO>>> getAllCourseOffs(
            @RequestParam(defaultValue = "1") int page
    ) {
        Page<CourseOffDTO> courseOffDTOPage = courseOffService.getAllCourseOffs(page - 1, 5);

        PaginationDTO paginationDTO = new PaginationDTO(
                courseOffDTOPage.getNumber() + 1,
                courseOffDTOPage.getSize(),
                courseOffDTOPage.getTotalPages(),
                courseOffDTOPage.getTotalElements()
        );

        PaginationResponse<CourseOffDTO> responseData = new PaginationResponse<>(
                courseOffDTOPage.getContent(),
                paginationDTO
        );

        return new ResponseEntity<>(new APIResponse<>(
                true,
                "Lấy danh sách khóa học offline thành công!",
                responseData,
                HttpStatus.OK,
                LocalDateTime.now().toString()
        ), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<APIResponse<CourseOffDTO>> getCourseOffById(@PathVariable Long id) {
        return new ResponseEntity<>(new APIResponse<>(
                true,
                "Lấy chi tiết 1 khóa học thành công!",
                courseOffService.getCourseById(id),
                HttpStatus.OK,
                LocalDateTime.now().toString()
        ), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<APIResponse<CourseOffDTO>> addCourseOff(@Valid @RequestBody CourseOffRequest courseOffRequest){
        return new ResponseEntity<>(new APIResponse<>(
                true,
                "Thao tác thêm thành công!",
                courseOffService.addCourseOff(courseOffRequest),
                HttpStatus.CREATED,
                LocalDateTime.now().toString()
        ), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<APIResponse<CourseOffDTO>> updateCourseOff(@PathVariable Long id, @Valid @RequestBody CourseOffRequest courseOffRequest){
        return new ResponseEntity<>(new APIResponse<>(
                true,
                "Thao tác cập nhật thành công!",
                courseOffService.update(id, courseOffRequest),
                HttpStatus.OK,
                LocalDateTime.now().toString()
        ), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCourseOff(@PathVariable Long id){
        courseOffService.delete(id);
        return new ResponseEntity<>(new APIResponse<>(
                true,
                "Thao tác xóa thành công!",
                null,
                HttpStatus.OK,
                LocalDateTime.now().toString()
        ), HttpStatus.OK);
    }
}
