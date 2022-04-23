package org.elastic.toy.db.resp;

import org.elastic.toy.db.config.RedisConfig;
import org.elastic.toy.db.server.RedisServer;
import org.junit.Test;

/**
 * @author bazinga
 * 2022-4-23 09:36:06
 */
public class RedisRespTest {


    @Test
    public void testRESP(){

        RedisConfig redisConfig = new RedisConfig();
        redisConfig.setHost("localhost");
        redisConfig.setPort(12345);
        redisConfig.setRedisNodeExecutor(new MockRedisNodeExecutor());

        RedisServer redisServer = new RedisServer(redisConfig);
        redisServer.start();
    }
}
