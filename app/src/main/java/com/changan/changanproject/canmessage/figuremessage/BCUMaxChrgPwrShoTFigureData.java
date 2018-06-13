package com.changan.changanproject.canmessage.figuremessage;

import com.changan.changanproject.canmessage.FigureData;

/**
 * Created by 张海逢 on 2017/12/6.
 * 电池最大充电功率（短时）
 */

public class BCUMaxChrgPwrShoTFigureData extends FigureData {
    public BCUMaxChrgPwrShoTFigureData() {
        super(0.5, 0, "kw");
        datas[2] = Short.parseShort("F2",16);
        datas[3] = Short.parseShort("0E",16);
    }


    public String getTitle(){
        return "电池最大充电功率";
    }
}
