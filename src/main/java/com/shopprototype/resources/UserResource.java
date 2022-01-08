package com.shopprototype.resources;

import com.shopprototype.domain.User;
import com.shopprototype.forms.UserForm;
import com.shopprototype.services.UserService;
import com.shopprototype.services.exceptions.ServiceException;
import com.shopprototype.views.UserMessage;
import com.shopprototype.views.UserView;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.data.domain.Pageable;
import org.springframework.web.util.UriComponentsBuilder;

import javax.transaction.Transactional;
import javax.validation.Valid;

@RestController
@RequestMapping("/users")
@Api( tags = "Users")
public class UserResource {

    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<Page<UserView>> getUser(@RequestParam(name = "id", required = false) Integer id,
                                                  @RequestParam(name = "name", required = false) String name,
                                                  @RequestParam(name = "email", required = false) String email,
                                                  Pageable pageable){
        return userService.getUser(id, name, email, pageable);
    }


    @RequestMapping(value="/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> find(@PathVariable Integer id){
        User obj = userService.find(id);
        return ResponseEntity.ok().body(obj);
    }

    @Transactional
    @PostMapping
    public ResponseEntity<UserView> postUser(@RequestBody @Valid UserForm userForm, UriComponentsBuilder builder) throws ServiceException {
        return userService.postUser(userForm, builder);
    }

    @Transactional
    @PutMapping(value = "/{id}")
    public ResponseEntity<UserView> putUser(@PathVariable Integer id, @RequestBody @Valid UserForm userForm) throws ServiceException {
        return userService.putUser(id, userForm);
    }

    @Transactional
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<UserMessage> deleteUser(@PathVariable Integer id) throws ServiceException {
        return userService.deleteUser(id);
    }
}
