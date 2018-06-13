package com.changan.changanproject.canmessage;

import cn.zlg.www.FrmData;
import cn.zlg.www.ItemData;

/**
 * Created by 张海逢 on 2018/1/2.
 */

public class OrderData extends ItemData {

    public OrderData(){
        nSendType = 1;
        bExtend = false;
        bRemote = false;
        nDataLen = 8;
        nTime = System.currentTimeMillis();
        bRecv = false;
    }
}
