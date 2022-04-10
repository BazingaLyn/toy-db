package org.elastic.toy.db.config;

/**
 * @author bazinga
 * 2022-4-10 09:36:00
 */
public class RedisConfig implements Config {

    /**
     * redis启动的端口
     */
    private String host;


    /**
     * redis启动的端口
     */
    private int port;


    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
}
