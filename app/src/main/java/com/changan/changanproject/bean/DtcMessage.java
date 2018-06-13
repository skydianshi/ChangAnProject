package com.changan.changanproject.bean;

import com.changan.changanproject.constant.Constant;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 张海逢 on 2017/12/21.
 */

public class DtcMessage {
    private String dtc;
    private String dtcMeaning;//dtc含义



    private String faultType;//故障类型描述
    private String faultsAttribute;//故障属性
    private String faultExplain;//故障说明
    private String possibleCauses;//可能故障原因
    private String correctiveAction;//维修建议
    private boolean has_frozenMessage;
    private List<String> frozenMessages;

    public DtcMessage(String dtc){
        has_frozenMessage = false;
        frozenMessages = new ArrayList<String>();
        this.dtc = dtc;
        try {
            dtcMeaning = Constant.dtcDetails.get(dtc).getDtcMeaning();
        }catch (NullPointerException e){
            dtcMeaning = "暂无故障含义说明";
        }
        try {
            faultsAttribute = Constant.dtcDetails.get(dtc).getFaultsAttribute();
        }catch (NullPointerException e){
            faultsAttribute = "暂无故障属性";
        }
        try {
            faultExplain = Constant.dtcDetails.get(dtc).getFaultExplain();
        }catch (NullPointerException e){
            faultExplain = "暂无故障说明";
        }
        try {
            possibleCauses = Constant.dtcDetails.get(dtc).getPossibleCauses();
        }catch (NullPointerException e){
            possibleCauses = "暂未找到可能故障原因";
        }
        try {
            faultType = Constant.dtcDetails.get(dtc).getFaultType();
        }catch (NullPointerException e){
            faultType = "暂无故障类型描述";
        }
        try {
            correctiveAction = Constant.dtcDetails.get(dtc).getCorrectiveAction();
        }catch (NullPointerException e){
            correctiveAction = "暂无维修建议";
        }
    }

    public DtcMessage(String dtc, String dtcMeaning, String faultType, String faultsAttribute,  String faultExplain, String possibleCauses, String correctiveAction) {
        this.correctiveAction = correctiveAction;
        this.faultType = faultType;
        this.dtc = dtc;
        this.dtcMeaning = dtcMeaning;
        this.faultExplain = faultExplain;
        this.faultsAttribute = faultsAttribute;
        this.possibleCauses = possibleCauses;
    }

    public String getFaultsAttribute() {
        return faultsAttribute;
    }

    public void setFaultsAttribute(String faultsAttribute) {
        this.faultsAttribute = faultsAttribute;
    }

    public String getFaultType() {
        return faultType;
    }

    public void setFaultType(String faultType) {
        this.faultType = faultType;
    }

    public String getCorrectiveAction() {
        return correctiveAction;
    }

    public void setCorrectiveAction(String correctiveAction) {
        this.correctiveAction = correctiveAction;
    }


    public String getDtc() {
        return dtc;
    }

    public void setDtc(String dtc) {
        this.dtc = dtc;
    }

    public String getDtcMeaning() {
        return dtcMeaning;
    }

    public void setDtcMeaning(String dtcMeaning) {
        this.dtcMeaning = dtcMeaning;
    }

    public String getFaultExplain() {
        return faultExplain;
    }

    public void setFaultExplain(String faultExplain) {
        this.faultExplain = faultExplain;
    }

    public List<String> getFrozenMessages() {
        return frozenMessages;
    }

    public void setFrozenMessages(List<String> frozenMessages) {
        this.frozenMessages = frozenMessages;
    }

    public boolean isHas_frozenMessage() {
        return has_frozenMessage;
    }

    public void setHas_frozenMessage(boolean has_frozenMessage) {
        this.has_frozenMessage = has_frozenMessage;
    }

    public String getPossibleCauses() {
        return possibleCauses;
    }

    public void setPossibleCauses(String possibleCauses) {
        this.possibleCauses = possibleCauses;
    }

}
