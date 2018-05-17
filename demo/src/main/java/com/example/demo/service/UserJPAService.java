package com.example.demo.service;

import com.example.demo.entity.User;
import com.example.demo.entity.UserJPA;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by jibaojie on 2017/7/12.
 */
public interface UserJPAService extends BaseService<UserJPA>{

    UserJPA getUserByUserId(Integer userId);

    List<UserJPA> listPage();
}
