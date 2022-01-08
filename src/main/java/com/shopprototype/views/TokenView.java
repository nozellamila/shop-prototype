package com.shopprototype.views;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class TokenView {

    private String token;
    private String type;

    public TokenView(String token, String type) {
        this.token = token;
        this.type = type;
    }
}
