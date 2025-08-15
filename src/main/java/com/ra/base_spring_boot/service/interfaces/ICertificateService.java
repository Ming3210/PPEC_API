package com.ra.base_spring_boot.service.interfaces;


import com.ra.base_spring_boot.dto.request.CertificateRequestDTO;
import com.ra.base_spring_boot.dto.request.CertificateSearchFilterDTO;
import com.ra.base_spring_boot.dto.response.CertificateResponseDTO;
import com.ra.base_spring_boot.dto.response.PaginationResponse;

public interface ICertificateService {

    CertificateResponseDTO createCertificate(CertificateRequestDTO certificateRequestDTO);

    CertificateResponseDTO getCertificateById(Long id);

    CertificateResponseDTO updateCertificate(Long id, CertificateRequestDTO certificateRequestDTO);

    void deleteCertificate(Long id);

    PaginationResponse<CertificateResponseDTO> searchAndFilterCertificates(CertificateSearchFilterDTO filterDTO);


    PaginationResponse<CertificateResponseDTO> getAllCertificates(int page, int size, String sortBy, String sortDirection);

    boolean canDeleteCertificate(Long id);
}
