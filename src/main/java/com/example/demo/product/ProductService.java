package com.example.demo.product;

import com.example.demo.brand.Brand;
import com.example.demo.brand.BrandService;
import com.example.demo.brand.dto.BrandDto;
import com.example.demo.category.Category;
import com.example.demo.category.CategoryService;
import com.example.demo.category.dto.CategoryDto;
import com.example.demo.exception.ApiRequestException;
import com.example.demo.product.dto.CreateProductDto;
import com.example.demo.product.dto.ProductDto;
import jakarta.persistence.EntityManager;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class ProductService {
    private final ProductRepo productRepo;
    private final ModelMapper modelMapper;
    private final BrandService brandService;
    private final CategoryService categoryService;
    private final EntityManager entityManager;
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    ProductService(ProductRepo productRepo, ModelMapper modelMapper, BrandService brandService, CategoryService categoryService, NamedParameterJdbcTemplate namedParameterJdbcTemplate, EntityManager entityManager) {
        this.productRepo = productRepo;
        this.modelMapper = modelMapper;
        this.brandService = brandService;
        this.categoryService = categoryService;
        this.entityManager = entityManager;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
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
        int pageablelimit = limit == null ? 10 : Integer.parseInt(limit);
        int pageablePage = page == null ? 0 : Integer.parseInt(page) - 1;
        MapSqlParameterSource productParam = new MapSqlParameterSource()
                .addValue("TITLE", title == null ? "" : title)
                .addValue("BRAND_ID", brandId == null ? 0 : Integer.parseInt(brandId))
                .addValue("CATEGORY_ID", categoryId == null ? 0 : Integer.parseInt(categoryId))
                .addValue("LIMIT", pageablelimit)
                .addValue("OFFSET", pageablePage);
        String GET_PRODUCTS_SQL = "SELECT DISTINCT\n" +
                "P.id,\n" +
                "P.title,\n" +
                "P.description,\n" +
                "P.price,\n" +
                "P.qty,\n" +
                "B.id AS brand_id,\n" +
                "B.title AS brand_title,\n" +
                "B.img_url AS brand_imgUrl\n" +
                "FROM product AS P\n" +
                "INNER JOIN brand AS B\n" +
                "ON P.brand_id = B.id\n" +
                "INNER JOIN product_category_list AS PC\n" +
                "ON P.id = PC.product_id\n" +
                "WHERE LOWER(P.title) LIKE CONCAT('%', :TITLE, '%')\n" +
                "AND :BRAND_ID in (0,P.brand_id)\n" +
                "AND :CATEGORY_ID in (0,PC.category_id)\n";
        String COUNT_QUERY = "SELECT COUNT(*) FROM (" + GET_PRODUCTS_SQL + ") AS countQuery";
        String paginatedQuery = GET_PRODUCTS_SQL + " ORDER BY P.id LIMIT :LIMIT OFFSET :OFFSET";
        int totalCount = this.namedParameterJdbcTemplate.queryForObject(COUNT_QUERY, productParam, Integer.class);
        List<ProductDto> productDtos = this.namedParameterJdbcTemplate.query(paginatedQuery, productParam, (rs, rowNum) -> {
            ProductDto productDto = new ProductDto();
            productDto.setId(rs.getLong("id"));
            productDto.setTitle(rs.getString("title"));
            productDto.setDescription(rs.getString("description"));
            productDto.setPrice(rs.getInt("price"));
            productDto.setQty(rs.getInt("qty"));
            BrandDto brandDto = new BrandDto();
            brandDto.setId(rs.getLong("brand_id"));
            brandDto.setTitle(rs.getString("brand_title"));
            brandDto.setImgUrl(rs.getString("brand_imgUrl"));
            productDto.setBrand(brandDto);
            MapSqlParameterSource categoryParam = new MapSqlParameterSource()
                    .addValue("PRODUCT_ID", productDto.getId());
            String GET_CATEGORIES_SQL = "SELECT\n" +
                    "C.id,\n" +
                    "C.title,\n" +
                    "C.img_url\n" +
                    "FROM category as C\n" +
                    "INNER JOIN product_category_list as PC\n" +
                    "on C.id = PC.category_id\n" +
                    "WHERE PC.product_id = :PRODUCT_ID";
            List<CategoryDto> categoryDtos = this.namedParameterJdbcTemplate.query(GET_CATEGORIES_SQL, categoryParam, (rs1, rowNum2) -> {
                CategoryDto categoryDto = new CategoryDto();
                categoryDto.setId(rs1.getLong("id"));
                categoryDto.setImgUrl(rs1.getString("img_url"));
                categoryDto.setTitle(rs1.getString("title"));
                return categoryDto;
            });
            productDto.setCategoryList(categoryDtos);
            return productDto;
        });
        Pageable
                pageable = PageRequest.of(pageablePage * pageablelimit, pageablelimit);
        return new PageImpl<>(productDtos, pageable, totalCount);
    }

}
