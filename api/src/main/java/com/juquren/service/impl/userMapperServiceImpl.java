package com.juquren.service.impl;

import com.common.sdk.ContentFacade;
import com.juquren.dao.UserMapper;
import com.juquren.entity.User;
import com.juquren.service.userMapperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by zhengfeng on 2017/5/26.
 */
@Service("userMapperService")
public class userMapperServiceImpl implements userMapperService {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private ContentFacade contentFacade;

    @Override
    public User selectByPrimaryKey(int id) {
        String name = contentFacade.getName();
        Integer num = contentFacade.getNum();
        System.out.println(name);
        return userMapper.selectByPrimaryKey(id);
    }
}
