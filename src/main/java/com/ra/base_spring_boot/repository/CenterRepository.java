package com.ra.base_spring_boot.repository;


import com.ra.base_spring_boot.model.Center;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CenterRepository extends JpaRepository<Center, Long> {

    boolean existsByName(String name);
    boolean existsByNameAndIdNot(String name, Long id);

    Page<Center> findByNameContainingIgnoreCase(String name, Pageable pageable);
    Page<Center> findByAddressContainingIgnoreCase(String address, Pageable pageable);
    Page<Center> findByNameContainingIgnoreCaseOrAddressContainingIgnoreCase(
            String name, String address, Pageable pageable);
}
