package com.changan.changanproject.canmessage.figuremessage;

import com.changan.changanproject.canmessage.FigureData;

/**
 * Created by 张海逢 on 2017/12/6.
 * CP状态
 */

public class CPStsFigureData extends FigureData {
    public CPStsFigureData() {
        super( 1, 0, "");
        datas[2] = Short.parseShort("F2",16);
        datas[3] = Short.parseShort("21",16);
    }


    public String getTitle(){
        return "CP状态";
    }
}
