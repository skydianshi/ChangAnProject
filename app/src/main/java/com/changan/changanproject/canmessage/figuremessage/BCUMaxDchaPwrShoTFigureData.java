package com.changan.changanproject.canmessage.figuremessage;

import com.changan.changanproject.canmessage.FigureData;

/**
 * Created by 张海逢 on 2017/12/6.
 * 电池最大放电功率（短时）
 */

public class BCUMaxDchaPwrShoTFigureData extends FigureData {
    public BCUMaxDchaPwrShoTFigureData() {
        super(0.5, 0, "kw");
        datas[2] = Short.parseShort("F2",16);
        datas[3] = Short.parseShort("0D",16);
    }


    public String getTitle(){
        return "电池最大放电功率";
    }
}
