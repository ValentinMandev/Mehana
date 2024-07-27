package com.softuni.mehana.service.implementation;

import com.softuni.mehana.model.entities.UserEntity;
import com.softuni.mehana.model.userdetails.UserDetailsEntity;
import com.softuni.mehana.model.entities.UserRoleEntity;
import com.softuni.mehana.model.enums.UserRoleEnum;
import com.softuni.mehana.repository.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetailsEntity loadUserByUsername(String username) throws UsernameNotFoundException {

        return userRepository
                .findByUsername(username)
                .map(UserDetailsServiceImpl::map)
                .orElseThrow(
                        () -> new UsernameNotFoundException("Username " + username + " not found!"));
    }

    private static UserDetailsEntity map(UserEntity userEntity) {

        return new UserDetailsEntity(
                userEntity.getUuid(),
                userEntity.getUsername(),
                userEntity.getPassword(),
                userEntity.getUserInfo().getFirstName(),
                userEntity.getRoles().stream().map(UserRoleEntity::getRole).map(UserDetailsServiceImpl::map).toList()
                );
    }

    private static GrantedAuthority map(UserRoleEnum role) {
        return new SimpleGrantedAuthority(
                "ROLE_" + role
        );
    }
}
