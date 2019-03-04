package com.atguigu.redis;

import redis.clients.jedis.Jedis;

/**
 * Created by lenovo on 2/28/2018.
 */
public class TT {
    public static void main(String[] args){
        Jedis jedis = new Jedis("192.168.1.15",6379);
        System.out.println(jedis.ping());
    }
}
