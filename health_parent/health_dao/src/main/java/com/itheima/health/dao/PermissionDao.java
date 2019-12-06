package com.itheima.health.dao;

import com.github.pagehelper.Page;
import com.itheima.health.pojo.Permission;

import java.util.List;
import java.util.Set;

public interface PermissionDao {

    Set<Permission> findPermissionsByRoleId(Integer roleId);

    List<Permission> findAll();

    void add(Permission permission);

    Page<Permission> selectByCondition(String queryString);

    Permission findById(Integer id);

    void edit(Permission permission);

    void delete(Integer id);
}
