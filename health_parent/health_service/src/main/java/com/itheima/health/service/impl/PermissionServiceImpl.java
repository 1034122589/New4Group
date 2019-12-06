package com.itheima.health.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.health.dao.PermissionDao;
import com.itheima.health.entity.PageResult;
import com.itheima.health.pojo.Permission;
import com.itheima.health.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service(interfaceClass = PermissionService.class)
@Transactional
public class PermissionServiceImpl implements PermissionService {
    @Autowired
    private PermissionDao permissionDao;

    @Override
    public List<Permission> findAll() {
        return permissionDao.findAll();
    }


    @Override
    public void add(Permission permission) {
        permissionDao.add(permission);
    }

    /**
     * 分页查询
     * @param currentPage
     * @param pageSize
     * @param queryString
     * @return
     */
    @Override
    public PageResult pageQuery(Integer currentPage, Integer pageSize, String queryString) {
        PageHelper.startPage(currentPage, pageSize);
        Page<Permission> page = permissionDao.selectByCondition(queryString);
        return new PageResult(page.getTotal(), page.getResult());
    }

    /**
     * 数据回显
     *
     * @param id
     * @return
     */
    @Override
    public Permission findById(Integer id) {
        return permissionDao.findById(id);
    }

    /**
     * 根据id删除当前项
     * @param id
     */
    @Override
    public void delete(Integer id) {
        permissionDao.delete(id);
    }

    /**
     * 编辑保存
     * @param permission
     */
    @Override
    public void edit(Permission permission) {
        permissionDao.edit(permission);
    }


}
