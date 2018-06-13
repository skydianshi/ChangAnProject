package com.changan.changanproject.canmessage.ordermessage;

import com.changan.changanproject.canmessage.OrderData;

/**
 * Created by 张海逢 on 2017/12/10.
 */

public class GetFrozenMessageIndexOrderData extends OrderData {
    public GetFrozenMessageIndexOrderData(){
        super();
        nID = Long.parseLong("07A0", 16);
        datas[0] = Short.parseShort("02",16);
        datas[1] = Short.parseShort("19",16);
        datas[2] = Short.parseShort("03",16);
        datas[3] = 0;
        datas[4] = 0;
        datas[5] = 0;
        datas[6] = 0;
        datas[7] = 0;
    }


}
