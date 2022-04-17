package org.elastic.toy.db.resp;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.elastic.toy.db.core.RedisNodeExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class RespConnectionHandler extends SimpleChannelInboundHandler<RedisRequest> {

    private static final Logger log = LoggerFactory.getLogger(RespConnectionHandler.class);

    private RedisNodeExecutor redisNodeExecutor;

    public RespConnectionHandler(boolean mpsc, RedisNodeExecutor redisNodeExecutor) {
        this.redisNodeExecutor = redisNodeExecutor;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RedisRequest redisRequest) throws Exception {
        log.info("redisMessage receive {}",redisRequest);

        ctx.channel().write(redisNodeExecutor.execute(redisRequest));
        ctx.flush();
    }
}
