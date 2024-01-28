package com.nhnacadymy.mido.account_backend_server.controller;

import com.nhnacadymy.mido.account_backend_server.exception.AccountAlreadyException;
import com.nhnacadymy.mido.account_backend_server.exception.AccountNotExistException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AccountNotExistException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<String> handleAccountNotExist(AccountNotExistException ex) {
        return new ResponseEntity<>("Account not found", HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(AccountAlreadyException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<String> handleAccountAlready(AccountAlreadyException ex) {
        return new ResponseEntity<>("Account Already Exist", HttpStatus.BAD_REQUEST);
    }
}