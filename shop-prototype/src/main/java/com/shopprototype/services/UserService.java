package com.shopprototype.services;

import com.shopprototype.domain.User;
import com.shopprototype.forms.UserForm;
import com.shopprototype.repositories.UserRepository;
import com.shopprototype.services.exceptions.ObjectNotFoundException;
import com.shopprototype.services.exceptions.ServiceException;
import com.shopprototype.views.UserMessage;
import com.shopprototype.views.UserView;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.Pageable;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    public ResponseEntity<Page<UserView>> getUser(Integer id, String name, String email, Pageable pageable){

        Page<User> users = userRepository.findByParameters(id, name, email, pageable);

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

        Optional<User> userAux = userRepository.findByEmail(userForm.getEmail());

        if(userAux.isPresent())
            throw new ServiceException("Usuário já cadastrado com o e-mail: " + userAux.get().getEmail());
        else {
            User user = new User();
            ModelMapper modelMapper = new ModelMapper();
            user = modelMapper.map(userForm, User.class);

            userRepository.save(user);

            URI uri = builder.path("/users/{id}").buildAndExpand(user.getId()).toUri();
            return ResponseEntity.created(uri).body(new UserView(user));
        }
    }

    public ResponseEntity<UserView> putUser(Integer id, UserForm userForm) throws ServiceException {
        Optional<User> user = userRepository.findById(id);
        Optional<User> userByEmail = userRepository.findByEmail(userForm.getEmail());

        if(!user.isPresent())
            throw new ObjectNotFoundException("Usuário não encontrado");
        if(user.isPresent() && userByEmail != null)
            if(user.get().getId() != userByEmail.get().getId())
                throw new ServiceException("Usuário já cadastrado com o e-mail: " + userByEmail.get().getEmail());

        User userFound;
        userFound = user.get();
        userFound.setName(userForm.getName());
        userFound.setEmail(userForm.getEmail());
        userFound.setPassword(userForm.getPassword());
        userFound.setRole(userForm.getRole());

        return ResponseEntity.ok(new UserView(userFound));
    }

    public ResponseEntity<UserMessage> deleteUser(Integer id) throws ServiceException {
        Optional<User> user = userRepository.findById(id);

        if(!user.isPresent())
            return ResponseEntity.ok(new UserMessage("Nenhum usuário excluído"));
        else {
            if(user.get().getCart() != null)
                throw new ServiceException("Não é possível excluir usuário com carrinho");
            else{
                userRepository.deleteById(user.get().getId());
                return ResponseEntity.ok(new UserMessage("Usuário excluído com sucesso"));
            }

        }
    }
}
