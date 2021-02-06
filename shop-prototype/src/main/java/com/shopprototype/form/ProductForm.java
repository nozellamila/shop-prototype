package com.shopprototype.form;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductForm {

    @NotBlank(message = "Nome não pode estar em branco")
    private String name;
    @NotBlank(message = "Preço não pode estar em branco")
    private String price;
    private String description;
    @NotNull
    private Integer quantity;
}
