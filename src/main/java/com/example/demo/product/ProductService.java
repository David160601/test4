package com.example.demo.product;

import com.example.demo.brand.Brand;
import com.example.demo.brand.BrandService;
import com.example.demo.category.Category;
import com.example.demo.category.CategoryService;
import com.example.demo.exception.ApiRequestException;
import com.example.demo.product.dto.CreateProductDto;
import com.example.demo.product.dto.ProductDto;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class ProductService {
    private final ProductRepo productRepo;
    private final ModelMapper modelMapper;
    private final BrandService brandService;
    private final CategoryService categoryService;
    private final EntityManager entityManager;

    @Autowired
    ProductService(ProductRepo productRepo, ModelMapper modelMapper, BrandService brandService, CategoryService categoryService, JdbcTemplate jdbcTemplate, EntityManager entityManager) {
        this.productRepo = productRepo;
        this.modelMapper = modelMapper;
        this.brandService = brandService;
        this.categoryService = categoryService;
        this.entityManager = entityManager;
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

    public Page<ProductDto> findByTitleAndBrandIdAndCategoryId(String title, String brandId, String categoryId, String page, String limit) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);
        QProduct qProduct = QProduct.product;
        BooleanExpression predicate = null;
        int newPage = page == null ? 1 : Integer.parseInt(page);
        int newLimit = limit == null ? 10 : Integer.parseInt(limit);
        if (title != null) {
            predicate = qProduct.title.eq(title);
        }
        if (brandId != null && !brandId.isEmpty()) {
            Brand brand = entityManager.find(Brand.class, Long.parseLong(brandId));
            if (brand == null) {
               throw new ApiRequestException("Brand not found",HttpStatus.NOT_FOUND);
            }
            predicate = predicate.and(qProduct.brand.eq(brand));
        }
        if (categoryId != null && !categoryId.isEmpty()) {
            Category category = entityManager.find(Category.class, Long.parseLong(categoryId));
            if (category == null) {
                throw new ApiRequestException("Category not found",HttpStatus.NOT_FOUND);
            }
            predicate = predicate.and(qProduct.categoryList.contains(category));
        }
        long total = queryFactory.selectFrom(qProduct)
                .where(predicate)
                .fetchCount();
        List<Product> products = queryFactory.selectFrom(qProduct)
                .where(predicate).offset((long) (newPage - 1) * newLimit).limit(newLimit)
                .fetch();

        List<ProductDto> productDtos = new ArrayList<>();
        for (Product item : products) {
            productDtos.add(modelMapper.map(item, ProductDto.class));

        }
        return new PageImpl<ProductDto>(productDtos, PageRequest.of(newPage - 1, newLimit), total);
    }


}
