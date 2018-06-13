package com.changan.changanproject.canmessage.figuremessage;

import com.changan.changanproject.canmessage.FigureData;

/**
 * Created by 张海逢 on 2017/12/6.
 * 允许重新上高压电
 */

public class BCUPwrUpAllwFigureData extends FigureData {
    public BCUPwrUpAllwFigureData() {
        super( 1, 0, "");
        datas[2] = Short.parseShort("F2",16);
        datas[3] = Short.parseShort("06",16);
    }


    public String getTitle(){
        return "允许重新上高压电";
    }
}
