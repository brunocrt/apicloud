package com.game.socket;

import com.game.DataContext;
import com.game.socket.handler.DispatcherHandler;
import com.game.socket.handler.SocketExceptionHandler;
import com.game.socket.handler.WebSocketServerHandler;
import com.game.socket.codec.WebSocketEncode;
import com.game.message.RequestWrapper;
import com.google.protobuf.ExtensionRegistry;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.net.InetSocketAddress;

@org.springframework.context.annotation.Configuration
@Service
public class NettyServer {
    private static final Logger logger = LoggerFactory.getLogger(NettyServer.class);

    @Value("${socket.server.host}")
    private String host;

    @Value("${socket.server.port}")
    private Integer port;

    @Value("${web.upload-path}")
    private String path;

    public String getHost() {
        return host;
    }

    public Integer getPort() {
        return port;
    }

    public String getPath() {
        return path;
    }

    public Integer getThreads() {
        return threads;
    }

    public ServerBootstrap getBootstrap() {
        return bootstrap;
    }

    @Value("${socket.server.threads}")
    private Integer threads;

    private ServerBootstrap bootstrap;

    static ExtensionRegistry registry = ExtensionRegistry.newInstance();
    static {RequestWrapper.registerAllExtensions(registry);}

    @PostConstruct
    public void init() {
        Thread t = new Thread(() -> socketIOServer());
        t.start();
    }

    public NettyServer socketIOServer() {
        EventLoopGroup boss = new NioEventLoopGroup(1);
        EventLoopGroup worker = new NioEventLoopGroup();

        bootstrap = new ServerBootstrap();
        bootstrap.group(boss, worker).channel(NioServerSocketChannel.class).handler(new LoggingHandler(LogLevel.DEBUG))
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    public void initChannel(SocketChannel ch) throws Exception {
                        ChannelPipeline pipeline = ch.pipeline();
                        pipeline.addLast(new HttpServerCodec());
                        pipeline.addLast(new HttpObjectAggregator(64*1024));
                        //pipeline.addLast(new WebSocketServerCompressionHandler());
                        pipeline.addLast(new WebSocketServerHandler());
                        pipeline.addLast(new WebSocketEncode());
                        pipeline.addLast(new ProtobufDecoder(RequestWrapper.Request.getDefaultInstance(),registry));
                        pipeline.addLast(new com.game.socket.handler.LoggingHandler.RequestLoggingHandler());
                        pipeline.addLast(new com.game.socket.handler.LoggingHandler.ResponseLoggingHandler());
                        pipeline.addLast(new ProtobufEncoder());
                        pipeline.addLast((DispatcherHandler)DataContext.getContext().getBean("dispatcherHandler"));
                        pipeline.addLast(new SocketExceptionHandler());

                    }
                });

        // Start the client.
        ChannelFuture ch = null;
        try {

            ch = bootstrap.bind(new InetSocketAddress(host, port)).sync();
            logger.info("server start successfully, {}:{}", host, port);
            ch.channel().closeFuture().sync();
            boss.shutdownGracefully();
            worker.shutdownGracefully();
        } catch (InterruptedException e) {
            logger.error("InterruptedException occur", e);
        }
        return this;
    }

    @PreDestroy
    public void destory() {

    }


}
