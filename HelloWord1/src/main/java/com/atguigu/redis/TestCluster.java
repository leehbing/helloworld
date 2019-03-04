package com.atguigu.redis;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.*;

import java.util.*;

/**
 * Created by lenovo on 2/23/2018.
 */
public class TestCluster {
    private final static Logger logger = LoggerFactory.getLogger(TestCluster.class);
    public static void main(String[] args){
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxTotal(20);
        poolConfig.setMaxIdle(1);
        poolConfig.setMaxWaitMillis(500);
        Set<HostAndPort> nodes = new LinkedHashSet<HostAndPort>();
        nodes.add(new HostAndPort("192.168.1.12",7000));   //redis集群的hosts和端口
        nodes.add(new HostAndPort("192.168.1.12",5005));
        nodes.add(new HostAndPort("192.168.1.12",5005));
        nodes.add(new HostAndPort("66.0.43.86",5006));
        nodes.add(new HostAndPort("66.0.43.87",5006));
        nodes.add(new HostAndPort("66.0.43.88",5006));
        JedisCluster cluster = new JedisCluster(nodes,poolConfig);


        System.out.println("=====================");
        System.out.println(cluster.ping());
//        System.out.println(cluster.hkeys("Financial-01010199"));
//        System.out.println(cluster.hmget("Financial-01010199","Top1","Top2"));
//        cluster.set("CSH_userid1","eventid1,eventid2,eventid3,eventid3");
//        cluster.set("RJ00300FA111","0102111|0.1|30000");
//        cluster.set("1800901FC890","1014000|0.02|20000");
//        System.out.println(cluster.get("RJ00300FA111"));
//        System.out.println(cluster.get("1800901FC890"));
//
//        System.out.println(cluster.hkeys("FINACIAL_PRODUCT"));
//        System.out.println(cluster.hgetAll("FINACIAL_PRODUCT"));


//        TreeSet<String> keys = clusterKeys(cluster,"Product*");
//        TreeSet<String> keys = clusterKeys(cluster,"CSH_*");
//        System.out.println("xxx===>"+cluster.hgetAll("Product_1163055312"));
//        System.out.println(keys);
//        for(Iterator<String> iterator = keys.iterator(); iterator.hasNext();){
//            String k = iterator.next();
//            System.out.println(k+"==>"+cluster.get(k));
//        }


        try{
            cluster.close();

        }catch (Exception e){
            e.printStackTrace();
        }

    }

    /**
     * JedisCluster没有keys的API，采用如下方法可实现类似于keys的遍历
     * @param jedisCluster
     * @param pattern
     * @return
     */
    public static TreeSet<String> clusterKeys(JedisCluster jedisCluster, String pattern){
        logger.debug("Start getting keys...");
        TreeSet<String> keys = new TreeSet<>();
        Map<String, JedisPool> clusterNodes = jedisCluster.getClusterNodes();
        for(String k : clusterNodes.keySet()){
            logger.debug("Getting keys from: {}", k);
            JedisPool jp = clusterNodes.get(k);
            Jedis connection = jp.getResource();
            try {
                keys.addAll(connection.keys(pattern));
            } catch(Exception e){
                logger.error("Getting keys error: {}", e);
            } finally{
                logger.debug("Connection closed.");
                connection.close();//用完一定要close这个链接！！！
            }
        }
        logger.debug("Keys gotten!");
        return keys;
    }

}
