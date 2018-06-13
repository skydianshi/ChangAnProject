package com.changan.changanproject.canmessage.figuremessage;

import com.changan.changanproject.canmessage.FigureData;

/**
 * Created by 张海逢 on 2017/12/6.
 * 硬件版本
 */

public class BCUHwVersFigureData extends FigureData {
    public BCUHwVersFigureData() {
        super(1, 0, "");
        datas[2] = Short.parseShort("F2",16);
        datas[3] = Short.parseShort("1E",16);
    }


    public String getTitle(){
        return "硬件版本";
    }
}
