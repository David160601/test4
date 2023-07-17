package com.example.demo.brand;


import com.example.demo.brand.dto.BrandDto;
import com.example.demo.brand.dto.CreateBrandDto;
import com.example.demo.validation.ImageValidator;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping(path = "/brand")
public class BrandController {
    private final BrandService brandService;

    @Autowired
    public BrandController(BrandService brandService) {
        this.brandService = brandService;
    }

    //    @PostMapping()
//    BrandDto createBrand(@RequestBody @Valid CreateBrandDto createBrandDto) {
//        return this.brandService.createBrand(createBrandDto);
//    }
    @PostMapping()
    void createBrand(@RequestBody @Valid CreateBrandDto createBrandDto, @RequestParam("image") MultipartFile img) {
        boolean isImage= ImageValidator.validateImage(img);
        System.out.println(isImage);
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
    List<BrandDto> getBrands() {
        return this.brandService.getBrands();
    }
}
