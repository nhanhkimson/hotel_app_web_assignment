package org.example.hotel_mangement.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PayloadResponse<T> {
    private List<T> items;
    private PaginationResponse pagination;
}