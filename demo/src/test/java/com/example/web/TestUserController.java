package com.example.web;

import com.example.demo.DemoApplication;
import com.example.demo.dao.UserMapper;
import com.example.demo.entity.User;
import com.example.demo.entity.UserJPA;
import com.example.demo.service.UserJPAService;
import com.example.demo.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

/**
 * Created by jibaojie on 2017/7/12.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = DemoApplication.class)
public class TestUserController {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserService userService;

    @Autowired
    private UserJPAService userJPAService;

    @Test
    @Rollback
    public void findByName() throws Exception {
//        userMapper.insert(000, "AAA", "111",  20);
        User u = userMapper.findByName("AAA");
        System.out.println(u.toString());
        u.setUserId(11);
        User user = userMapper.getByPrimaryKey(u);
        System.out.println(user.toString());
    }
    @Test
    @Rollback
    public void testService() throws Exception {
//        userMapper.insert(000, "AAA", "111",  20);
        User u = userService.getUserByUserId(0);
        System.out.println(u.toString());
        u.setUserId(11);
        User user = userService.getByPrimaryKey(u);
        System.out.println(user.toString());
    }

    @Test
    public void testList() throws  Exception{
        List<User> list = userService.listPage();
        System.out.println(".............................." + list.size());
    }

    /**
     * 测试JPA
     * @throws Exception
     */
    @Test
    public void testJPAList() throws  Exception{
        UserJPA list = userJPAService.getUserByUserId(1);
        System.out.println(".............................." + list.toString());
    }



}
