package com.wxy.usertest;

import com.wxy.testneo4j.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserSericeImpl implements UserService {
    private UserRepository userRepository;

    @Autowired
    public UserSericeImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @Override
    public User saveOrUpdate(User user) {
       return userRepository.save(user);
    }

    @Override
    public User getById(Long id) {
        return  userRepository.findById(id).orElse(null);
    }

    @Override
    public List<User> getUserList() {
        return userRepository.getUserNodeList();
    }


}
