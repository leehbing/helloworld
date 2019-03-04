package com.it18zhang.zookeeper.test;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Stat;

import java.util.List;

/**
 * Created by hongbing.li on 2017/8/21.
 */
public class App {

    //        String connStrPair = "s1:2181,s2:2181,s3:2181";
    private static final String connStrPair = "192.168.1.122:2181";

    public static void main(String[] args) throws Exception {
//        createPath();
        testWatcher();

    }

    private static void connZookeeper() throws Exception {

        ZooKeeper zk = new ZooKeeper(connStrPair, 2000, null);
        Stat stat = new Stat();
        byte[] bytes = zk.getData("/root", null, stat);   //将节点/root的状态放入stat，关联信息在bytes中
        System.out.println(new String(bytes));
    }

    private static void createPath() throws Exception {  //创建节点
        ZooKeeper zk = new ZooKeeper(connStrPair, 2000, null);
        String path = "/root/s1";
        String retPath = zk.create(path, "s1_data".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        System.out.println(retPath);
    }

    private static void deletePath() throws Exception {  //删除节点
        ZooKeeper zk = new ZooKeeper(connStrPair, 2000, null);
        String path = "/root/s1";
        //version等价于数据库的乐观锁
        zk.delete("root/s1", 2);
    }
    private static void getData()throws Exception{  //设置数据
        ZooKeeper zk = new ZooKeeper(connStrPair,2000,null);
        Stat s = zk.setData("root/s1","zzzz".getBytes(),0);
        System.out.println(s.getVersion());
    }
    private static void getChildren()throws Exception{  //获取children
        ZooKeeper zk = new ZooKeeper(connStrPair,2000,null);
        List<String> list = zk.getChildren("/root",null);
        System.out.println(list);
    }

    private static void testWatcher()throws Exception{  //watch,观察者模式
        Watcher w = new Watcher(){
            @Override
            public void process(WatchedEvent event) {
                System.out.println("有事情发生"+event.getType());
            }
        };

        ZooKeeper zk = new ZooKeeper(connStrPair,2000,w);//构造时会回调一次watch
        zk.getData("/root/s1",w,null);
        zk.setData("/root/s1","xxxx".getBytes(),1);//再触发一次watch
        while(true){
            Thread.sleep(5000);
        }
    }
}
