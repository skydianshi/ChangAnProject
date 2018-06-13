package cn.zlg.www;

public class ErrData {

	public byte nCmdValue; // 错误码
	public long nCount; // 错误计数值
	public long nTime; // 格林威尔时间(1970-1-1 0时)到接收到错误信息的毫秒数

	public ErrData() {
		super();
		// TODO Auto-generated constructor stub
	}

	public ErrData(ErrData other) {
		nCmdValue = other.nCmdValue;
		nCount = other.nCount;
		nTime = other.nTime;
	}
}
