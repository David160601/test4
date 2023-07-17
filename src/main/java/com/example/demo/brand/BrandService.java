package com.example.demo.brand;

import com.example.demo.brand.dto.BrandDto;
import com.example.demo.brand.dto.CreateBrandDto;
import com.example.demo.exception.ApiRequestException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BrandService {
    private final BrandRepo brandRepo;
    private final ModelMapper modelMapper;

    @Autowired
    public BrandService(BrandRepo brandRepo, ModelMapper modelMapper) {
        this.brandRepo = brandRepo;
        this.modelMapper = modelMapper;
    }

    BrandDto createBrand(CreateBrandDto createBrandDto) {
        Brand newBrand = new Brand();
        newBrand.setTitle(createBrandDto.getTitle());
        newBrand.setImgUrl("testing");
        newBrand = brandRepo.save(newBrand);
        return modelMapper.map(newBrand, BrandDto.class);
    }

    BrandDto getBrandDto(long id) {
        return modelMapper.map(this.getBrand(id), BrandDto.class);
    }

    public Brand getBrand(long id) {
        return this.brandRepo.findById(id).orElseThrow(() -> new ApiRequestException("Brand not found", HttpStatus.NOT_FOUND));
    }

    void deleteBrand(long id) {
        try {
            Brand deleteBrand = this.getBrand(id);
            brandRepo.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new ApiRequestException(e.getMessage(), HttpStatus.CONFLICT);
        }

    }

    BrandDto updateBrand(long id, BrandDto brandDto) {
        Brand updateBrand = this.getBrand(id);
        if (brandDto.getTitle() != null) {
            updateBrand.setTitle(brandDto.getTitle());
        }
        if (brandDto.getImgUrl() != null) {
            updateBrand.setImgUrl(brandDto.getImgUrl());
        }
        updateBrand = brandRepo.save(updateBrand);
        return modelMapper.map(updateBrand, BrandDto.class);
    }

    List<BrandDto> getBrands() {
        List<Brand> brands = this.brandRepo.findAll();
        List<BrandDto> brandDtos = new ArrayList<>();
        for (Brand brand : brands) {
            BrandDto brandDto = modelMapper.map(brand, BrandDto.class);
            brandDtos.add(brandDto);
        }
        return brandDtos;
    }
}
