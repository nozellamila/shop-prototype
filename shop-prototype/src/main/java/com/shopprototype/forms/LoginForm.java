package com.shopprototype.forms;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import javax.validation.constraints.NotNull;

@Setter
@Getter
public class LoginForm {

    @NotNull
    private String email;
    @NotNull
    private String password;

    public UsernamePasswordAuthenticationToken convert(){
        return new UsernamePasswordAuthenticationToken(email, password);
    }
}
