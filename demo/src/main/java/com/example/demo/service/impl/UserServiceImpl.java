package com.example.demo.service.impl;

import com.example.demo.dao.UserMapper;
import com.example.demo.entity.User;
import com.example.demo.service.UserService;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jibaojie on 2017/7/12.
 */
@Service("userService")
public class UserServiceImpl extends BaseServiceImpl<User> implements UserService{

    @Autowired
    private UserMapper userMapper;

    @Override
    public User getUserByUserId(Integer userId) {
        User user = new User();
        user.setUserId(userId);
        return userMapper.getByUserId(user);
    }

    @Override
    public List<User> listPage() {
        PageHelper.startPage(1, 2, "userId");
        return userMapper.listPage();
    }
}
