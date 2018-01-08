package com.zhuguang.jack.jvm.lock;

import java.util.concurrent.CountDownLatch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OrderService implements Runnable {
    
    private static int count = 100;
    
    private static Logger logger = LoggerFactory.getLogger(OrderService.class);
    
    private OrderNumFactory onf = new OrderNumFactory();
    
    private static CountDownLatch cdl = new CountDownLatch(count);
    
    private static Object obj = new Object();
    
    public void run() {
        //        synchronized (obj) {
        createOrderNum();
        //        }
    }
    
    public void createOrderNum() {
        String orderNum = onf.createOrderNum();
        logger.info(Thread.currentThread().getName() + "创建了订单号：[" + orderNum
                + "]!");
    }
    
    public static void main(String[] args) {
        for (int i = 0; i < count; i++) {
            new Thread(new OrderService()).start();
            cdl.countDown();
        }
    }
    
}
