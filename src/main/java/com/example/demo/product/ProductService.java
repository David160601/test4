package com.example.demo.product;

import com.example.demo.brand.Brand;
import com.example.demo.brand.BrandService;
import com.example.demo.category.Category;
import com.example.demo.category.CategoryService;
import com.example.demo.exception.ApiRequestException;
import com.example.demo.product.dto.CreateProductDto;
import com.example.demo.product.dto.ProductDto;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;


@Service
public class ProductService {
    private final ProductRepo productRepo;
    private final ModelMapper modelMapper;
    private final BrandService brandService;
    private final CategoryService categoryService;

    @Autowired
    ProductService(ProductRepo productRepo, ModelMapper modelMapper, BrandService brandService, CategoryService categoryService) {
        this.productRepo = productRepo;
        this.modelMapper = modelMapper;
        this.brandService = brandService;
        this.categoryService = categoryService;
    }

    ProductDto createProduct(CreateProductDto createProductDto) {
        Product newProduct = new Product();
        Long brandId = createProductDto.getBrandId();
        if (brandId != null && brandId != 0) {
            Brand brand = this.brandService.getBrand(createProductDto.getBrandId());
            newProduct.setBrand(brand);
        }
        newProduct.setTitle(createProductDto.getTitle());
        newProduct.setDescription(createProductDto.getDescription());
        newProduct.setQty(createProductDto.getQty());
        newProduct.setPrice(createProductDto.getPrice());
        if (createProductDto.getCategoryIds() != null) {
            for (int i : createProductDto.getCategoryIds()) {
                Category category = this.categoryService.getCategory(i);
                newProduct.addCategory(category);
            }
        }
        newProduct = productRepo.save(newProduct);
        return modelMapper.map(newProduct, ProductDto.class);
    }

    ProductDto getProductDto(long id) {
        return modelMapper.map(this.getProduct(id), ProductDto.class);
    }

    Product getProduct(long id) {
        Product product = this.productRepo.findById(id).orElseThrow(() -> new ApiRequestException("Product not found", HttpStatus.NOT_FOUND));
        return product;
    }

    ProductDto updateProduct(long id, ProductDto productDto) {
        Product product = this.getProduct(id);
        if (productDto.getTitle() != null) {
            product.setTitle(productDto.getTitle());
        }
        if (productDto.getDescription() != null) {
            product.setDescription(productDto.getDescription());
        }
        if (productDto.getPrice() != product.getPrice()) {
            product.setPrice(productDto.getPrice());
        }
        if (productDto.getQty() != product.getQty()) {
            product.setQty(productDto.getQty());
        }
        if (productDto.getBrandId() != null) {
            Brand brand = this.brandService.getBrand(product.getId());
            product.setBrand(brand);
        }
        if (productDto.getAddCategoryIds() != null) {
            for (int i : productDto.getAddCategoryIds()) {
                Category category = this.categoryService.getCategory(i);
                product.addCategory(category);
            }
        }
        if (productDto.getDeleteCategoryIds() != null) {
            for (int i : productDto.getDeleteCategoryIds()) {
                Category category = this.categoryService.getCategory(i);
                product.removeCategory(category);
            }
        }
        product = this.productRepo.save(product);
        return modelMapper.map(product, ProductDto.class);
    }

    void deleteProduct(long id) {
        Product product = this.getProduct(id);
        productRepo.delete(product);
    }

    Page<ProductDto> getProducts(String brandId, String page, String limit, String title) {
        int pageNumber = page != null ? Integer.parseInt(page) : 1;
        int pageSize = limit != null ? Integer.parseInt(limit) : 10;
        Specification<Product> spec = Specification.where(null);
        if (brandId != null) {
            long parsedBrandId = Long.parseLong(brandId);
            spec = spec.and(hasBrandId(parsedBrandId));
        }
        if (title != null) {
            spec = spec.and(hasTitle(title));
        }
        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize);
        Page<Product> products = this.productRepo.findAll(spec, pageable);
        Page<ProductDto> productDTOs = products.map(product -> modelMapper.map(product, ProductDto.class));
        return productDTOs;
    }

    private static Specification<Product> hasBrandId(long brandId) {
        return (root, query, builder) -> builder.equal(root.get("brand").get("id"), brandId);
    }

    private static Specification<Product> hasTitle(String title) {
        return (root, query, builder) -> builder.like(root.get("title"), "%" + title.toLowerCase() + "%");
    }

}
