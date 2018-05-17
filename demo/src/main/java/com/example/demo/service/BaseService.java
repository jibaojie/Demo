package com.example.demo.service;

import com.example.demo.entity.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * Created by jibaojie on 2017/7/12.
 */
public interface BaseService<T> {

    List<T> listAll();

    List<T> list();

    T getByPrimaryKey(T t);

    int insert(T t);

    void update(T t);

    void delete(T t);

}
