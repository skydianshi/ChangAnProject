package cn.zlg.www;

import java.util.Arrays;

public class FrmData {
	public long nID;
	public byte nSendType;
	public boolean bExtend;
	public boolean bRemote;
	public byte nDataLen;
	public short[] datas;
	public long nTime;// 格林威尔时间(1970-1-1 0时)到接收到帧数据的毫秒数

	public FrmData() {
		super();
		datas = new short[8];
	}

	public FrmData(FrmData other) {
		super();
		nID = other.nID;
		nSendType = other.nSendType;
		bExtend = other.bExtend;
		bRemote = other.bRemote;
		nDataLen = other.nDataLen;
		datas = Arrays.copyOf(other.datas, other.datas.length);
		nTime = other.nTime;
	}
}
