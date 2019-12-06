package com.itheima.health.dao;

import com.github.pagehelper.Page;
import com.itheima.health.pojo.Menu;

import java.util.List;
import java.util.Set;

public interface MenuDao {

    Set<Menu> findMenusByRoleId(Integer roleId);

    List<Menu> findAll();

    void add(Menu menu);

    Page<Menu> selectByCondition(String queryString);

    Menu findById(Integer id);

    void edit(Menu menu);

    void delete(Integer id);
}
