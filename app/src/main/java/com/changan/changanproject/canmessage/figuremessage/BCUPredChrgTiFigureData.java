package com.changan.changanproject.canmessage.figuremessage;

import com.changan.changanproject.canmessage.FigureData;

/**
 * Created by 张海逢 on 2017/12/6.
 * 剩余充电时间
 */

public class BCUPredChrgTiFigureData extends FigureData {
    public BCUPredChrgTiFigureData() {
        super( 1, 0, "Min");
        datas[2] = Short.parseShort("F2",16);
        datas[3] = Short.parseShort("1B",16);
    }


    public String getTitle(){
        return "剩余充电时间";
    }
}
