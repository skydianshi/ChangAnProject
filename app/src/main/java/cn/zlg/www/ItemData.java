package cn.zlg.www;

import java.util.Arrays;

public class ItemData extends FrmData {

	public long interval;// 分类显示下，同ID帧的时间间隔
	public int count = 1;// 分类显示下，同ID帧的数目
	public boolean bRecv;

	public static final int MILLIS_PER_S = 1000;
	public static final int MILLIS_PER_MIN = MILLIS_PER_S * 60;
	public static final int MILLIS_PER_HOUR = MILLIS_PER_MIN * 60;
	public static final int MILLIS_PER_DAY = MILLIS_PER_HOUR * 60;
	public static final int MILLIS_PER_60DAY = MILLIS_PER_DAY * 60;

	public ItemData() {
		super();
		// TODO Auto-generated constructor stub
	}

	public ItemData(ItemData other) {
		super(other);
		// TODO Auto-generated constructor stub
		count = other.count;
		bRecv = other.bRecv;
		interval = other.interval;
	}
	
	public void copyOf(ItemData other) {
		nID = other.nID;
		nSendType = other.nSendType;
		bExtend = other.bExtend;
		bRemote = other.bRemote;
		nDataLen = other.nDataLen;
		datas = Arrays.copyOf(other.datas, other.datas.length);
		nTime = other.nTime;
		count = other.count;
		bRecv = other.bRecv;
		interval = other.interval;
	}

	public void makeInterval(long nTime) {
		interval = this.nTime - nTime;
	}

	public String getIntervalString() {
		if (count > 1) {
			if (interval <= MILLIS_PER_MIN) {
				return interval + "ms";
			} else if (interval <= MILLIS_PER_HOUR) {
				return interval / MILLIS_PER_MIN + "min";
			} else if (interval <= MILLIS_PER_DAY) {
				return interval / MILLIS_PER_HOUR + "h";
			} else if (interval <= MILLIS_PER_60DAY) {
				return interval / MILLIS_PER_DAY + "d";
			} else {
				return ">60d";
			}
		}
		return "";
	}
	
	public void setStatisticsInfo(ItemData other) {
		makeInterval(other.nTime);
		count = other.count + 1;
	}

	@Override
	public boolean equals(Object o) {
		// TODO Auto-generated method stub
		ItemData itemData = (ItemData) o;
		if (nID == itemData.nID) {
			setStatisticsInfo(itemData);
			return true;
		}
		return false;
	}
}
