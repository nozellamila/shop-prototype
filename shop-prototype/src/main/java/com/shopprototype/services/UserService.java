package com.shopprototype.services;

import com.shopprototype.domain.User;
import com.shopprototype.repositories.UserRepository;
import com.shopprototype.services.exceptions.ObjectNotFoundException;
import com.shopprototype.views.UserView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.Pageable;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    public ResponseEntity<Page<UserView>> getUser(Integer id, String name, String email, Boolean admin, Pageable pageable){

        Page<User> users = userRepository.findByParameters(id, name, email, admin, pageable);

        if(users != null && !users.isEmpty()){
            return new ResponseEntity<Page<UserView>>(users.map(UserView::new), HttpStatus.OK);
        }else {
            throw new ObjectNotFoundException("Usuário não encontrado");
        }
    }


    public User find(Integer id) {
        Optional<User> obj = userRepository.findById(id);
        return obj.orElseThrow(() -> new ObjectNotFoundException(
                "Objeto não encontrado! Id: " + id +", Tipo: " + User.class.getName()));
    }
}
