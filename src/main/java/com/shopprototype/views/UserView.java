package com.shopprototype.views;


import com.shopprototype.domain.Role;
import com.shopprototype.domain.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserView {
    private Integer id;
    private String name;
    private String email;
    private List<String> role = new ArrayList<>();

    public UserView(User user){

        this.id = user.getId();
        this.name = user.getName();
        this.email = user.getEmail();
        user.getRole().forEach(role -> {
            this.role.add(role.getAuthority());
        });
    }
}
