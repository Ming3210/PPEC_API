package com.ra.base_spring_boot.dto.response;

import com.ra.base_spring_boot.dto.request.PaginationDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class PaginationResponse<T> {
    private List<T> items;
    private PaginationDTO pagination;

    public PaginationResponse(List<T> items, PaginationDTO pagination) {
        this.items = items;
        this.pagination = pagination;
    }

    public PaginationResponse(org.springframework.data.domain.Page<T> page) {
        this.items = page.getContent();
        this.pagination = new PaginationDTO(
                page.getNumber(),
                page.getSize(),
                page.getTotalPages(),
                page.getTotalElements()
        );
    }

    public PaginationResponse(List<T> items, int currentPage, int pageSize, int totalPages, long totalElements) {
        this.items = items;
        this.pagination = new PaginationDTO(currentPage, pageSize, totalPages, totalElements);
    }

    public PaginationResponse(List<T> items, int currentPage, int pageSize, long totalElements) {
        this.items = items;
        int totalPages = (int) Math.ceil((double) totalElements / pageSize);
        this.pagination = new PaginationDTO(currentPage, pageSize, totalPages, totalElements);
    }

    public static <T> PaginationResponse<T> of(org.springframework.data.domain.Page<T> page) {
        return new PaginationResponse<>(page);
    }

    public static <T> PaginationResponse<T> of(List<T> items, PaginationDTO pagination) {
        return new PaginationResponse<>(items, pagination);
    }

    public static <T> PaginationResponse<T> of(List<T> items, int currentPage, int pageSize, long totalElements) {
        return new PaginationResponse<>(items, currentPage, pageSize, totalElements);
    }

    public static <T> PaginationResponse<T> empty(int page, int size) {
        return new PaginationResponse<>(
                java.util.Collections.emptyList(),
                new PaginationDTO(page, size, 0, 0L)
        );
    }
}
