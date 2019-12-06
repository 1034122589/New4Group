package com.itheima.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.health.entity.PageResult;
import com.itheima.health.entity.QueryPageBean;
import com.itheima.health.entity.Result;
import com.itheima.health.pojo.Menu;
import com.itheima.health.service.MenuService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/menu")
public class MenuController {
    @Reference
    private MenuService menuService;

    @RequestMapping("/findAll")
    public Result findAll(){
        List<Menu> list = menuService.findAll();
        if (list != null && list.size() > 0){
            return new Result(true,"查询成功",list);
        }
        return new Result(false,"查询失败");
    }

    /**
     * 新增
     * @param menu
     * @return
     */
    @RequestMapping("/add")
    public Result add(@RequestBody Menu menu) {
        //System.out.println(checkItem);
        try {
            menuService.add(menu);
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
        PageResult pageResult = menuService.pageQuery(
                queryPageBean.getCurrentPage(),
                queryPageBean.getPageSize(),
                queryPageBean.getQueryString()
        );
        return pageResult;
    }

    @RequestMapping("/delete")
    public Result delete(Integer id) {
        try {
            menuService.delete(id);
        } catch (RuntimeException e) {
            return new Result(false, "被角色应用,不可删除!");
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
            Menu menu = menuService.findById(id);
            return  new Result(true,"查询成功",menu);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"查询失败");
        }
    }
    @RequestMapping("/edit")
    public Result edit(@RequestBody Menu menu){
        try {
            menuService.edit(menu);
            return new Result(true,"编辑成功");
        } catch (Exception e) {
            return new Result(false,"编辑失败");
        }
    }
}
