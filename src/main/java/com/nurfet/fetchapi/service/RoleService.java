package com.nurfet.fetchapi.service;

import com.nurfet.fetchapi.model.Role;
import java.util.List;
import java.util.Set;

public interface RoleService {
    List<Role> findAllRole();

    void addDefaultRole();

    Set<Role> findByIdRoles(List<Long> roles);
}
