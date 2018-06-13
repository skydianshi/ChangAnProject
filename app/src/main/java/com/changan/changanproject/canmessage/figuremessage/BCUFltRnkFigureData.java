package com.changan.changanproject.canmessage.figuremessage;

import com.changan.changanproject.canmessage.FigureData;

/**
 * Created by 张海逢 on 2017/12/6.
 * 电池故障等级
 */

public class BCUFltRnkFigureData extends FigureData {
    public BCUFltRnkFigureData() {
        super(1, 0, "");
        datas[2] = Short.parseShort("F2",16);
        datas[3] = Short.parseShort("03",16);
    }


    public String getTitle(){
        return "电池故障等级";
    }
}
