package com.nurfet.fetchapi.service;

import com.nurfet.fetchapi.exception.NotFoundException;
import com.nurfet.fetchapi.repository.RoleRepository;
import com.nurfet.fetchapi.repository.UserRepository;
import com.nurfet.fetchapi.model.Role;
import com.nurfet.fetchapi.model.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import jakarta.annotation.PostConstruct;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.HashSet;


@Transactional
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userDAO;

    private final RoleRepository roleDAO;

    private final PasswordEncoder passwordEncoder;


    public UserServiceImpl(UserRepository userDAO, RoleRepository roleDAO, PasswordEncoder passwordEncoder) {
        this.userDAO = userDAO;
        this.roleDAO = roleDAO;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User passwordCoder(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return user;
    }

    @Transactional(readOnly = true)
    @Override
    public List<User> findAll() {
        return userDAO.findAll();
    }

    @Transactional(readOnly = true)
    @Override
    public User getById(long id) {

        return userDAO.findById(id).orElseThrow(() -> new NotFoundException(User.class, id));

    }

    @Transactional
    @Override
    public void save(User user) {
        userDAO.save(passwordCoder(user));
    }

    @Transactional
    @Override
    public void update(User user) {
        userDAO.save(user);
    }

    @Transactional
    @Override
    public void deleteById(long id) {
        User user = userDAO.findById(id).orElseThrow(() -> new NotFoundException(User.class, id));

        userDAO.delete(user);
    }


    @Transactional(readOnly = true)
    @Override
    public User findByUsername(String username) {
        return userDAO.findByUsername(username);
    }



    @Override
    @PostConstruct
    public void addDefaultUser() {
        Set<Role> roles1 = new HashSet<>();
        roles1.add(roleDAO.findById(1L).orElse(null));
        Set<Role> roles2 = new HashSet<>();
        roles2.add(roleDAO.findById(1L).orElse(null));
        roles2.add(roleDAO.findById(2L).orElse(null));
        User user1 = new User("Марина", "Маринина", "marina@mail.ru", "12345", roles1);
        User user2 = new User("Михаил", "Иванов", "admin@gmail.com", "admin", roles2);
        save(user1);
        save(user2);
    }

}

