package com.changan.changanproject.canmessage;

import cn.zlg.www.ItemData;

/**
 * Created by 张海逢 on 2018/1/2.
 */

public class FigureData extends ItemData {
    private double resolution = 1;
    private int offset = 0;
    private String unit = "";

    public FigureData(double resolution, int offset, String unit){
        this.offset = offset;
        this.unit = unit;
        this.resolution = resolution;
        nID = Long.parseLong("07A0", 16);
        nSendType = 1;
        bExtend = false;
        bRemote = false;
        nDataLen = 8;
        nTime = System.currentTimeMillis();
        bRecv = false;
        datas[0] = Short.parseShort("03",16);
        datas[1] = Short.parseShort("22",16);
        datas[4] = 0;
        datas[5] = 0;
        datas[6] = 0;
        datas[7] = 0;
    }

    public String getResult(){
        double result = (datas[4]*256+datas[5]) * resolution + offset;
        return String.valueOf(result)+unit;
    }

    public String getTitle(){
        return null;
    }

    public String getDid(){
        if(Integer.toHexString(datas[3]).length()==1){
            return Integer.toHexString(datas[2])+"0"+Integer.toHexString(datas[3]);
        }
        return Integer.toHexString(datas[2])+Integer.toHexString(datas[3]);
    }
}
