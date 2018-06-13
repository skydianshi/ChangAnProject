package com.changan.changanproject.canmessage.figuremessage;

import com.changan.changanproject.canmessage.FigureData;

/**
 * Created by 张海逢 on 2017/12/6.
 * 外插充电需求电流
 */

public class BCUChrgIReqFigureData extends FigureData {
    public BCUChrgIReqFigureData() {
        super(0.1, 0, "A");
        datas[2] = Short.parseShort("F2",16);
        datas[3] = Short.parseShort("14",16);
    }


    public String getTitle(){
        return "外插充电需求电流";
    }
}
