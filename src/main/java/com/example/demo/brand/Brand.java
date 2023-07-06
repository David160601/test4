package com.example.demo.brand;


import com.example.demo.product.Product;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "brand")
public class Brand {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String imgUrl;

    @OneToMany(mappedBy = "brand", cascade = CascadeType.ALL)
    List<Product> products;
}
