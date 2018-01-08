package com.zhuguang.jack.zookeeper.lock;

import org.I0Itec.zkclient.ZkClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class ZkAbstractLock implements Lock {
    
    private static Logger logger = LoggerFactory.getLogger(ZkAbstractLock.class);
    
    private static String host = "localhost";
    
    private static String port = "2181";
    
    protected static String path = "/lock";
    
    protected ZkClient client = new ZkClient(host + ":" + port);
    
    public void lock() {
        
        if (tryLock()) {
            logger.info(Thread.currentThread().getName() + "get lock success！");
        }
        else {
            waitforlock();
            lock();
        }
    }
    
    protected abstract void waitforlock();
    
    protected abstract boolean tryLock();
    
    public void unlock() {
        client.close();
    }
    
}
