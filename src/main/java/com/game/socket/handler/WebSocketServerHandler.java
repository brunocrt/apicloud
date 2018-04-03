package com.game.socket.handler;

import com.game.cache.GlobalChannelCache;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.codec.http.websocketx.*;
import io.netty.util.CharsetUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Administrator on 2017/8/18.
 */
@org.springframework.context.annotation.Configuration
@ChannelHandler.Sharable
public class WebSocketServerHandler extends ChannelInboundHandlerAdapter {

    private static final Logger logger = LoggerFactory.getLogger(WebSocketServerHandler.class);

    private WebSocketServerHandshaker handshaker;


    @Value("${socket.server.host}")
    private String host;

    @Value("${socket.server.port}")
    private Integer port;

    //@Value("${ip.trust.array}")
    private List<String> arrayTest = new ArrayList<>();

    private String webSocketURL = "ws://"+ host +":"+ port +"/websocket";
    //private String webSocketURL = "ws://localhost:9988/websocket";


    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        // 添加
        //TODO 需要对连接做心跳检测，超时主动断开socket
        GlobalChannelCache.group.add(ctx.channel());
        ByteBuf b = Unpooled.buffer();
        //ctx.channel().writeAndFlush(b.writeBytes("hello client".getBytes()));
        logger.debug("客户端与服务端连接开启");
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        // 移除
        GlobalChannelCache.group.remove(ctx.channel());
        logger.debug("客户端与服务端连接关闭");
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg)
            throws Exception {
        if (msg instanceof FullHttpRequest) {
            handleHttpRequest(ctx, ((FullHttpRequest) msg));
        } else if (msg instanceof WebSocketFrame) {
            handlerWebSocketFrame(ctx, (WebSocketFrame) msg);
        }
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    private void handlerWebSocketFrame(ChannelHandlerContext ctx,
                                       WebSocketFrame frame) {

        // 判断是否关闭链路的指令
        if (frame instanceof CloseWebSocketFrame) {
            handshaker.close(ctx.channel(), (CloseWebSocketFrame) frame.retain());
            return;
        }
        // 判断是否ping消息
        if (frame instanceof PingWebSocketFrame) {
            ctx.channel().write(new PongWebSocketFrame(frame.content().retain()));
            return;
        }

        if (frame instanceof TextWebSocketFrame) {
            logger.debug("websocket socket dump: {}", ByteBufUtil.hexDump(frame.content()));
            logger.debug("websocket socket text message: {}", ((TextWebSocketFrame) frame).text());
            frame.release();
            return;
        }
        try {
            logger.debug("trustIPList : {}",arrayTest);
            logger.error("WebSocketContent 2dump:{}", ByteBufUtil.hexDump(frame.content()));
            logger.error("WebSocketContent 2readableBytes:{}", frame.content().readableBytes());
            ctx.fireChannelRead(frame.content());

        }catch (Throwable e) {
            e.printStackTrace();
        }
        /*
        String request = ((TextWebSocketFrame) frame).text();
        frame.release(); //此处需手动释放buffer，因为该buffer不再往下一个handler传递
        logger.debug("服务端收到：" + request);
        logger.debug(String.format("%s received %s", ctx.channel(), request));
        //logger.info(String.format("%s received %s", ctx.channel(), request));

        TextWebSocketFrame tws = new TextWebSocketFrame(new Date().toString() + ctx.channel().id() + "：" + request);

        TextWebSocketFrame tws2 = new TextWebSocketFrame("thanks to come");

        TextWebSocketFrame tws3 =  tws2.copy();
        // 群发
        GlobalChannelCache.group.writeAndFlush(tws);
        // 返回【谁发的发给谁】
        ctx.channel().writeAndFlush(tws2);
        ctx.channel().writeAndFlush(tws3);
        //ctx.channel().writeAndFlush( tws3.retainedDuplicate());*/
    }

    private void handleHttpRequest(ChannelHandlerContext ctx,
                                   FullHttpRequest req) {
        if (!req.decoderResult().isSuccess()
                || (!"websocket".equals(req.headers().get("Upgrade")))) {
            sendHttpResponse(ctx, req, new DefaultFullHttpResponse(
                    HttpVersion.HTTP_1_1, HttpResponseStatus.BAD_REQUEST));
            return;
        }
        WebSocketServerHandshakerFactory wsFactory = new WebSocketServerHandshakerFactory(
                webSocketURL, null, false);
        handshaker = wsFactory.newHandshaker(req);
        if (handshaker == null) {
            WebSocketServerHandshakerFactory.sendUnsupportedVersionResponse(ctx.channel());
        } else {
            handshaker.handshake(ctx.channel(), req);
        }
        req.release();
    }

    private static void sendHttpResponse(ChannelHandlerContext ctx,
                                         FullHttpRequest req, DefaultFullHttpResponse res) {
        // 返回应答给客户端
        if (res.status().code() != 200) {
            ByteBuf buf = Unpooled.copiedBuffer(res.status().toString(),
                    CharsetUtil.UTF_8);
            res.content().writeBytes(buf);
            buf.release();
        }
        // 如果是非Keep-Alive，关闭连接
        ChannelFuture f = ctx.channel().writeAndFlush(res);
        if (!isKeepAlive(req) || res.status().code() != 200) {
            f.addListener(ChannelFutureListener.CLOSE);
        }
    }

    private static boolean isKeepAlive(FullHttpRequest req) {
        return false;
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
            throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}