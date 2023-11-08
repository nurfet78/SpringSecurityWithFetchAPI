package com.nurfet.fetchapi.controller;

import com.nurfet.fetchapi.exception.ExceptionInfo;
import com.nurfet.fetchapi.exception.UserUsernameExistException;
import com.nurfet.fetchapi.model.User;
import com.nurfet.fetchapi.service.UserService;
import com.nurfet.fetchapi.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;
import jakarta.validation.Valid;
import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/api")
public class RestApiController {

    private final UserService userService;


    @Autowired
    public RestApiController(RoleService roleService, UserService userService) {
        this.userService = userService;
    }


    @GetMapping("/users")
    public ResponseEntity<List<User>> getUsers() {
        return new ResponseEntity<>(userService.findAll(), HttpStatus.OK);
    }


    @PostMapping("/users")
    public ResponseEntity<ExceptionInfo> createUser(@Valid @RequestBody User user, BindingResult bindingResult) throws UserUsernameExistException {
        if (bindingResult.hasErrors()) {
            String error = getErrorsFromBindingResult(bindingResult);
            return new ResponseEntity<>(new ExceptionInfo(error), HttpStatus.BAD_REQUEST);
        }
        try {
            userService.save(user);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (DataIntegrityViolationException ex) {
            addErrorIfDataIntegrityViolationException(bindingResult);
            String error = getErrorsFromBindingResult(bindingResult);
            return new ResponseEntity<>(new ExceptionInfo(error), HttpStatus.CONFLICT);
        }
    }


    @DeleteMapping("/users/{id}")
    public ResponseEntity<ExceptionInfo> pageDelete(@PathVariable("id") long id) {
        userService.deleteById(id);
        return new ResponseEntity<>(new ExceptionInfo("User deleted"), HttpStatus.OK);
    }


    @GetMapping("users/{id}")
    public ResponseEntity<User> getUser(@PathVariable("id") long id) {
        User user = userService.getById(id);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }


    @GetMapping("/user")
    public ResponseEntity<User> getUserByUsername(Principal principal) {
        User user = userService.findByUsername(principal.getName());
        return new ResponseEntity<>(user, HttpStatus.OK);
    }


    @PutMapping("/users/{id}")
    public ResponseEntity<ExceptionInfo> pageEdit(@PathVariable("id") long id,
                                                  @Valid @RequestBody User user,
                                                  BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            String error = getErrorsFromBindingResult(bindingResult);
            return new ResponseEntity<>(new ExceptionInfo(error), HttpStatus.BAD_REQUEST);
        }
        try {
            String oldPassword = userService.getById(id).getPassword();
            if (oldPassword.equals(user.getPassword())) {
                System.out.println("TRUE");
                user.setPassword(oldPassword);
                userService.update(user);
            } else {
                System.out.println("FALSE");
                userService.save(user);
            }
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (UserUsernameExistException u) {
            throw new UserUsernameExistException("User with username exist");
        }
    }

    private String getErrorsFromBindingResult(BindingResult bindingResult) {
        return bindingResult.getFieldErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.joining("; "));
    }

    private void addErrorIfDataIntegrityViolationException(BindingResult bindingResult) {
        bindingResult.addError(new FieldError(bindingResult.getObjectName(),
                "email", "E-mail must be unique"));
    }
}