package com.example.demo.product;


import com.example.demo.brand.Brand;
import com.example.demo.category.Category;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "product")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private int price;

    @Column(nullable = false)
    private int qty;

    @ManyToOne(fetch = FetchType.EAGER)
    private Brand brand;

    @ManyToMany()
    @JoinTable(joinColumns = @JoinColumn(name = "product_id"), inverseJoinColumns = @JoinColumn(name = "category_id"))
    private List<Category> categoryList = new ArrayList<>();

    public void addCategory(Category category) {
        if (!categoryList.contains(category)) {
            this.categoryList.add(category);
        }
    }

    public void removeCategory(Category category) {
        if (categoryList.contains(category)) {
            this.categoryList.remove(category);
        }
    }
}
