package com.example.demo.brand;


import com.example.demo.brand.dto.BrandDto;
import com.example.demo.brand.dto.CreateBrandDto;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/brand")
public class BrandController {
    private final BrandService brandService;

    @Autowired
    public BrandController(BrandService brandService) {
        this.brandService = brandService;
    }

    @PostMapping()
    BrandDto createBrand(@RequestBody @Valid CreateBrandDto createBrandDto) {
        return this.brandService.createBrand(createBrandDto);
    }

    @GetMapping("/{id}")
    BrandDto getBrand(@PathVariable("id") long id) {
        return this.brandService.getBrandDto(id);
    }

    @DeleteMapping("/{id}")
    void deleteBrand(@PathVariable("id") long id) {
        this.brandService.deleteBrand(id);
    }

    @PutMapping("/{id}")
    BrandDto updateBrand(@PathVariable("id") long id, @RequestBody() @Valid BrandDto brandDto) {
        return this.brandService.updateBrand(id, brandDto);
    }
    @GetMapping()
    List<Brand> getBrands() {
        return this.brandService.getBrands();
    }
}
