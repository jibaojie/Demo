package com.example.demo.service.impl;

import com.example.demo.dao.BaseMapper;
import com.example.demo.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by jibaojie on 2017/7/12.
 */
public class BaseServiceIPAImpl<T>  implements BaseService<T>{

    @Override
    public List<T> listAll() {
        return null;
    }

    @Override
    public List<T> list() {
        return null;
    }

    @Override
    public T getByPrimaryKey(T t) {
        return null;
    }

    @Override
    public int insert(T t) {
        return 0;
    }

    @Override
    public void update(T t) {

    }

    @Override
    public void delete(T t) {

    }
}
