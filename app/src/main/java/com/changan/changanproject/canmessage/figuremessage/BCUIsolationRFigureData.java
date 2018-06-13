package com.changan.changanproject.canmessage.figuremessage;

import com.changan.changanproject.canmessage.FigureData;

/**
 * Created by 张海逢 on 2017/12/6.
 * 绝缘电阻
 */

public class BCUIsolationRFigureData extends FigureData {
    public BCUIsolationRFigureData() {
        super(1, 0, "kw");
        datas[2] = Short.parseShort("F2",16);
        datas[3] = Short.parseShort("1C",16);
    }


    public String getTitle(){
        return "绝缘电阻";
    }
}
