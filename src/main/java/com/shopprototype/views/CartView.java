package com.shopprototype.views;

import com.shopprototype.domain.Cart;
import com.shopprototype.domain.AuxProductCart;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
    private String creationDate;
    private Integer id;


    public CartView(Cart cart){
        String dataPattern = "dd-MM-yyyy HH:mm:ss";
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(dataPattern);
        this.products = cart.getAuxProductCarts();
        this.totalPrice = cart.getTotalPrice();
        this.totalQuantity = cart.getTotalQuantity();
        this.userId = cart.getUser().getId();
        this.creationDate = cart.getCreationDate().format(dateTimeFormatter);
        this.id = cart.getId();
    }
}
