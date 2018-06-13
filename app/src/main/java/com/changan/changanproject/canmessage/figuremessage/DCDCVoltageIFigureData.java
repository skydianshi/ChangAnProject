package com.changan.changanproject.canmessage.figuremessage;

import com.changan.changanproject.canmessage.FigureData;

/**
 * Created by 张海逢 on 2017/12/6.
 * DCDC低压电流
 */

public class DCDCVoltageIFigureData extends FigureData {
    public DCDCVoltageIFigureData() {
        super(1, 0, "A");
        datas[2] = Short.parseShort("F2",16);
        datas[3] = Short.parseShort("28",16);
    }


    public String getTitle(){
        return "DCDC低压电流";
    }
}
