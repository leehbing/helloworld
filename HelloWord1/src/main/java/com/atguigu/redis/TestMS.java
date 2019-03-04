package com.atguigu.redis;

import redis.clients.jedis.Jedis;

public class TestMS {
	public static void main(String[] args) {
		Jedis jedis_M = new Jedis("127.0.0.1",6379);
		Jedis jedis_S = new Jedis("127.0.0.1",6380);

		jedis_S.slaveof("127.0.0.1",6379);

		jedis_M.set("class","1122V2"); //主写

		String result = jedis_S.get("class"); //从读
		System.out.println(result);
	}
}
