package com.shopprototype.forms;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductForm {

    @NotBlank(message = "Nome não pode estar em branco")
    private String name;
    @NotNull(message = "Preço não pode estar em branco")
    private Float price;
    private String description;
    @NotNull
    private Integer quantity;
}
