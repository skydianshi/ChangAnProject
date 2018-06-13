package com.changan.changanproject.canmessage.figuremessage;

import com.changan.changanproject.canmessage.FigureData;

/**
 * Created by 张海逢 on 2017/12/6.
 * 标定数据版本
 */

public class BCUCalDataVersFigureData extends FigureData {
    public BCUCalDataVersFigureData() {
        super(1, 0, "");
        datas[2] = Short.parseShort("F2",16);
        datas[3] = Short.parseShort("1F",16);
    }


    public String getTitle(){
        return "标定数据版本";
    }
}
