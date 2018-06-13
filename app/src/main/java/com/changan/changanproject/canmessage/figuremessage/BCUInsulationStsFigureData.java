package com.changan.changanproject.canmessage.figuremessage;

import com.changan.changanproject.canmessage.FigureData;

/**
 * Created by 张海逢 on 2017/12/6.
 * 绝缘状态
 */

public class BCUInsulationStsFigureData extends FigureData {
    public BCUInsulationStsFigureData() {
        super(1, 0, "");
        datas[2] = Short.parseShort("F2",16);
        datas[3] = Short.parseShort("0A",16);
    }


    public String getTitle(){
        return "绝缘状态";
    }
}
