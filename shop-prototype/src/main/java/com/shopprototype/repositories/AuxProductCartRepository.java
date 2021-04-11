package com.shopprototype.repositories;

import com.shopprototype.domain.AuxProductCart;
import com.shopprototype.domain.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuxProductCartRepository extends JpaRepository<AuxProductCart, Integer> {
}
