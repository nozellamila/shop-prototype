package com.shopprototype.resources;

import com.shopprototype.forms.LoginForm;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

@RestController
@RequestMapping("/auth")
public class AuthenticationResource {

    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping
    public ResponseEntity<?> auth(@RequestBody @Valid LoginForm loginForm){
        UsernamePasswordAuthenticationToken loginData = loginForm.convert();

        try {
            Authentication authentication = authenticationManager.authenticate(loginData);
            return ResponseEntity.ok().build();
        }catch (AuthenticationException e){
            return ResponseEntity.badRequest().build();
        }
    }
}
