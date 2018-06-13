package com.changan.changanproject.canmessage.figuremessage;

import com.changan.changanproject.canmessage.FigureData;

/**
 * Created by 张海逢 on 2017/12/6.
 * BCU充电过程故障信息
 */

public class BCUChrgErrInfoFigureData extends FigureData {
    public BCUChrgErrInfoFigureData() {
        super( 1, 0, "");
        datas[2] = Short.parseShort("F2",16);
        datas[3] = Short.parseShort("17",16);
    }


    public String getTitle(){
        return "BCU充电过程故障";
    }
}
