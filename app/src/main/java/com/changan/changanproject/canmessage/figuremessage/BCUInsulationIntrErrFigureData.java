package com.changan.changanproject.canmessage.figuremessage;

import com.changan.changanproject.canmessage.FigureData;

/**
 * Created by 张海逢 on 2017/12/6.
 * 电池内部绝缘故障
 */

public class BCUInsulationIntrErrFigureData extends FigureData {
    public BCUInsulationIntrErrFigureData() {
        super(1, 0, "");
        datas[2] = Short.parseShort("F2",16);
        datas[3] = Short.parseShort("0B",16);
    }


    public String getTitle(){
        return "电池内部绝缘故障";
    }
}
