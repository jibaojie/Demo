package com.example.demo.entity;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by jibaojie on 2017/7/11.
 */
@Entity
@Table(name = "user1", schema = "dbo", catalog = "gpallas")
public class UserJPA extends BaseEntity implements Serializable{

    private Integer userId;
    private String name;
    private String password;
    private Integer age;


    @Id
    @Column(name = "userid")
    public Integer getUserId() {
        return userId;
    }

    @Basic
    @Column(name = "name")
    public String getName() {
        return name;
    }

    @Basic
    @Column(name = "password")
    public String getPassword() {
        return password;
    }

    @Basic
    @Column(name = "age")
    public Integer getAge() {
        return age;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "User{" +
                ", userId=" + userId +
                ", name='" + name + '\'' +
                ", password='" + password + '\'' +
                ", age=" + age +
                '}';
    }
}
