package com.wxy.usertest;

import com.wxy.usertest.User;

import java.util.List;

public interface UserService {
    User saveOrUpdate(User user);
    User getById(Long id);
    List<User> getUserList();
}
