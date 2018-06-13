package com.changan.changanproject.canmessage.figuremessage;

import com.changan.changanproject.canmessage.FigureData;

/**
 * Created by 张海逢 on 2017/12/6.
 * 电池荷电量
 */

public class BCUSOCFigureData extends FigureData {
    public BCUSOCFigureData() {
        super(0.1, 0, "%");
        datas[2] = Short.parseShort("F2",16);
        datas[3] = Short.parseShort("02",16);
    }


    public String getTitle(){
        return "电池荷电量";
    }
}
