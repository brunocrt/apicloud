package com.game.repository.service;


import com.game.exception.ErrorCode;
import com.game.exception.ServiceException;
import com.game.repository.mybatis.UserDao;
import com.game.repository.pojo.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class LoginServiceImpl extends BaseServiceImpl{

    private Logger logger = LoggerFactory.getLogger(LoginServiceImpl.class);

    @Autowired UserDao userDao;

    public User login(String name) throws ServiceException {
        User user;
        if(redisTemplate.hasKey(TABLE_USER + DELIMITER + name)){
             user = (User) redisTemplate.opsForValue().get(TABLE_USER + DELIMITER + name);
        }else{
            user = userDao.queryByName(name);
            if(user == null) {
                logger.error("USER [{}] NO REGISTER ", name);
                throw new ServiceException(ErrorCode.USER_NO_REGISTER);
            }
            redisTemplate.opsForValue().set(TABLE_USER + DELIMITER + name, user, DEFAULT_TIMEOUT, TimeUnit.MINUTES);
        }
        return user;
    }


}
