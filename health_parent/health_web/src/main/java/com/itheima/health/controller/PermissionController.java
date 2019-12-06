package com.itheima.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.health.constant.MessageConstant;
import com.itheima.health.entity.PageResult;
import com.itheima.health.entity.QueryPageBean;
import com.itheima.health.entity.Result;
import com.itheima.health.pojo.Permission;
import com.itheima.health.service.PermissionService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/permission")
public class PermissionController {
    @Reference
    private PermissionService permissionService;

    /**
     * 新增权限项
     * @param permission
     * @return
     */
    @RequestMapping("/add")
    public Result add(@RequestBody Permission permission) {
        //System.out.println(checkItem);
        try {
            permissionService.add(permission);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "新增失败");
        }
        return new Result(true, "新增成功");
    }

    /**
     * 查询展示
     * @param queryPageBean
     * @return
     */
    @RequestMapping("/findPage")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean) {
        PageResult pageResult = permissionService.pageQuery(
                queryPageBean.getCurrentPage(),
                queryPageBean.getPageSize(),
                queryPageBean.getQueryString()
        );
        return pageResult;
    }

    @RequestMapping("/delete")
    public Result delete(Integer id) {
        try {
            permissionService.delete(id);
        } catch (RuntimeException e) {
                return new Result(false,"被角色应用,不可删除!");
        } catch (Exception e) {
            return new Result(false, "删除失败");
        }
        return new Result(true, "删除成功");
    }

    /**
     * 数据回显
     * @param id
     * @return
     */
    @RequestMapping("/findById")
    public Result findById(Integer id) {
        try {
            Permission permission = permissionService.findById(id);
            return  new Result(true,"查询成功",permission);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"查询失败");
        }
    }
    @RequestMapping("/edit")
    public Result edit(@RequestBody Permission permission){
        try {
            permissionService.edit(permission);
            return new Result(true,"编辑成功");
        } catch (Exception e) {
            return new Result(false,"编辑失败");
        }
    }

    @RequestMapping("/findAll")
    public Result findAll(){
        List<Permission> list = permissionService.findAll();
        if (list != null && list.size() > 0){
            return new Result(true, MessageConstant.QUERY_SUCCESS,list);
        }
        return new Result(false, MessageConstant.QUERY_FAIL);
    }
}
