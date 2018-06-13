package com.changan.changanproject.canmessage.figuremessage;

import com.changan.changanproject.canmessage.FigureData;

/**
 * Created by 张海逢 on 2017/12/6.
 * 充电机交流端实时输入电压
 */

public class OBCChrgInpAcUFigureData extends FigureData {
    public OBCChrgInpAcUFigureData() {
        super( 1, 0, "V");
        datas[2] = Short.parseShort("F2",16);
        datas[3] = Short.parseShort("23",16);
    }


    public String getTitle(){
        return "充电机输入电压";
    }
}
