package com.changan.changanproject.canmessage.figuremessage;

import com.changan.changanproject.canmessage.FigureData;

/**
 * Created by 张海逢 on 2017/12/6.
 * DCDC工作模式
 */

public class DCDCOperModeFigureData extends FigureData {
    public DCDCOperModeFigureData() {
        super(1, 0, "");
        datas[2] = Short.parseShort("F2",16);
        datas[3] = Short.parseShort("29",16);
    }


    public String getTitle(){
        return "DCDC工作模式";
    }
}
