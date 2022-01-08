package com.shopprototype.repositories;

import com.shopprototype.domain.AuxProductCart;
import com.shopprototype.domain.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface AuxProductCartRepository extends JpaRepository<AuxProductCart, Integer> {

}
