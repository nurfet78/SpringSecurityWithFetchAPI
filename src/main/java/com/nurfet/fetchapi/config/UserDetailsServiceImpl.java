package com.nurfet.fetchapi.config;

import com.nurfet.fetchapi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.nurfet.fetchapi.model.User;

@Transactional
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    UserRepository userDAO;


    @Autowired
    public UserDetailsServiceImpl(UserRepository userDAO) {
        this.userDAO = userDAO;
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {

        User user = userDAO.findByUsername(s);
            if (user == null) {
                throw new UsernameNotFoundException(s);
            }
            return user;
    }
}
