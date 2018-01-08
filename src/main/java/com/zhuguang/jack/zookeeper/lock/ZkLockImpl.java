package com.zhuguang.jack.zookeeper.lock;

import java.util.concurrent.CountDownLatch;

import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.exception.ZkException;

public class ZkLockImpl extends ZkAbstractLock {
    
    private CountDownLatch cdl = null;
    
    @Override
    protected void waitforlock() {
        
        IZkDataListener listener = new IZkDataListener() {
            
            public void handleDataDeleted(String arg0) throws Exception {
                if (cdl != null) {
                    cdl.countDown();
                }
            }
            
            public void handleDataChange(String arg0, Object arg1)
                    throws Exception {
                // TODO Auto-generated method stub
                
            }
        };
        
        client.subscribeDataChanges(path, listener);
        
        if (client.exists(path)) {
            cdl = new CountDownLatch(1);
            try {
                cdl.await();
            }
            catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        client.unsubscribeDataChanges(path, listener);
        
    }
    
    @Override
    protected boolean tryLock() {
        try {
            client.createEphemeral(path);
            return true;
        }
        catch (ZkException e) {
            return false;
        }
    }
    
}
