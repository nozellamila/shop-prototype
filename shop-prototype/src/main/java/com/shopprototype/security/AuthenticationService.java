package com.shopprototype.security;

import com.shopprototype.domain.User;
import com.shopprototype.repositories.UserRepository;
import com.shopprototype.services.exceptions.ObjectNotFoundException;
import com.shopprototype.views.UserView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthenticationService implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByEmail(email);

        if(user.isPresent()){
            return user.get();
        }else {
            throw new ObjectNotFoundException("Dados inválidos");
        }
    }
}
