package com.itheima.health.service;

import com.itheima.health.entity.PageResult;
import com.itheima.health.pojo.Menu;

import java.util.List;

public interface MenuService {

    List<Menu> findAll();

    void add(Menu menu);

    PageResult pageQuery(Integer currentPage, Integer pageSize, String queryString);

    void delete(Integer id);

    Menu findById(Integer id);

    void edit(Menu menu);

}
