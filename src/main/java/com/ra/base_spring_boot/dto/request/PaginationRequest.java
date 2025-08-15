package com.ra.base_spring_boot.dto.request;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class PaginationRequest {
    private int currentPage;
    private int pageSize;
    private int totalPages;
    private long totalItems;
}
