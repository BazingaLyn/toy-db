package org.elastic.toy.db.resp;

import java.io.Serializable;

/**
 * @author bazinga
 * 2022-4-9 22:45:18
 */
public class RedisResp implements Serializable {

    private RedisTokenType type;

    private Object o;

    private static final String OK = "OK";


    public enum RedisTokenType {
        INTEGER,
        SIMPLE_STRING,
        STRING,
        ARRAY,
        ERROR,
        UNKNOWN
    }


}
