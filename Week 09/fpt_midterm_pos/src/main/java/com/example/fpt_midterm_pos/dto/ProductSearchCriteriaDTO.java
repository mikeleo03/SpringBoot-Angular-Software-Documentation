package com.example.fpt_midterm_pos.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductSearchCriteriaDTO {
    private String name;
    private String sortByName;
    private String sortByPrice;
    private Double minPrice;
    private Double maxPrice;
}


