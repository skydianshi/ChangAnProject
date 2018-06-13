package com.changan.changanproject.canmessage.figuremessage;

import com.changan.changanproject.canmessage.FigureData;

/**
 * Created by 张海逢 on 2017/12/6.
 * BCU工作模式
 */

public class BCUOperModFigureData extends FigureData {
    public BCUOperModFigureData() {
        super(1, 0, "");
        datas[2] = Short.parseShort("F2",16);
        datas[3] = Short.parseShort("04",16);
    }


    public String getTitle(){
        return "BCU工作模式";
    }
}
