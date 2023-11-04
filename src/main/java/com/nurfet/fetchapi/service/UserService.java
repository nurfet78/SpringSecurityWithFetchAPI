package com.nurfet.fetchapi.service;

import com.nurfet.fetchapi.model.User;
import java.util.List;


public interface UserService {

    List<User> findAll();

    User getById(long id);

    void save(User user);

    void deleteById(long id);

    User findByUsername(String username);

    void addDefaultUser();

    void update(User user);

    User passwordCoder(User user);
}
