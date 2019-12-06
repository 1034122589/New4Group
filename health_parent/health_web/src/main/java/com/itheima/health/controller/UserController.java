package com.itheima.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.health.constant.MessageConstant;
import com.itheima.health.entity.PageResult;
import com.itheima.health.entity.QueryPageBean;
import com.itheima.health.entity.Result;
import com.itheima.health.pojo.Menu;
import com.itheima.health.pojo.Role;
import com.itheima.health.service.UserService;
import jdk.nashorn.internal.ir.ReturnNode;
import org.apache.jute.compiler.JString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserCache;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.cache.EhCacheBasedUserCache;
import org.springframework.security.core.userdetails.cache.NullUserCache;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

import javax.servlet.http.HttpServletRequest;
import java.awt.image.ImageProducer;
import java.util.*;

/**
 * @ClassName CheckItemController
 * @Description TODO
 * @Author ly
 * @Company 深圳黑马程序员
 * @Date 2019/11/19 15:50
 * @Version V1.0
 */
@RestController
@RequestMapping(value = "/user")
public class UserController {
    @Autowired
    BCryptPasswordEncoder passwordEncoder;

    @Reference
    UserService userService;

    @Autowired
    JedisPool jedisPool;

    // 从SpringSecurity中获取用户信息,和用户菜单
    @RequestMapping(value = "/getUsername")
    public Result getUsername() {
        try {
            User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            // 使用登录名，获取用户菜单
            String userName = user.getUsername();

            List menu = getMenu(userName);

            Map<String,Object> map = new HashMap<>();

            map.put("username",userName);
            map.put("menuList",menu);

            return new Result(true, MessageConstant.GET_USERNAME_SUCCESS, map);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.GET_USERNAME_FAIL);
        }
    }

    //获取用户菜单动态展示
    private List getMenu(String userName) {
        // 使用登录名，查询当前登录名对应用户信息
        com.itheima.health.pojo.User user2 = userService.findUserByUsername(userName);
        //封装当前用户菜单信息
        List<Map<String, Object>> menuList = new ArrayList<>();
        //1.封装工作台(固定)
        Map<String, Object> work = new HashMap<>();
        work.put("path", "1");
        work.put("title", "工作台");
        work.put("icon", "fa-dashboard");
        menuList.add(work);
        //2.封装当前用户动态菜单
        //2.1获取当前用户所有菜单信息
        Set<Role> roles = user2.getRoles();
        LinkedHashSet<Menu> menus = null;
        if (roles == null) {
//            //普通用户
//            Map<String, Object> common = new HashMap<>();
//            common.put("path", "2");
//            common.put("title", "会员管理");
//            common.put("icon", "fa-user-md");
//            common.put("children", "fa-user-md");
//
//            List<Map> commonCh = new ArrayList<>();
//            menuList.add(work);
            return null;
        }
        for (Role role : roles) {
            if (role == null) {
                return null;
            }
            menus = role.getMenus();
        }
        //2.2封装菜单信息
        if (menus == null) {
            return null;
        }
        //封装一级菜单
        Map<String, Object> parent = null;
        for (Menu menu : menus) {
            if (menu.getParentMenuId() == null) {
                parent = new HashMap<>();
                parent.put("path", menu.getPath());
                parent.put("title", menu.getName());
                parent.put("icon", menu.getIcon());
                //封装二级菜单
                List<Map<String, Object>> children = new ArrayList<>();
                for (Menu menu1 : menus) {
                    if (menu1.getParentMenuId() == menu.getId()) {
                        Map<String, Object> childrenMap = new HashMap<>();
                        childrenMap.put("path", menu1.getPath());
                        childrenMap.put("title", menu1.getName());
                        childrenMap.put("linkUrl", menu1.getLinkUrl());
                        children.add(childrenMap);
                    }
                }
                parent.put("children",children);
                menuList.add(parent);
            }
        }
        return menuList;
    }


    // 添加user
    @RequestMapping(value = "/add")
    public Result add(@RequestBody com.itheima.health.pojo.User user, Integer [] roleids){

        try {
            userService.add(user,roleids);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "新增用户失败");
        }

        return new Result(true, "新增用户成功");
    }


    // 角色查询
    @RequestMapping(value = "/findAll")
    public Result findAll(){

        try {
            List<Role>  list =userService.findAll();
            return new Result(true,"角色查询成功",list);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"角色查询失败");
        }


    }

    // 分页查询
    @RequestMapping(value = "/findPage")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean){
        PageResult pageResult = userService.findPage(queryPageBean.getQueryString(),queryPageBean.getCurrentPage(),queryPageBean.getPageSize());
        return pageResult;
    }

    // 用户的主键查询
    @RequestMapping(value = "/findById")
    public Result findById(Integer id){
        // ID查询
        com.itheima.health.pojo.User user = userService.findById(id);
        if(user!=null){
            return new Result(true, MessageConstant.QUERY_CHECKGROUP_SUCCESS,user);
        }
        else{
            return new Result(false, MessageConstant.QUERY_CHECKGROUP_FAIL);
        }
    }


    // 使用用户id，查询用户个=和角色的中间表，获取角色的集合，返回List<Integer>
    @RequestMapping(value = "/findCheckItemIdsByCheckGroupId")
    public List<Integer> findCheckItemIdsByCheckGroupId(Integer id){
        // 使用检查组的id，查询检查项的ID集合
        List<Integer> list = userService.findCheckItemIdsByCheckGroupId(id);
        return list;
    }
    // 编辑检查组
    @RequestMapping(value = "/edit")
    public Result edit(@RequestBody com.itheima.health.pojo.User user, Integer [] roleids){
        try {

            // 编辑
            userService.edit(user,roleids);
            return new Result(true, "编辑用户成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "编辑用户失败");
        }
    }

    // 编辑检查组
    @RequestMapping(value = "/delect")
    @PreAuthorize(value = "hasAuthority('ROLE_DELETE')")
    public Result delect(Integer id){
        try {

            // 编辑
            userService.delect(id);
            return new Result(true, "删除用户成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "权限不足");
        }
    }

    // 使用用户id，查询用户个=和角色的中间表，获取角色的集合，返回List<Integer>
    @RequestMapping(value = "/findname")
    public Result findUserName(String username){
        // 使用检查组的id，查询检查项的ID集合
        com.itheima.health.pojo.User user = userService.findUserName(username);
        if (user==null){
            return new Result(true, "用户名可用使用");
        }else {
            return new Result(false, "用户已存在");
        }

    }
}
