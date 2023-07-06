package com.example.demo.product;

import com.example.demo.brand.Brand;
import com.example.demo.brand.BrandService;
import com.example.demo.exception.ApiRequestException;
import com.example.demo.product.dto.CreateProductDto;
import com.example.demo.product.dto.ProductDto;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.util.List;

import org.modelmapper.TypeToken;


@Service
public class ProductService {
    private final ProductRepo productRepo;
    private final ModelMapper modelMapper;
    private final BrandService brandService;

    @Autowired
    ProductService(ProductRepo productRepo, ModelMapper modelMapper, BrandService brandService) {
        this.productRepo = productRepo;
        this.modelMapper = modelMapper;
        this.brandService = brandService;
    }

    ProductDto createProduct(CreateProductDto createProductDto) {
        Product newProduct = new Product();
        Long brandId = createProductDto.getBrandId();
        if (brandId != null && brandId != 0) {
            System.out.println(brandId);
            Brand brand = this.brandService.getBrand(createProductDto.getBrandId());
            newProduct.setBrand(brand);
        }
        newProduct.setTitle(createProductDto.getTitle());
        newProduct.setDescription(createProductDto.getDescription());
        newProduct.setQty(createProductDto.getQty());
        newProduct.setPrice(createProductDto.getPrice());
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

        product = this.productRepo.save(product);
        return modelMapper.map(product, ProductDto.class);
    }

    void deleteProduct(long id) {
        Product product = this.getProduct(id);
        productRepo.delete(product);
    }

    List<ProductDto> getProducts() {
        Type listType = new TypeToken<List<ProductDto>>() {
        }.getType();
        List<Product> products = this.productRepo.findAll();
        return modelMapper.map(products, listType);
    }

}
