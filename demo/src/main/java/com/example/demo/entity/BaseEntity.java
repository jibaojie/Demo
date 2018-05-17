package com.example.demo.entity;

import io.swagger.models.auth.In;

import javax.persistence.MappedSuperclass;
import java.io.Serializable;

/**
 * MappedSuperclass注解只能标准在类上
 * Created by jibaojie on 2017/7/12.
 * 标注为@MappedSuperclass的类将不是一个完整的实体类，他将不会映射到数据库表，但是他的属性都将映射到其子类的数据库字段中
 * 标注为@MappedSuperclass的类不能再标注@Entity或@Table注解，也无需实现序列化接口
 *
 */
@MappedSuperclass
public class BaseEntity implements Serializable{

    private Integer page;
    private Integer limit;

    public Integer getPage() {
        return page;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }
}
