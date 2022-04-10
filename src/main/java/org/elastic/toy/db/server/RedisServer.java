package org.elastic.toy.db.server;

import com.google.common.base.Strings;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import org.elastic.toy.db.config.RedisConfig;

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
    }

    private void checkConfig() {
        if (this.redisConfig == null || Strings.isNullOrEmpty(this.redisConfig.getHost()) || this.redisConfig.getPort() < 5000) {
            throw new IllegalArgumentException("redisConfig is illegal please check config");
        }
    }
}
