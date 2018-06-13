package com.changan.changanproject.canmessage.figuremessage;

import com.changan.changanproject.canmessage.FigureData;

/**
 * Created by 张海逢 on 2017/12/6.
 * DCDC低压电压
 */

public class DCDCVoltageUFigureData extends FigureData {
    public DCDCVoltageUFigureData() {
        super(0.125, 0, "V");
        datas[2] = Short.parseShort("F2",16);
        datas[3] = Short.parseShort("27",16);
    }


    public String getTitle(){
        return "DCDC低压电压";
    }
}
