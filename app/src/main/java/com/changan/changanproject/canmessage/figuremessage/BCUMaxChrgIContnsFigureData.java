package com.changan.changanproject.canmessage.figuremessage;

import com.changan.changanproject.canmessage.FigureData;

/**
 * Created by 张海逢 on 2017/12/6.
 * 电池最大持续充电电流
 */

public class BCUMaxChrgIContnsFigureData extends FigureData {
    public BCUMaxChrgIContnsFigureData() {
        super(1, 0, "A");
        datas[2] = Short.parseShort("F2",16);
        datas[3] = Short.parseShort("11",16);
    }


    public String getTitle(){
        return "最大持续充电电流";
    }
}
