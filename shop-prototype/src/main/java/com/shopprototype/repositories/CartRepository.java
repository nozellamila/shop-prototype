package com.shopprototype.repositories;

import com.shopprototype.domain.Cart;
import com.shopprototype.domain.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CartRepository extends JpaRepository<Cart, Integer> {

    @Query("SELECT cart FROM Cart cart WHERE" +
            "(:id IS NULL OR cart.id = :id) AND" +
            "(:totalPrice IS NULL OR cart.totalPrice = :totalPrice) AND" +
            "(:totalQuantity IS NULL OR cart.totalQuantity = :totalQuantity) AND" +
            "(:userId IS NULL OR cart.user.id = :userId)")
    Page<Cart> findByParameters(@Param("id") Integer id,
                                @Param("totalPrice") Float totalPrice,
                                @Param("totalQuantity") Integer totalQuantity,
                                @Param("userId") Integer userId,
                                Pageable pageable);

    @Query("SELECT cart FROM Cart cart WHERE" +
            "(cart.user.id = :userId)")
    Cart findByUserId(@Param("userId") Integer id);
}
