package com.zhuguang.jack.jvm.lock;

import java.text.SimpleDateFormat;
import java.util.Date;

public class OrderNumFactory {
    
    private static int i = 0;
    
    public static String createOrderNum() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss-");
        return sdf.format(new Date()) + ++i;
    }
}
