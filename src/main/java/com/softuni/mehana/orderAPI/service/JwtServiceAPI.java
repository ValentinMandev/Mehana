package com.softuni.mehana.orderAPI.service;

import org.springframework.security.core.userdetails.UserDetails;

public interface JwtServiceAPI {
    UserDetails extractUserInformation(String jwtToken);
}
