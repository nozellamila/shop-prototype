package com.shopprototype.form;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class UserForm {
    @NotBlank(message = "{name.not.blank}")
    private String name;
    @Email(message = "{email.not.valid}")
    @NotBlank(message = "{email.not.blank}")
    private String email;
    @Length(min = 6, max = 6, message = "Senha deve ter seis caracteres")
    @NotBlank(message = "{senha.not.blank}")
    private String password;
    @NotNull(message = "Admin deve ser true ou false")
    private Boolean admin;
}
