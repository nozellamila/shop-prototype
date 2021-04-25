package com.shopprototype.forms;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

@Setter
@Getter
public class LoginForm {

    @NotNull
    private String email;
    @NotNull
    private String pwd;

    public UsernamePasswordAuthenticationToken convert(){
        return new UsernamePasswordAuthenticationToken(email, pwd);
    }
}
