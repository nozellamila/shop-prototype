package com.shopprototype.domain;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
public class Cart implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne
    private User user;

    @ManyToMany(mappedBy = "carts", cascade = CascadeType.ALL)
    private List<Product> products = new ArrayList<Product>();

    @OneToMany(mappedBy = "cart", cascade = CascadeType.REMOVE)
    private List<AuxProductCart> auxProductCarts = new ArrayList<AuxProductCart>();

    private Float totalPrice;
    private Integer totalQuantity;

    public Cart(){
        this.totalPrice = 0.0f;
        this.totalQuantity = 0;
    }
}
