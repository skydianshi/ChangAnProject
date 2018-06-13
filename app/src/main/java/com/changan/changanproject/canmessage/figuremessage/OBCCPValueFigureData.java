package com.changan.changanproject.canmessage.figuremessage;

import com.changan.changanproject.canmessage.FigureData;

/**
 * Created by 张海逢 on 2017/12/6.
 * CP占空比
 */

public class OBCCPValueFigureData extends FigureData {
    public OBCCPValueFigureData() {
        super( 1, 0, "%");
        datas[2] = Short.parseShort("F2",16);
        datas[3] = Short.parseShort("20",16);
    }


    public String getTitle(){
        return "CP占空比";
    }
}
