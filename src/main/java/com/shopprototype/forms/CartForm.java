package com.shopprototype.forms;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CartForm {

    @NotNull(message = "Id do usuário não deve ser nulo ou vazio")
    private Integer userId;
    private List<ProductCartForm> products = new ArrayList<ProductCartForm>();
}
