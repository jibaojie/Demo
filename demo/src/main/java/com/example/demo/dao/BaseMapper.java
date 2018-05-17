package com.example.demo.dao;

import java.util.List;

/**
 * Created by jibaojie on 2017/7/12.
 */
public interface BaseMapper<T> {

    List<T> listAll();

    List<T> list();

    T getByPrimaryKey(T t);

    int insert(T t);

    void update(T t);

    void delete(T t);
}
