package com.changan.changanproject.constant;

import android.content.Context;

import com.changan.changanproject.R;
import com.changan.changanproject.bean.DtcMessage;
import com.changan.changanproject.canmessage.FigureData;
import com.changan.changanproject.canmessage.figuremessage.BCUBattIFigureData;
import com.changan.changanproject.canmessage.figuremessage.BCUBattUFigureData;
import com.changan.changanproject.canmessage.figuremessage.BCUFltRnkFigureData;
import com.changan.changanproject.canmessage.figuremessage.BCUHVILStsFigureData;
import com.changan.changanproject.canmessage.figuremessage.BCUMaxBattTFigureData;
import com.changan.changanproject.canmessage.figuremessage.BCUMaxChrgIContnsFigureData;
import com.changan.changanproject.canmessage.figuremessage.BCUMaxChrgPwrShoTFigureData;
import com.changan.changanproject.canmessage.figuremessage.BCUMinBattTFigureData;
import com.changan.changanproject.canmessage.figuremessage.BCUMinDchaIUFigureData;
import com.changan.changanproject.canmessage.figuremessage.BCUOperModFigureData;
import com.changan.changanproject.canmessage.figuremessage.BCUPredChrgTiFigureData;
import com.changan.changanproject.canmessage.figuremessage.BCUSOCFigureData;
import com.changan.changanproject.canmessage.figuremessage.OBCCPValueFigureData;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by 张海逢 on 2017/12/25.
 */

public class Constant {
    public static HashMap<String,DtcMessage> dtcDetails = new HashMap<>();
    public static boolean is_save = false;//保存CAN数据
    public static boolean is_send = false;//是否正在发送CAN数据
    public static boolean is_read = false;//是否更新CAN数据列表
    public static boolean is_readFrozenMessage = false;//是否正在检测冻结帧
    public static boolean is_connect = false;//连接wifi，获取socket通道并开始接收数据
    public static boolean is_sendOrder = false;//判断是否发送指令式CAN消息

    public static String saveFilePath = "changan.csv";
    public static long writeIndex = 0;

    public static ArrayList<FigureData> figureDatas = new ArrayList();

    public static String IP_ADDRESS = "192.168.0.178";
    public static int TARGET_PORT = 4001;
    public static int ERR_PORT = 5001;


