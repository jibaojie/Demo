package com.example.demo.dao;

import com.example.demo.entity.User;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by jibaojie on 2017/7/12.
 */
@Repository
@Mapper
public interface UserMapper extends BaseMapper<User>{

    //查出来的id password为空，只有name和age列
//    @Results({
//            @Result(property = "name", column = "name"),
//            @Result(property = "age", column = "age")
//    })
    @Select("SELECT * FROM USER1")
    @Override
    List<User> listAll();

    @Override
    @Select("")
    List<User> list();

    @Override
    @Select("SELECT TOP 1 * FROM USER1 WHERE userId = #{userId}")
    User getByPrimaryKey(User user);

//    @Insert("INSERT INTO USER1(USERID, NAME, password, AGE) VALUES(#{userId}, #{name}, #{password}, #{age})")
//    @Override
//    int insert(User user);

    @Update("UPDATE USER1 SET age=#{age}, name = #{name}, password = #{password} WHERE userId=#{userId}")
    @Override
    void update(User user);

    @Delete("DELETE FROM USER1 WHERE userId =#{userId}")
    @Override
    void delete(User user);


    @Select("SELECT TOP 1 * FROM USER1 WHERE NAME = #{name}")
    User findByName(@Param("name") String name);


    List<User> listPage();

    User getByUserId(User user);

}
