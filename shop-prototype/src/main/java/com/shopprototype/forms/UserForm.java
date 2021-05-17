package com.shopprototype.forms;

import com.shopprototype.domain.Role;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class UserForm {
    @NotBlank(message = "Nome não deve ser nulo ou vazio")
    private String name;
    @Email(message = "Insira um email válido")
    @NotBlank(message = "Email não deve ser nulo ou vazio")
    private String email;
    @NotBlank(message = "Senha não deve ser nulo ou vazio")
    @Length(min = 6, max = 10, message = "Senha deve ter 6 a 10 digitos")
    private String password;
    private List<String> rolesNames = new ArrayList<>();
}
