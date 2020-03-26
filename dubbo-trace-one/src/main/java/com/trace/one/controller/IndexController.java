package com.trace.one.controller;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/")
public class IndexController {

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
}
