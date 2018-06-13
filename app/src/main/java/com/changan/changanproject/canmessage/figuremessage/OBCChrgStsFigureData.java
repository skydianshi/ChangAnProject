package com.changan.changanproject.canmessage.figuremessage;

import com.changan.changanproject.canmessage.FigureData;

/**
 * Created by 张海逢 on 2017/12/6.
 * 充电机工作状态
 */

public class OBCChrgStsFigureData extends FigureData {
    public OBCChrgStsFigureData() {
        super(1, 0, "");
        datas[2] = Short.parseShort("F2",16);
        datas[3] = Short.parseShort("22",16);
    }


    public String getTitle(){
        return "充电机工作状态";
    }
}
