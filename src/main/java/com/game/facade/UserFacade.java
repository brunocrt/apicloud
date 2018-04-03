package com.game.facade;

import com.game.exception.ServiceException;
import com.game.repository.pojo.User;
import com.game.repository.service.LoginServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserFacade {

    @Autowired
    protected LoginServiceImpl loginService;

    public User login(String name) throws ServiceException{
        return loginService.login(name);
    }

}
