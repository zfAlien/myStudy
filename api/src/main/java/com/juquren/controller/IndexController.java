package com.juquren.controller;

import com.alibaba.fastjson.JSON;
import com.juquren.entity.User;
import com.juquren.invocation.JDKDynamicProxy;
import com.juquren.service.impl.userMapperServiceImpl;
import com.juquren.service.userMapperService;
import com.juquren.task.ScheduledTask;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;
import sun.misc.ProxyGenerator;

import javax.servlet.http.HttpServletResponse;
import java.io.FileOutputStream;
import java.io.IOException;
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
    public String index() {
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
        User testFactoryBean = (User) wac.getBean("testFactoryBean");
        System.out.println(testFactoryBean.getId());
        User user = userMapperService.selectByPrimaryKey(1);
        List<String> test = new ArrayList<>();
        return JSON.toJSONString(test);
    }

    @GetMapping("/test")
    @RequiresPermissions("index:test")
    @ResponseBody
    public List test(HttpServletResponse res) {
        List<String> a = new ArrayList<>();
        a.add("1");
        a.add("2");
        return a;
    }

    @GetMapping("/list")
    @RequiresPermissions("index:list")
    @ResponseBody
    public List list(HttpServletResponse res) {
        List<String> a = new ArrayList<>();
        a.add("1");
        a.add("2");
        return a;
    }

    @GetMapping("/test/task")
    @ResponseBody
    public void testTask(HttpServletResponse res) {
        ScheduledTask scheduledTask = new ScheduledTask();
        scheduledTask.run();
    }

    @GetMapping("/test/jdk/proxy")
    @ResponseBody
    public void testJdkProxy(HttpServletResponse res) throws IOException {
        System.getProperties().put("jdk.proxy.ProxyGenerator.saveGeneratedFiles", "true");
        byte[] classFile = ProxyGenerator.generateProxyClass("$Proxy0", userMapperServiceImpl.class.getInterfaces());
        FileOutputStream out = new FileOutputStream( "d:\\$Proxy0.class");
        out.write(classFile);
        // jdk动态代理测试
        userMapperService subject = new JDKDynamicProxy(new userMapperServiceImpl()).getProxy();
        subject.selectByPrimaryKey(1);
    }
}
