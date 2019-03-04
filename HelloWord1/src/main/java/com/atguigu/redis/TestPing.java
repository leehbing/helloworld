package com.atguigu.redis;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;

import java.util.Iterator;
import java.util.Set;

public class TestPing {
    public static void main(String[] args) {
        String[] hosts = {"66.0.43.86", "66.0.43.88", "66.0.43.87", "66.0.43.86", "66.0.43.87", "66.0.43.88"};
        int[] ports = {5005, 5005, 5005, 5006, 5006, 5006};
//        String[] hosts = {"66.0.43.86"};
//        int[] ports = {5006};
        Jedis jedis = null;
        for (int i = 0; i < 6; i++) {
            jedis = new Jedis(hosts[i], ports[i]);
            System.out.println(hosts[i] + ":" + ports[i]);
            System.out.println(jedis.ping());
            System.out.println(jedis.get("yhpdqbgnzj_120180423102659005003004"));
            Set<String> keys = jedis.keys("yh*");

            for (Iterator iterator = keys.iterator(); iterator.hasNext(); ) {
                String key = (String) iterator.next();
                System.out.println("key=="+key );
//                jedis.del(key);
            }
        }


    }
}
