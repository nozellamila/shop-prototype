package com.shopprototype.domain;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
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
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime creationDate;

    public Cart(){
        this.totalPrice = 0.0f;
        this.totalQuantity = 0;
    }
}
