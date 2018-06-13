package com.changan.changanproject.canmessage.figuremessage;

import com.changan.changanproject.canmessage.FigureData;

/**
 * Created by 张海逢 on 2017/12/6.
 * 碰撞状态
 */

public class BCUCrashStsFigureData extends FigureData {
    public BCUCrashStsFigureData() {
        super(1, 0, "");
        datas[2] = Short.parseShort("F2",16);
        datas[3] = Short.parseShort("08",16);
    }


    public String getTitle(){
        return "碰撞状态";
    }
}
