package com.changan.changanproject.canmessage.figuremessage;

import com.changan.changanproject.canmessage.FigureData;

/**
 * Created by 张海逢 on 2017/12/6.
 * 电池最大充电电压
 */

public class BCUMaxChrgUFigureData extends FigureData {
    public BCUMaxChrgUFigureData() {
        super(1, 0, "V");
        datas[2] = Short.parseShort("F2",16);
        datas[3] = Short.parseShort("12",16);
    }


    public String getTitle(){
        return "电池最大充电电压";
    }
}
