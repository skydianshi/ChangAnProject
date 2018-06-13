package com.changan.changanproject.canmessage.figuremessage;

import com.changan.changanproject.canmessage.FigureData;

/**
 * Created by 张海逢 on 2017/12/6.
 * 电池总电流
 */

public class BCUBattIFigureData extends FigureData {
    public BCUBattIFigureData() {
        super(0.1, -600, "A");
        datas[2] = Short.parseShort("F2",16);
        datas[3] = Short.parseShort("01",16);
    }


    public String getTitle(){
        return "电池总电流";
    }
}
