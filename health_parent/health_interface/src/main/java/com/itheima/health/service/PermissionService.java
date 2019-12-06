package com.itheima.health.service;

import com.itheima.health.entity.PageResult;
import com.itheima.health.pojo.Permission;

import java.util.List;

public interface PermissionService {
    List<Permission> findAll();

    void add(Permission permission);

    /**
     * 分页查询
     * @param currentPage
     * @param pageSize
     * @param queryString
     * @return
     */
    PageResult pageQuery(Integer currentPage, Integer pageSize, String queryString);

    Permission findById(Integer id);

    void delete(Integer id);

    void edit(Permission permission);
}
