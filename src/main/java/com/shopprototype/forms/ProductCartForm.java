package com.shopprototype.forms;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductCartForm {

    @NotNull
    private Integer productId;
    @NotNull
    private Integer quantity;

}
