package com.zbartholomew.springbootsecuritymysqllogin.service;

import com.zbartholomew.springbootsecuritymysqllogin.model.User;

public interface UserService {
    public User findUserByEmail(String email);
    public void saveUser(User user);
}