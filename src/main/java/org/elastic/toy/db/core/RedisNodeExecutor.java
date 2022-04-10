package org.elastic.toy.db.core;

import org.elastic.toy.db.resp.RedisRequest;
import org.elastic.toy.db.resp.RedisResp;

/**
 * @author bazinga
 * 2022-4-10 09:37:30
 * redis请求节点处理器
 */
public interface RedisNodeExecutor {


    /**
     * 注册redis可以支持的命令集
     */
    void registerCommandProcessor();


    /**
     * 执行客户端传递过来的redis的命令
     * @param redisRequest
     * @return
     */
    RedisResp execute(RedisRequest redisRequest);
}
