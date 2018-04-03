package com.game.api.controller;


import com.game.bean.RestResult;
import com.game.exception.ServiceException;
import com.game.repository.pojo.User;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@Validated
@RequestMapping(value = "service/api", method = RequestMethod.GET)
public class IndexController extends BaseController{

    @ResponseBody
    @RequestMapping(params = {"action=login"})
    @ResponseStatus(HttpStatus.OK)
    public RestResult login(
            @RequestParam
                    String token,
            @RequestParam
                    int gametype,
            @RequestParam
                    int gameversion,
            @RequestParam
                    String privatekey
    ) throws ServiceException {

            User user = userFacade.login("HelloWorld1");
            return new RestResult(0, user.getName(),String.valueOf(user.getBalance()));

    }

    @ResponseBody
    @RequestMapping(params = {"action=login","version=1.0"})
    @ResponseStatus(HttpStatus.OK)
    public RestResult login2(
            @RequestParam
                    String token,
            @RequestParam
                    int gametype,
            @RequestParam
                    int gameversion,
            @RequestParam
                    String privatekey
    ) throws ServiceException {

        User user = userFacade.login("HelloWorld-V1.0");
        return new RestResult(0, user.getName(),String.valueOf(user.getBalance()));

    }

    @ResponseBody
    @RequestMapping(params = {"action=logout"})
    @ResponseStatus(HttpStatus.OK)
    public RestResult logout(
            @RequestParam
                    String token,
            @RequestParam
                    int gametype,
            @RequestParam
                    int gameversion,
            @RequestParam
                    String privatekey) {

        return new RestResult(888,"111","111");
    }
}
