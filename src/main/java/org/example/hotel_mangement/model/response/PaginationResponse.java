package org.example.hotel_mangement.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaginationResponse {
    private Long totalElements;
    private Integer currentPage;
    private Integer pageSize;
    private Integer totalPages;
    public static PaginationResponse fromPage(Page<?> pageData, int currentPage, int pageSize) {
        return PaginationResponse.builder()
                .currentPage(currentPage)
                .totalElements(pageData.getTotalElements())
                .pageSize(pageSize)
                .totalPages(pageData.getTotalPages())
                .build();
    }
    
    public static PaginationResponse empty(int currentPage, int pageSize) {
        return PaginationResponse.builder()
                .currentPage(currentPage)
                .totalElements(0L)
                .pageSize(pageSize)
                .totalPages(0)
                .build();
    }
}
