package com.softuni.mehana.service.exception;

public class CartItemEntityNotFoundException extends RuntimeException{

    public CartItemEntityNotFoundException(String message) {
        super(message);
    }
}
