package com.changan.changanproject.canmessage.figuremessage;

import com.changan.changanproject.canmessage.FigureData;

/**
 * Created by 张海逢 on 2017/12/6.
 * 电池最低温度
 */

public class BCUMinBattTFigureData extends FigureData {
    public BCUMinBattTFigureData() {
        super( 1, -40, "℃");
        datas[2] = Short.parseShort("F2",16);
        datas[3] = Short.parseShort("1A",16);
    }


    public String getTitle(){
        return "电池最低温度";
    }
}
