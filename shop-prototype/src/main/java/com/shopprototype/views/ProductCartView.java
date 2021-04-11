package com.shopprototype.views;

import com.shopprototype.domain.Product;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductCartView {

    private Integer id;
    private Float price;
    private Integer quantity;

    public ProductCartView(Product product){
        this.id = product.getId();
        this.price = product.getPrice();
        this.quantity = product.getQuantity();
    }
}
