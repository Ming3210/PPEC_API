package com.ra.base_spring_boot.repository;

import com.ra.base_spring_boot.model.Certificate;
import com.ra.base_spring_boot.model.constants.CertificateStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;


@Repository
public interface CertificateRepository extends JpaRepository<Certificate, Long>, JpaSpecificationExecutor<Certificate> {

    boolean existsByCode(String code);
    boolean existsByCodeAndIdNot(String code, Long id);

    Page<Certificate> findByStatus(CertificateStatus status, Pageable pageable);

    Page<Certificate> findByNameContainingIgnoreCase(String name, Pageable pageable);

    Page<Certificate> findByCodeContainingIgnoreCase(String code, Pageable pageable);
}
