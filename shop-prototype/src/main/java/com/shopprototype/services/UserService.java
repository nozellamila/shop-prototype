package com.shopprototype.services;

import com.shopprototype.domain.User;
import com.shopprototype.form.UserForm;
import com.shopprototype.repositories.UserRepository;
import com.shopprototype.services.exceptions.ObjectNotFoundException;
import com.shopprototype.services.exceptions.ServiceException;
import com.shopprototype.views.UserView;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.Pageable;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
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

    public ResponseEntity<UserView> postUser(UserForm userForm, UriComponentsBuilder builder) throws ServiceException {

        User user = userRepository.findByEmail(userForm.getEmail());

        if(user != null)
            throw new ServiceException("Usuário já cadastrado com o e-mail: " + user.getEmail());
        else {
            ModelMapper modelMapper = new ModelMapper();
            user = modelMapper.map(userForm, User.class);

            userRepository.save(user);

            URI uri = builder.path("/users/{id}").buildAndExpand(user.getId()).toUri();
            return ResponseEntity.created(uri).body(new UserView(user));
        }
    }

    public ResponseEntity<UserView> putUser(Integer id, UserForm userForm) {
        Optional<User> user = userRepository.findById(id);

        if(!user.isPresent())
            throw new ObjectNotFoundException("Usuário não encontrado");
        else {
            User userFound = user.get();
            userFound.setName(userForm.getName());
            userFound.setEmail(userForm.getEmail());
            userFound.setPassword(userForm.getPassword());
            userFound.setAdmin(userForm.getAdmin());

            return ResponseEntity.ok(new UserView(userFound));
        }
    }
}
