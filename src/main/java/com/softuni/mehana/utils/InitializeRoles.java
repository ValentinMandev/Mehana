package com.softuni.mehana.utils;

import com.softuni.mehana.model.entities.UserRoleEntity;
import com.softuni.mehana.model.enums.UserRoleEnum;
import com.softuni.mehana.repository.UserRoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class InitializeRoles implements CommandLineRunner {

    private UserRoleRepository userRoleRepository;

    public InitializeRoles(UserRoleRepository userRoleRepository) {
        this.userRoleRepository = userRoleRepository;
    }

    @Override
    public void run(String... args) throws Exception {

        if (userRoleRepository.count() == 0) {
            UserRoleEntity admin = new UserRoleEntity();
            admin.setRole(UserRoleEnum.ADMIN);
            userRoleRepository.save(admin);

            UserRoleEntity user = new UserRoleEntity();
            user.setRole(UserRoleEnum.USER);
            userRoleRepository.save(user);
        }

    }

}
