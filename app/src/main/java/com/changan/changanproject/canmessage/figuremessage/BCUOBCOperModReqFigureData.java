package com.changan.changanproject.canmessage.figuremessage;

import com.changan.changanproject.canmessage.FigureData;

/**
 * Created by 张海逢 on 2017/12/6.
 * OBC工作模式请求
 */

public class BCUOBCOperModReqFigureData extends FigureData {
    public BCUOBCOperModReqFigureData() {
        super( 1, 0, "");
        datas[2] = Short.parseShort("F2",16);
        datas[3] = Short.parseShort("05",16);
    }


    public String getTitle(){
        return "OBC工作模式请求";
    }
}
