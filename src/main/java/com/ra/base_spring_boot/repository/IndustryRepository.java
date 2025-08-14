package com.ra.base_spring_boot.repository;

import com.ra.base_spring_boot.model.Industry;
import org.springframework.data.jpa.repository.JpaRepository;
<<<<<<< HEAD

public interface IndustryRepository extends JpaRepository<Industry, Long> {
=======
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface IndustryRepository extends JpaRepository<Industry, Long> {
    @Query("SELECT CASE WHEN COUNT(i) > 0 THEN true ELSE false END FROM Industry i WHERE i.id = :id")
    boolean existsByIdCustom(@Param("id") Long id);
    @Query("SELECT i FROM Industry i WHERE i.id IN :industryIds")
    List<Industry> findAllById(Set<Long> industryIds);
>>>>>>> b09d1708296b754049d74cc987095ae7e7cb2c2a
}
