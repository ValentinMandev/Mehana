package com.softuni.mehana.model.userdetails;

import java.util.UUID;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

@Getter
@Setter
public class UserDetailsEntity extends User {

    private final UUID uuid;
    private String firstName;


    public UserDetailsEntity(
            UUID uuid,
            String username,
            String password,
            String firstName,
            Collection<? extends GrantedAuthority> authorities
    ) {
        super(username, password, authorities);
        this.uuid = uuid;
        this.firstName = firstName;
    }

}
