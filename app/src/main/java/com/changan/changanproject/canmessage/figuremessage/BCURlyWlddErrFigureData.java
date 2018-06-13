package com.changan.changanproject.canmessage.figuremessage;

import com.changan.changanproject.canmessage.FigureData;

/**
 * Created by 张海逢 on 2017/12/6.
 * 主继电器常闭故障
 */

public class BCURlyWlddErrFigureData extends FigureData {
    public BCURlyWlddErrFigureData() {
        super( 1, 0, "");
        datas[2] = Short.parseShort("F2",16);
        datas[3] = Short.parseShort("09",16);
    }


    public String getTitle(){
        return "主继电器常闭故障";
    }
}
