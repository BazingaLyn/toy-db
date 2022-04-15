package org.elastic.toy.db.server;

import com.google.common.base.Strings;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.elastic.toy.db.config.RedisConfig;
import org.elastic.toy.db.resp.RespInitializerHandler;

public class RedisServer {


    private static final int BUFFER_SIZE = 1024 * 1024;


    private EventLoopGroup bossGroup;
    private EventLoopGroup workerGroup;
    private ChannelFuture future;

    private RedisConfig redisConfig;

    public RedisServer(RedisConfig redisConfig) {
        this.redisConfig = redisConfig;
    }


    public void start() {

        checkConfig();
        try{
            bossGroup = new NioEventLoopGroup(1);
            workerGroup = new NioEventLoopGroup(Runtime.getRuntime().availableProcessors() * 2);

            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup,workerGroup).channel(NioServerSocketChannel.class).
                    childHandler(new RespInitializerHandler()).
                    option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT).
                    childOption(ChannelOption.SO_RCVBUF,BUFFER_SIZE).
                    childOption(ChannelOption.SO_SNDBUF,BUFFER_SIZE).
                    childOption(ChannelOption.SO_KEEPALIVE,true).
                    childOption(ChannelOption.ALLOCATOR,PooledByteBufAllocator.DEFAULT);

            future = bootstrap.bind(redisConfig.getHost(), redisConfig.getPort());

            future.channel().closeFuture().sync();
        }catch (Exception e){

        }
    }

    private void checkConfig() {
        if (this.redisConfig == null || Strings.isNullOrEmpty(this.redisConfig.getHost()) || this.redisConfig.getPort() < 5000) {
            throw new IllegalArgumentException("redisConfig is illegal please check config");
        }
    }
}
