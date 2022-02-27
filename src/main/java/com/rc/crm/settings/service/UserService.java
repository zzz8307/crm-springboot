package com.rc.crm.settings.service;

import com.rc.crm.settings.domain.User;
import com.rc.crm.exception.InvalidLoginException;

import java.util.List;

/**
 * @author rc
 */
public interface UserService {
    User login(String username, String password, String ip) throws InvalidLoginException;

    List<User> getAllUsers();

    User getUserById(String id);
}
