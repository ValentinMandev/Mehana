package com.softuni.mehana.utils;

import com.softuni.mehana.model.dto.UserRegisterDto;
import com.softuni.mehana.model.entities.UserEntity;
import com.softuni.mehana.model.entities.UserRoleEntity;
import com.softuni.mehana.model.enums.UserRoleEnum;
import com.softuni.mehana.repository.UserRepository;
import com.softuni.mehana.repository.UserRoleRepository;
import com.softuni.mehana.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Order(2)
@Component
public class InitializeUsers implements CommandLineRunner {

    UserService userService;
    UserRepository userRepository;
    UserRoleRepository userRoleRepository;

    public InitializeUsers(UserService userService, UserRepository userRepository, UserRoleRepository userRoleRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
        this.userRoleRepository = userRoleRepository;
    }

    @Override
    public void run(String... args) throws Exception {

        if (userRepository.count() == 0) {
            userService.registerUser(generateUser("valio", "1234", "1234", "valio@gmail.com",
                    "Валентин", "Мандев", "0888123123", "София, жк. Квартал 1"));
            userService.registerUser(generateUser("pesho", "1234", "1234", "pesho@gmail.com",
                    "Петър", "Петров", "0888456456", "София, жк. Квартал 2"));
            userService.registerUser(generateUser("tania", "1234", "1234", "tania@gmail.com",
                    "Таня", "Иванова", "0888789789", "София, жк. Квартал 3"));
            userService.registerUser(generateUser("galya", "1234", "1234", "galya@gmail.com",
                    "Галя", "Тодорова", "0888147147", "София, жк. Квартал 2"));

            addAdminRole(Long.valueOf(1));
            addAdminRole(Long.valueOf(4));
        }

    }

    private UserRegisterDto generateUser(String username, String password, String confirmPassword, String email,
                                         String firstName, String lastName, String phoneNumber, String address) {

        UserRegisterDto userRegisterDto = new UserRegisterDto();
        userRegisterDto.setUsername(username);
        userRegisterDto.setPassword(password);
        userRegisterDto.setConfirmPassword(confirmPassword);
        userRegisterDto.setEmail(email);
        userRegisterDto.setFirstName(firstName);
        userRegisterDto.setLastName(lastName);
        userRegisterDto.setPhoneNumber(phoneNumber);
        userRegisterDto.setAddress(address);

        return userRegisterDto;
    }

    private void addAdminRole(Long id) {

        UserEntity user = userRepository.findById(id).orElse(null);
        UserRoleEntity userRole = userRoleRepository.findByRole(UserRoleEnum.ADMIN).orElse(null);

        if (user != null && userRole != null) {
            user.getRoles().add(userRole);
        }

        userRepository.save(user);
    }
}
