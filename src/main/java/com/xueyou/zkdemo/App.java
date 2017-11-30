package com.xueyou.zkdemo;

import com.xueyou.zkdemo.zkUtils.CreateClient;
import com.xueyou.zkdemo.zkUtils.CuratorZkClientBridge;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.leader.LeaderSelector;
import org.apache.curator.framework.recipes.leader.LeaderSelectorListener;
import org.apache.curator.framework.recipes.leader.LeaderSelectorListenerAdapter;
import org.apache.zookeeper.KeeperException;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Hello world!
 */
public class App {
    public static String connectionString = "192.168.0.66:62181,192.168.0.66:62182,192.168.0.66:62183";
    public static List<String> res = new ArrayList<>();
    public static final int ID = 3;

    public static void main(String[] args) throws Exception {
        System.out.println("Hello World!");
        CuratorFramework curatorFramework = CreateClient.createSimple(connectionString);
        curatorFramework.start();
        //doSomething to zookeeper
        CuratorZkClientBridge curatorZkClientBridge = new CuratorZkClientBridge(curatorFramework);

//        System.out.println(getNode(curatorZkClientBridge, "/"));
        //master 选举
        LeaderSelectorListener listener = new LeaderSelectorListenerAdapter() {
            public void takeLeadership(CuratorFramework client) throws Exception {
                // this callback will get called when you are the leader
                // do whatever leader work you need to and only exit
                // this method when you want to relinquish leadership
                System.out.println("i am master" + "\t" + ID + "\t" + new SimpleDateFormat("HH:mm:ss").format(new Date()));
//                Thread.sleep(2000);
                Thread.sleep(Integer.MAX_VALUE);
            }
        };

        LeaderSelector selector = new LeaderSelector(curatorFramework, "/amaster", listener);
        selector.autoRequeue();  // not required, but this is behavior that you will probably expect
        selector.start();
        Thread.sleep(Integer.MAX_VALUE);
    }

    //递归获取所有zookeeper下的节点
    public static List<String> getNode(CuratorZkClientBridge curatorZkClientBridge, String parentNode) {
        try {
            List<String> tmpList = curatorZkClientBridge.getChildren(parentNode, false);
            for (String tmp : tmpList) {
                String childNode = parentNode.equals("/") ? parentNode + tmp : parentNode + "/" + tmp;
                res.add(childNode);
                getNode(curatorZkClientBridge, childNode);
            }
            return res;
        } catch (KeeperException e) {
            e.printStackTrace();
            return null;
        } catch (InterruptedException e) {
            e.printStackTrace();
            return null;
        }
    }

}
