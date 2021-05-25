package com.shopprototype.services;

import com.shopprototype.domain.Role;
import com.shopprototype.domain.User;
import com.shopprototype.forms.UserForm;
import com.shopprototype.repositories.RoleRepository;
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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.Pageable;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

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

        User user = new User();
        Optional<User> userAux = userRepository.findByEmail(userForm.getEmail());

        if (userForm.getRolesNames().size() > 0){
            List<String> roleNameList = userForm.getRolesNames();
            List<Role> roleList = new ArrayList<>();

            roleNameList.forEach(roleName -> {
                Optional<Role> roleAux = roleRepository.findByName(roleName);
                if (!roleAux.isPresent()){
                    Role role = new Role();
                    role.setName(roleName);
                    roleRepository.save(role);
                    roleList.add(role);
                }else {
                    roleList.add(roleAux.get());
                }
            });

            user.setRole(roleList);
        }

        if(userAux.isPresent())
            throw new ServiceException(HttpStatus.CONFLICT, "Usuário já cadastrado com o e-mail: " + userAux.get().getEmail());
        else {
            /*
            ModelMapper modelMapper = new ModelMapper();
            user = modelMapper.map(userForm, User.class);
             */
            user.setName(userForm.getName());
            user.setEmail(userForm.getEmail());

            String password = new BCryptPasswordEncoder().encode(userForm.getPassword());
            user.setPassword(password);
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
        if(user.isPresent() && userByEmail.isPresent())
            if(user.get().getId() != userByEmail.get().getId())
                throw new ServiceException(HttpStatus.CONFLICT, "Usuário já cadastrado com o e-mail: " + userByEmail.get().getEmail());

        User userFound;
        userFound = user.get();
        userFound.setName(userForm.getName());
        userFound.setEmail(userForm.getEmail());

        String password = new BCryptPasswordEncoder().encode(userForm.getPassword());
        userFound.setPassword(password);

        if (userForm.getRolesNames().size() > 0){
            List<String> roleNameList = userForm.getRolesNames();
            List<Role> roleList = new ArrayList<>();

            roleNameList.forEach(roleName -> {
                Optional<Role> roleAux = roleRepository.findByName(roleName);
                if (!roleAux.isPresent()){
                    Role role = new Role();
                    role.setName(roleName);
                    roleRepository.save(role);
                    roleList.add(role);
                }else {
                    roleList.add(roleAux.get());
                }
            });

            userFound.setRole(roleList);
        }

        return ResponseEntity.ok(new UserView(userFound));
    }

    public ResponseEntity<UserMessage> deleteUser(Integer id) throws ServiceException {
        Optional<User> user = userRepository.findById(id);

        if(!user.isPresent())
            return ResponseEntity.ok(new UserMessage("Nenhum usuário excluído"));
        else {
            if(user.get().getCart() != null)
                throw new ServiceException(HttpStatus.UNPROCESSABLE_ENTITY, "Não é possível excluir usuário com carrinho");
            else{
                userRepository.deleteById(user.get().getId());
                return ResponseEntity.ok(new UserMessage("Usuário excluído com sucesso"));
            }

        }
    }
}
