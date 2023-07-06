package com.example.demo.product;

import com.example.demo.product.dto.CreateProductDto;
import com.example.demo.product.dto.ProductDto;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/product")
public class ProductController {
    private final ProductService productService;

    @Autowired
    ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping()
    ProductDto createProduct(@RequestBody() @Valid CreateProductDto createProductDto) {
        return this.productService.createProduct(createProductDto);
    }

    @GetMapping("/{id}")
    ProductDto getProductDto(@PathVariable("id") long id) {
        return this.productService.getProductDto(id);
    }

    @GetMapping("")
    List<ProductDto> getProducts() {
        return this.productService.getProducts();
    }

    @DeleteMapping("/{id}")
    void deleteProduct(@PathVariable("id") long id) {
        this.productService.deleteProduct(id);
    }

    @PutMapping("/{id}")
    ProductDto updateProduct(@PathVariable("id") long id, @RequestBody() ProductDto productDto) {
        return this.productService.updateProduct(id, productDto);
    }
}
