package com.example.demo.service.impl;

import com.example.demo.dao.BaseMapper;
import com.example.demo.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

/**
 * Created by jibaojie on 2017/7/12.
 */
public class BaseServiceImpl<T> implements BaseService<T>{

    @Autowired
    private BaseMapper<T> baseMapper;

    @Override
    public List<T> listAll() {
        return baseMapper.listAll();
    }

    @Override
    public  List<T> list() {
        return baseMapper.listAll();
    }

    @Override
    public T getByPrimaryKey(T t) {
        return baseMapper.getByPrimaryKey(t);
    }

    @Override
    public int insert(T t) {
        return baseMapper.insert(t);
    }

    @Override
    public void update(T t) {
        baseMapper.update(t);
    }

    @Override
    public void delete(T t) {
        baseMapper.delete(t);
    }
}
