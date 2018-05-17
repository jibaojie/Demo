package com.example.demo.dao;

import com.example.demo.entity.User;
import com.example.demo.entity.UserJPA;
import org.apache.ibatis.annotations.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by jibaojie on 2017/7/12.
 */
@Repository
public interface UserJPADao extends BaseDao<UserJPA, Long> {

    UserJPA findByName(String name);
}
