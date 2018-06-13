package com.changan.changanproject.canmessage.figuremessage;

import com.changan.changanproject.canmessage.FigureData;

/**
 * Created by 张海逢 on 2017/12/6.
 * 外插充电需求电压
 */

public class BCUChrgUReqFigureData extends FigureData {
    public BCUChrgUReqFigureData() {
        super(0.1, 0, "V");
        datas[2] = Short.parseShort("F2",16);
        datas[3] = Short.parseShort("13",16);
    }


    public String getTitle(){
        return "外插充电需求电压";
    }
}
