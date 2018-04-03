package com.game.socket.handler;

import com.game.exception.ErrorCode;
import com.game.exception.ServiceException;
import com.game.message.ResponseWrapper;
import io.netty.channel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.SocketAddress;


@ChannelHandler.Sharable
public class SocketExceptionHandler extends ChannelDuplexHandler {

    private static final Logger logger = LoggerFactory.getLogger(SocketExceptionHandler.class);

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        // Uncaught exceptions from inbound handlers will propagate up to this handler
        logger.error("Socket api call failed", cause);
        Long commandId = DispatcherHandler.cmdLocalVar.get();
        if(cause instanceof ServiceException) {
            ServiceException e  = (ServiceException)cause;
            ResponseWrapper.Response response = ResponseWrapper.Response.newBuilder()
                    .setState(e.getErrorCode().getCode())
                    .setMessage(e.getErrorCode().getDesc())
                    .setCommandId(commandId)
                    .build();
            ctx.writeAndFlush(response);
        }else {
            ResponseWrapper.Response response = ResponseWrapper.Response.newBuilder()
                    .setState(ErrorCode.SERVER_ERROR.getCode())
                    .setMessage(ErrorCode.SERVER_ERROR.getDesc())
                    .setCommandId(commandId)
                    .build();
            ctx.writeAndFlush(response);
        }

    }

    @Override
    public void connect(ChannelHandlerContext ctx, SocketAddress remoteAddress, SocketAddress localAddress, ChannelPromise promise) {
        ctx.connect(remoteAddress, localAddress, promise.addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) {
                if (!future.isSuccess()) {
                    // Handle connect exception here...
                    logger.error("Socket connect failed,remote:[{}],local:[{}]", remoteAddress, localAddress);
                }
            }
        }));
    }

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) {
        ctx.write(msg, promise.addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) {
                if (!future.isSuccess()) {
                    // Handle write exception here...
                    logger.error("Socket write msg failed, {}", msg.toString());

                }
            }
        }));
    }

}
