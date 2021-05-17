package com.shopprototype.forms;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Setter
@Getter
public class LoginForm {

    @NotBlank(message = "Email não deve ser nulo ou vazio")
    @Email(message = "Insira um email válido")
    private String email;
    @NotBlank(message = "Senha não deve ser nulo ou vazio")
    @Size(min =6, max = 10, message = "Senha deve ter 6 a 10 digitos")
    private String password;

    public UsernamePasswordAuthenticationToken convert(){
        return new UsernamePasswordAuthenticationToken(email, password);
    }
}
