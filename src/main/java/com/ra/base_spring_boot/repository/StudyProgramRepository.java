package com.ra.base_spring_boot.repository;

import com.ra.base_spring_boot.model.StudyProgram;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudyProgramRepository extends JpaRepository<StudyProgram, Long> {
    void deleteByCourseOff_Id(Long courseId);
}
