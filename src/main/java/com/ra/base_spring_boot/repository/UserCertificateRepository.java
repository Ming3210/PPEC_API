package com.ra.base_spring_boot.repository;


import com.ra.base_spring_boot.model.UserCertificate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserCertificateRepository extends JpaRepository<UserCertificate, Long> {

    long countByCertificateId(Long certificateId);

}
