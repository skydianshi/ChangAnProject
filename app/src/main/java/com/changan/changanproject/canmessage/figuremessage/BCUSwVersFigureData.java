package com.changan.changanproject.canmessage.figuremessage;

import com.changan.changanproject.canmessage.FigureData;

/**
 * Created by 张海逢 on 2017/12/6.
 * 软件版本
 */

public class BCUSwVersFigureData extends FigureData {
    public BCUSwVersFigureData() {
        super( 1, 0, "");
        datas[2] = Short.parseShort("F2",16);
        datas[3] = Short.parseShort("1D",16);
    }


    public String getTitle(){
        return "软件版本";
    }
}
