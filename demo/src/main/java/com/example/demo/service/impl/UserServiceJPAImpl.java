package com.example.demo.service.impl;

import com.example.demo.dao.UserJPADao;
import com.example.demo.dao.UserMapper;
import com.example.demo.entity.User;
import com.example.demo.entity.UserJPA;
import com.example.demo.service.UserJPAService;
import com.example.demo.service.UserService;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by jibaojie on 2017/7/12.
 */
@Service("userJPAService")
public class UserServiceJPAImpl extends BaseServiceIPAImpl<UserJPA> implements UserJPAService{

    @Autowired
    private UserJPADao userJPADao;

    @Override
    public UserJPA getUserByUserId(Integer userId) {
        return userJPADao.findByName("baojie");
    }

    @Override
    public List<UserJPA> listPage() {
        return null;
    }
}
