package com.example.demo.product.dto;

import com.example.demo.brand.dto.BrandDto;
import com.example.demo.category.dto.CategoryDto;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ProductDto {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private long id;
    private String title;
    private String description;
    @Min(0)
    private int price;
    @Min(0)
    private int qty;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Long BrandId;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private BrandDto brand;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private List<Integer> addCategoryIds;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private List<Integer> deleteCategoryIds;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private List<CategoryDto> categoryList;
}
