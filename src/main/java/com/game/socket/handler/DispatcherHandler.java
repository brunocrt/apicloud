package com.game.socket.handler;

import com.game.exception.ErrorCode;
import com.game.exception.ServiceException;
import com.game.facade.UserFacade;
import com.game.message.ResponseWrapper;
import com.game.repository.pojo.User;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;


import static com.game.message.RequestWrapper.Type.*;
import static com.game.message.RequestWrapper.*;


@Component
@ChannelHandler.Sharable
public class DispatcherHandler extends SimpleChannelInboundHandler<Request> {

    private static final Logger logger = LoggerFactory.getLogger(WebSocketServerHandler.class);

    public static ThreadLocal<Long> cmdLocalVar = new ThreadLocal<>();

    @Autowired
    private UserFacade userFacade;

    private Map<Type, Handleable<ChannelHandlerContext, Request>> handlerMapper = new ConcurrentHashMap();

    /**
     * configure handler
     */

    @PostConstruct
    public void init() {
        handlerMapper.put(LOGIN, this::login);
    }


    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Request request) throws Exception {
        Type type = request.getType();
        cmdLocalVar.set(request.getCommandId());
        Handleable handler = handlerMapper.get(type);
        Objects.requireNonNull(handler, "Consumer not found");
        handler.handle(channelHandlerContext, request);
    }

    /**
     * socket interface
     *
     * @param channelHandlerContext
     * @param request
     */
    public void login(ChannelHandlerContext channelHandlerContext, Request request) throws ServiceException {
        Login loginMessage = request.getExtension(login);
        ResponseWrapper.Response response;
        User user = userFacade.login(loginMessage.getUser());
        response = ResponseWrapper.Response.newBuilder()
                .setState(ErrorCode.SUCCESS.getCode())
                .setValue(String.valueOf(user.getBalance()))
                .setCommandId(request.getCommandId())
                .build();
        channelHandlerContext.writeAndFlush(response);
    }


}
