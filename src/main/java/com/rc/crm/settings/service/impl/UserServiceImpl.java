package com.rc.crm.settings.service.impl;

import com.rc.crm.settings.dao.UserMapper;
import com.rc.crm.settings.domain.User;
import com.rc.crm.settings.domain.UserExample;
import com.rc.crm.exception.InvalidLoginException;
import com.rc.crm.settings.service.UserService;
import com.rc.crm.util.MD5Util;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * @author rc
 */
@Service
public class UserServiceImpl implements UserService {

    @Resource
    private UserMapper userMapper;

    private static final String ACCOUNT_LOCKED_OUT = "1";

    @Override
    public User login(String username, String password, String ip) throws InvalidLoginException {
        UserExample example = new UserExample();
        UserExample.Criteria criteria = example.createCriteria();
        criteria.andUsernameEqualTo(username);
        criteria.andPasswordEqualTo(MD5Util.getMD5(password));

        List<User> users = userMapper.selectByExample(example);
        if (users.isEmpty()) {
            throw new InvalidLoginException("用户名或密码错误");
        }

        User user = users.get(0);
        String allowIps = user.getAllowIps();
        if (!"".equals(allowIps) && !allowIps.contains(ip)) {
            throw new InvalidLoginException("IP 地址受限");
        }

        Date now = new Date();
        Date expireTime = user.getExpireTime();
        if (expireTime != null && expireTime.before(now)) {
            throw new InvalidLoginException("账户已过期");
        }

        if (ACCOUNT_LOCKED_OUT.equals(user.getLockedOut())) {
            throw new InvalidLoginException("账户已锁定");
        }
        return user;
    }

    @Override
    public List<User> getAllUsers() {
        return userMapper.selectByExample(new UserExample());
    }

    @Override
    public User getUserById(String id) {
        return userMapper.selectByPrimaryKey(id);
    }
}
