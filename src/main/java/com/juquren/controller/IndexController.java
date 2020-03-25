package com.juquren.controller;

import com.alibaba.fastjson.JSON;
import com.juquren.bean.TestFactoryBean;
import com.juquren.entity.User;
import com.juquren.service.userMapperService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/")
public class IndexController {
    @Autowired
    userMapperService userMapperService;
    @Autowired
    User testFactoryBean;
    @GetMapping("/index")
    @ResponseBody
    public String index(){
        WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
        try {
            UsernamePasswordToken token = new UsernamePasswordToken("zhengfeng", "123");
            Subject subject = SecurityUtils.getSubject();
            subject.login(token);
        } catch (UnknownAccountException e) {
            return e.getMessage();
        } catch (IncorrectCredentialsException e) {
            return e.getMessage();
        } catch (LockedAccountException e) {
            return e.getMessage();
        } catch (AuthenticationException e) {
            return "账户验证失败";
        }
        System.out.println("我想去创建");
        User testFactoryBean = (User)wac.getBean("testFactoryBean");
        System.out.println(testFactoryBean.getId());
        User user = userMapperService.selectByPrimaryKey(1);
        List<String> test = new ArrayList<>();
        return JSON.toJSONString(test);
    }

    @GetMapping("/test")
    @RequiresPermissions("index:test")
    @ResponseBody
    public List test(HttpServletResponse res){
        List<String> a = new ArrayList<>();
        a.add("1");
        a.add("2");
        return a;
    }

    @GetMapping("/list")
    @RequiresPermissions("index:list")
    @ResponseBody
    public List list(HttpServletResponse res){
        List<String> a = new ArrayList<>();
        a.add("1");
        a.add("2");
        return a;
    }
}
