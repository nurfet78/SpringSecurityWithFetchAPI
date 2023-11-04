package com.nurfet.fetchapi.service;

import com.nurfet.fetchapi.repository.RoleRepository;
import com.nurfet.fetchapi.model.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.annotation.PostConstruct;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleDAO;

    @Autowired
    public RoleServiceImpl(RoleRepository roleDAO) {
        this.roleDAO = roleDAO;
    }

    @Transactional(readOnly = true)
    @Override
    public List<Role> findAllRole() {
        return roleDAO.findAll();
    }

    @Override
    @PostConstruct
    public void addDefaultRole() {
        roleDAO.save(new Role("ROLE_USER"));
        roleDAO.save(new Role("ROLE_ADMIN"));
    }

    @Transactional(readOnly = true)
    @Override
    public Set<Role> findByIdRoles(List<Long> roles) {
        return new HashSet<>(roleDAO.findAllById(roles));
    }
}
