package com.game.api.controller;

import com.game.facade.UserFacade;
import com.game.repository.service.LoginServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;

public class BaseController {

    @Autowired
    UserFacade userFacade;

}
