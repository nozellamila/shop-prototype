package com.shopprototype.repositories;

import com.shopprototype.domain.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProductRepository extends JpaRepository<Product, Integer> {

    @Query("SELECT product FROM Product product WHERE" +
            "(:id IS NULL OR product.id = :id) AND" +
            "(:name IS NULL OR product.name = :name) AND" +
            "(:price IS NULL OR product.price = :price) AND" +
            "(:description IS NULL OR product.description = :description) AND" +
            "(:quantity IS NULL OR product.quantity = :quantity)")
    Page<Product> findByParameters(@Param("id") Integer id,
                                   @Param("name") String name,
                                   @Param("price") Float price,
                                   @Param("description") String description,
                                   @Param("quantity") Integer quantity,
                                   Pageable pageable);

    Product findByName(String name);
}
