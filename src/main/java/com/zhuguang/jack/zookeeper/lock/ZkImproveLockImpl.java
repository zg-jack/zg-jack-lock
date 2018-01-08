package com.zhuguang.jack.zookeeper.lock;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import org.I0Itec.zkclient.IZkDataListener;

public class ZkImproveLockImpl extends ZkAbstractLock {
    
    private String beforePath;
    
    private String currentPath;
    
    private CountDownLatch latch = null;
    
    public ZkImproveLockImpl() {
        if (!this.client.exists(path)) {
            this.client.createPersistent(path, "zg-jack");
        }
    }
    
    @Override
    protected void waitforlock() {
        IZkDataListener listener = new IZkDataListener() {
            
            public void handleDataDeleted(String arg0) throws Exception {
                if (latch != null) {
                    latch.countDown();
                }
            }
            
            public void handleDataChange(String arg0, Object arg1)
                    throws Exception {
                // TODO Auto-generated method stub
                
            }
        };
        
        client.subscribeDataChanges(beforePath, listener);
        if (client.exists(beforePath)) {
            latch = new CountDownLatch(1);
            try {
                latch.await();
            }
            catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        client.unsubscribeDataChanges(beforePath, listener);
    }
    
    @Override
    protected boolean tryLock() {
        if (currentPath == null || currentPath.length() <= 0) {
            currentPath = this.client.createEphemeralSequential(path + "/",
                    "zg-jack");
        }
        List<String> childrens = this.client.getChildren(path);
        Collections.sort(childrens);
        if (currentPath.equals(path + "/" + childrens.get(0))) {
            return true;
        }
        else {
            int wz = Collections.binarySearch(childrens,
                    currentPath.substring(6));
            beforePath = path + "/" + childrens.get(wz - 1);
        }
        return false;
    }
}
