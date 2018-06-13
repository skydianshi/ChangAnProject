package com.changan.changanproject.canmessage.vesionCommand;

import com.changan.changanproject.canmessage.OrderData;

/**
 * Created by 张海逢 on 2017/12/10.
 */

public class SupplierOrderData extends OrderData {
    public SupplierOrderData(){
        super();
        nID = Long.parseLong("07A0", 16);
        datas[0] = Short.parseShort("03",16);
        datas[1] = Short.parseShort("22",16);
        datas[2] = Short.parseShort("F1",16);
        datas[3] = Short.parseShort("A0",16);
        datas[4] = 0;
        datas[5] = 0;
        datas[6] = 0;
        datas[7] = 0;
    }


}
