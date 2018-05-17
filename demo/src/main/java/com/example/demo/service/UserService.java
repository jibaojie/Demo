package com.example.demo.service;

import com.example.demo.entity.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by jibaojie on 2017/7/12.
 */
public interface UserService extends BaseService<User>{

    List<User> listPage();

    User getUserByUserId(Integer userId);

}
