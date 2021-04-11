package com.shopprototype.views;

import com.shopprototype.domain.Cart;
import com.shopprototype.domain.AuxProductCart;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CartView {

    private List<AuxProductCart> products;
    private Float totalPrice;
    private Integer totalQuantity;
    private Integer userId;
    private Integer id;

    public CartView(Cart cart){
        this.products = cart.getAuxProductCarts();
        this.totalPrice = cart.getTotalPrice();
        this.totalQuantity = cart.getTotalQuantity();
        this.userId = cart.getUser().getId();
        this.id = cart.getId();
    }
}
