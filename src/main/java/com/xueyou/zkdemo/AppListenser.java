package com.xueyou.zkdemo;

import com.xueyou.zkdemo.zkUtils.CreateClient;
import com.xueyou.zkdemo.zkUtils.CuratorZkClientBridge;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.NodeCache;
import org.apache.curator.framework.recipes.cache.TreeCache;
import org.apache.curator.framework.recipes.cache.TreeCacheEvent;
import org.apache.curator.framework.recipes.cache.TreeCacheListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Hello world!
 */
public class AppListenser {
    public static String connectionString = "192.168.0.66:62181,192.168.0.66:62182,192.168.0.66:62183";
    public static List<String> res = new ArrayList<>();
    public static final int ID = 3;

    public static void main(String[] args) throws Exception {
        System.out.println("Hello World!");
        CuratorFramework curatorFramework = CreateClient.createSimple(connectionString);
        curatorFramework.start();
        //doSomething to zookeeper
        CuratorZkClientBridge curatorZkClientBridge = new CuratorZkClientBridge(curatorFramework);


        final TreeCache treeCache = new TreeCache(curatorFramework, "/test");
        treeCache.getListenable().addListener(new TreeCacheListener() {
            @Override
            public void childEvent(CuratorFramework curatorFramework, TreeCacheEvent treeCacheEvent) throws Exception {
                System.out.println(treeCacheEvent.getType());
                System.out.println(treeCacheEvent.getData().getPath());
                System.out.println(new String(treeCacheEvent.getData().getData()));
            }
        });
        treeCache.start();
        ;
        Thread.sleep(Integer.MAX_VALUE);
    }

}
