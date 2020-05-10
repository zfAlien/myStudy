package com.juquren.service;

import com.juquren.entity.User;

/**
 * Created by zhengfeng on 2017/5/26.
 */
public interface userMapperService {

    User selectByPrimaryKey(int id);

    void testDubbo();
}
