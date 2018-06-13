package com.changan.changanproject.canmessage.figuremessage;

import com.changan.changanproject.canmessage.FigureData;

/**
 * Created by 张海逢 on 2017/12/6.
 * 高压互锁状态
 */

public class BCUHVILStsFigureData extends FigureData {
    public BCUHVILStsFigureData() {
        super(1, 0, "");
        datas[2] = Short.parseShort("F2",16);
        datas[3] = Short.parseShort("07",16);
    }


    public String getTitle(){
        return "高压互锁状态";
    }
}
