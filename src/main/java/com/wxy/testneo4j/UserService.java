package com.wxy.testneo4j;

import java.util.List;

public interface UserService {
    User saveOrUpdate(User user);
    User getById(Long id);
    List<User> getUserList();
}
