package com.changan.changanproject.canmessage.figuremessage;

import com.changan.changanproject.canmessage.FigureData;

/**
 * Created by 张海逢 on 2017/12/6.
 * 电池充电模式
 */

public class BCUChrgModFigureData extends FigureData {
    public BCUChrgModFigureData() {
        super(1, 0, "");
        datas[2] = Short.parseShort("F2",16);
        datas[3] = Short.parseShort("16",16);
    }


    public String getTitle(){
        return "电池充电模式";
    }
}
