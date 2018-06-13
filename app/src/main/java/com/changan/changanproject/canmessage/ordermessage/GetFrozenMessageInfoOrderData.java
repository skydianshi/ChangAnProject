package com.changan.changanproject.canmessage.ordermessage;

import com.changan.changanproject.canmessage.OrderData;

/**
 * Created by 张海逢 on 2017/12/10.
 */

public class GetFrozenMessageInfoOrderData extends OrderData {
    public GetFrozenMessageInfoOrderData(){
        super();
    }

    public GetFrozenMessageInfoOrderData(String dtc){
        super();
        nID = Long.parseLong("07A0", 16);
        datas[0] = Short.parseShort("06",16);
        datas[1] = Short.parseShort("19",16);
        datas[2] = Short.parseShort("04",16);
        datas[3] = Short.parseShort(dtc.substring(0,2),16);
        datas[4] = Short.parseShort(dtc.substring(2,4),16);
        datas[5] = Short.parseShort(dtc.substring(4,6),16);
        datas[6] = 0;
        datas[7] = 0;
    }


}
