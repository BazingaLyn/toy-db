package org.elastic.toy.db.resp;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import org.elastic.toy.db.core.RedisNodeExecutor;

/**
 * @author bazinga
 * 2022-4-15 15:11:42
 */
public class RespInitializerHandler extends ChannelInitializer<SocketChannel> {

    private RedisNodeExecutor redisNodeExecutor;


    public RespInitializerHandler(RedisNodeExecutor redisNodeExecutor){
        this.redisNodeExecutor = redisNodeExecutor;
    }

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        socketChannel.pipeline().addLast(new RedisEncoder());
        socketChannel.pipeline().addLast(new RedisDecoder());
        socketChannel.pipeline().addLast(new RespConnectionHandler(true,redisNodeExecutor));


    }
}
