package com.example.demo.category;


import com.example.demo.category.dto.CategoryDto;
import com.example.demo.category.dto.CreateCategoryDto;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/category")
public class CategoryController {
    private CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping()
    CategoryDto createCategory(@RequestBody() @Valid CreateCategoryDto createCategoryDto) {
        return this.categoryService.createCategory(createCategoryDto);
    }

    @GetMapping("/{id}")
    CategoryDto getCategory(@PathVariable("id") long id) {
        return this.categoryService.getCategoryDto(id);
    }

    @DeleteMapping("/{id}")
    void deleteCategory(@PathVariable("id") long id) {
        this.categoryService.deleteCategory(id);
    }

    @PutMapping("/{id}")
    CategoryDto updateCategory(@PathVariable("id") long id, @RequestBody() CategoryDto categoryDto) {
        return this.categoryService.updateCategory(id, categoryDto);
    }

    @GetMapping("")
    List<CategoryDto> getCategoryList() {
        return this.categoryService.getCategoryList();
    }
}
