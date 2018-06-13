package com.changan.changanproject.canmessage.figuremessage;

import com.changan.changanproject.canmessage.FigureData;

/**
 * Created by 张海逢 on 2017/12/6.
 * 电池充电状态
 */

public class BCUChrgStsFigureData extends FigureData {
    public BCUChrgStsFigureData() {
        super(1, 0, "");
        datas[2] = Short.parseShort("F2",16);
        datas[3] = Short.parseShort("18",16);
    }


    public String getTitle(){
        return "电池充电状态";
    }
}
