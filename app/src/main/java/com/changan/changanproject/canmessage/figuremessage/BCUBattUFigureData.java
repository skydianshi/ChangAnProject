package com.changan.changanproject.canmessage.figuremessage;

import com.changan.changanproject.canmessage.FigureData;

/**
 * Created by 张海逢 on 2017/12/6.
 * 电池总电压
 */

public class BCUBattUFigureData extends FigureData {
    public BCUBattUFigureData() {
        super(0.1, 0, "V");
        datas[2] = Short.parseShort("F2",16);
        datas[3] = Short.parseShort("00",16);
    }


    public String getTitle(){
        return "电池总电压";
    }

}