    public Constant(Context context){
        dtcDetails.put("180009",new DtcMessage(context.getResources().getString(R.string.dtc_180009),context.getResources().getString(R.string.Meaning_180009),context.getResources().getString(R.string.faultType_180009),context.getResources().getString(R.string.faultAttribute_180009),context.getResources().getString(R.string.faultExplain_180009),context.getResources().getString(R.string.possibleCause_180009),context.getResources().getString(R.string.correctAction_180009)));
        dtcDetails.put("180109",new DtcMessage(context.getResources().getString(R.string.dtc_180109),context.getResources().getString(R.string.Meaning_180109),context.getResources().getString(R.string.faultType_180109),context.getResources().getString(R.string.faultAttribute_180109),context.getResources().getString(R.string.faultExplain_180109),context.getResources().getString(R.string.possibleCause_180109),context.getResources().getString(R.string.correctAction_180109)));
        dtcDetails.put("180209",new DtcMessage(context.getResources().getString(R.string.dtc_180209),context.getResources().getString(R.string.Meaning_180209),context.getResources().getString(R.string.faultType_180209),context.getResources().getString(R.string.faultAttribute_180209),context.getResources().getString(R.string.faultExplain_180209),context.getResources().getString(R.string.possibleCause_180209),context.getResources().getString(R.string.correctAction_180209)));
        dtcDetails.put("180309",new DtcMessage(context.getResources().getString(R.string.dtc_180309),context.getResources().getString(R.string.Meaning_180309),context.getResources().getString(R.string.faultType_180309),context.getResources().getString(R.string.faultAttribute_180309),context.getResources().getString(R.string.faultExplain_180309),context.getResources().getString(R.string.possibleCause_180309),context.getResources().getString(R.string.correctAction_180309)));
        dtcDetails.put("180409",new DtcMessage(context.getResources().getString(R.string.dtc_180409),context.getResources().getString(R.string.Meaning_180409),context.getResources().getString(R.string.faultType_180409),context.getResources().getString(R.string.faultAttribute_180409),context.getResources().getString(R.string.faultExplain_180409),context.getResources().getString(R.string.possibleCause_180409),context.getResources().getString(R.string.correctAction_180409)));
        dtcDetails.put("1ed364",new DtcMessage(context.getResources().getString(R.string.dtc_1ed364),context.getResources().getString(R.string.Meaning_1ed364),context.getResources().getString(R.string.faultType_1ed364),context.getResources().getString(R.string.faultAttribute_1ed364),context.getResources().getString(R.string.faultExplain_1ed364),context.getResources().getString(R.string.possibleCause_1ed364),context.getResources().getString(R.string.correctAction_1ed364)));
        dtcDetails.put("1ed482",new DtcMessage(context.getResources().getString(R.string.dtc_1ed482),context.getResources().getString(R.string.Meaning_1ed482),context.getResources().getString(R.string.faultType_1ed482),context.getResources().getString(R.string.faultAttribute_1ed482),context.getResources().getString(R.string.faultExplain_1ed482),context.getResources().getString(R.string.possibleCause_1ed482),context.getResources().getString(R.string.correctAction_1ed482)));
        dtcDetails.put("d30d87",new DtcMessage(context.getResources().getString(R.string.dtc_d30d87),context.getResources().getString(R.string.Meaning_d30d87),context.getResources().getString(R.string.faultType_d30d87),context.getResources().getString(R.string.faultAttribute_d30d87),context.getResources().getString(R.string.faultExplain_d30d87),context.getResources().getString(R.string.possibleCause_d30d87),context.getResources().getString(R.string.correctAction_d30d87)));
        dtcDetails.put("d30e87",new DtcMessage(context.getResources().getString(R.string.dtc_d30e87),context.getResources().getString(R.string.Meaning_d30e87),context.getResources().getString(R.string.faultType_d30e87),context.getResources().getString(R.string.faultAttribute_d30e87),context.getResources().getString(R.string.faultExplain_d30e87),context.getResources().getString(R.string.possibleCause_d30e87),context.getResources().getString(R.string.correctAction_d30e87)));
        dtcDetails.put("d30f87",new DtcMessage(context.getResources().getString(R.string.dtc_d30f87),context.getResources().getString(R.string.Meaning_d30f87),context.getResources().getString(R.string.faultType_d30f87),context.getResources().getString(R.string.faultAttribute_d30f87),context.getResources().getString(R.string.faultExplain_d30f87),context.getResources().getString(R.string.possibleCause_d30f87),context.getResources().getString(R.string.correctAction_d30f87)));
        dtcDetails.put("d31087",new DtcMessage(context.getResources().getString(R.string.dtc_d31087),context.getResources().getString(R.string.Meaning_d31087),context.getResources().getString(R.string.faultType_d31087),context.getResources().getString(R.string.faultAttribute_d31087),context.getResources().getString(R.string.faultExplain_d31087),context.getResources().getString(R.string.possibleCause_d31087),context.getResources().getString(R.string.correctAction_d31087)));
        dtcDetails.put("d31187",new DtcMessage(context.getResources().getString(R.string.dtc_d31187),context.getResources().getString(R.string.Meaning_d31187),context.getResources().getString(R.string.faultType_d31187),context.getResources().getString(R.string.faultAttribute_d31187),context.getResources().getString(R.string.faultExplain_d31187),context.getResources().getString(R.string.possibleCause_d31187),context.getResources().getString(R.string.correctAction_d31187)));
        dtcDetails.put("d31287",new DtcMessage(context.getResources().getString(R.string.dtc_d31287),context.getResources().getString(R.string.Meaning_d31287),context.getResources().getString(R.string.faultType_d31287),context.getResources().getString(R.string.faultAttribute_d31287),context.getResources().getString(R.string.faultExplain_d31287),context.getResources().getString(R.string.possibleCause_d31287),context.getResources().getString(R.string.correctAction_d31287)));
        dtcDetails.put("d31387",new DtcMessage(context.getResources().getString(R.string.dtc_d31387),context.getResources().getString(R.string.Meaning_d31387),context.getResources().getString(R.string.faultType_d31387),context.getResources().getString(R.string.faultAttribute_d31387),context.getResources().getString(R.string.faultExplain_d31387),context.getResources().getString(R.string.possibleCause_d31387),context.getResources().getString(R.string.correctAction_d31387)));
        dtcDetails.put("d31487",new DtcMessage(context.getResources().getString(R.string.dtc_d31487),context.getResources().getString(R.string.Meaning_d31487),context.getResources().getString(R.string.faultType_d31487),context.getResources().getString(R.string.faultAttribute_d31487),context.getResources().getString(R.string.faultExplain_d31487),context.getResources().getString(R.string.possibleCause_d31487),context.getResources().getString(R.string.correctAction_d31487)));
        dtcDetails.put("d31587",new DtcMessage(context.getResources().getString(R.string.dtc_d31587),context.getResources().getString(R.string.Meaning_d31587),context.getResources().getString(R.string.faultType_d31587),context.getResources().getString(R.string.faultAttribute_d31587),context.getResources().getString(R.string.faultExplain_d31587),context.getResources().getString(R.string.possibleCause_d31587),context.getResources().getString(R.string.correctAction_d31587)));
        dtcDetails.put("d31687",new DtcMessage(context.getResources().getString(R.string.dtc_d31687),context.getResources().getString(R.string.Meaning_d31687),context.getResources().getString(R.string.faultType_d31687),context.getResources().getString(R.string.faultAttribute_d31687),context.getResources().getString(R.string.faultExplain_d31687),context.getResources().getString(R.string.possibleCause_d31687),context.getResources().getString(R.string.correctAction_d31687)));


        figureDatas.add(new BCUBattIFigureData());
        figureDatas.add(new BCUBattUFigureData());
        figureDatas.add(new BCUFltRnkFigureData());
        figureDatas.add(new BCUOperModFigureData());
        figureDatas.add(new OBCCPValueFigureData());
        figureDatas.add(new BCUSOCFigureData());
        figureDatas.add(new BCUMaxBattTFigureData());
        figureDatas.add(new BCUMinBattTFigureData());
        figureDatas.add(new BCUHVILStsFigureData());
        figureDatas.add(new BCUPredChrgTiFigureData());
    }
}
