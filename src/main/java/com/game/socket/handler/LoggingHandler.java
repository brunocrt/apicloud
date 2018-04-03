package com.game.socket.handler;

import com.game.message.RequestWrapper;
import com.game.message.ResponseWrapper;
import io.netty.channel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class LoggingHandler {

    private static final Logger logger = LoggerFactory.getLogger("IMLOG");

    @ChannelHandler.Sharable
    public static class RequestLoggingHandler extends ChannelInboundHandlerAdapter {
        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg)
                throws Exception {
            if (msg instanceof RequestWrapper.Request) {
                logger.debug(msg.toString().replaceAll("\r|\n", " "));
            }
            ctx.fireChannelRead(msg);
        }

    }

    @ChannelHandler.Sharable
    public static class ResponseLoggingHandler extends ChannelOutboundHandlerAdapter {
        @Override
        public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) {
            if (msg instanceof ResponseWrapper.Response) {
                logger.debug(msg.toString().replaceAll("\r|\n", " "));
            }
            ctx.writeAndFlush(msg);
        }
    }

}
