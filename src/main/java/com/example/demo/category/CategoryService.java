package com.example.demo.category;

import com.example.demo.category.dto.CategoryDto;
import com.example.demo.category.dto.CreateCategoryDto;
import com.example.demo.exception.ApiRequestException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CategoryService {
    private final CategoryRepo categoryRepo;
    private final ModelMapper modelMapper;

    @Autowired
    public CategoryService(CategoryRepo categoryRepo, ModelMapper modelMapper) {
        this.categoryRepo = categoryRepo;
        this.modelMapper = modelMapper;
    }

    CategoryDto createCategory(CreateCategoryDto createCategoryDto) {
        Category category = new Category();
        category.setTitle(createCategoryDto.getTitle());
        category.setImgUrl(createCategoryDto.getImgUrl());
        return modelMapper.map(this.categoryRepo.save(category), CategoryDto.class);
    }

    public Category getCategory(long id) {
        Category category = this.categoryRepo.findById(id).orElseThrow(() -> new ApiRequestException("Category not found", HttpStatus.NOT_FOUND));
        return category;
    }

    CategoryDto getCategoryDto(long id) {
        return modelMapper.map(this.getCategory(id), CategoryDto.class);
    }

    void deleteCategory(long id) {
        Category category = this.getCategory(id);
        categoryRepo.delete(category);
    }

    CategoryDto updateCategory(long id, CategoryDto categoryDto) {
        Category category = this.getCategory(id);
        if (categoryDto.getTitle() != null) {
            category.setTitle(categoryDto.getTitle());
        }
        if (categoryDto.getImgUrl() != null) {
            category.setImgUrl(categoryDto.getImgUrl());
        }
        category = categoryRepo.save(category);
        return modelMapper.map(category, CategoryDto.class);
    }


    List<CategoryDto> getCategoryList() {
        List<Category> categoryList = this.categoryRepo.findAll();
        List<CategoryDto> categoryDtos = new ArrayList<>();
        for (Category category : categoryList) {
            categoryDtos.add(modelMapper.map(category, CategoryDto.class));
        }
        return categoryDtos;
    }
}
