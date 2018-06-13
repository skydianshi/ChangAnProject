package cn.zlg.www;

public class ControlCAN {
	/*
	 * 用于初始化TCP服务器端 nPort:端口号 ipAddrErrNotice:错误通知服务器的ip地址 nPortErrNotice:错误通知端口
	 */
	public native boolean initTCPServer(int nPort, String ipAddrErrNotice,
			int nPortErrNotice);

	/*
	 * 用于初始化TCP客户端 ipAddr:要连接的服务器的ip地址，例如"192.168.27.22" nPort: 要连接的服务器的端口号
	 * nPortErrNotice:错误通知端口
	 */
	public native boolean initTCPClient(String ipAddr, int nPort, int nPortErrNotice);

	/**
	 * 用于初始化UDP服务器端，UDP只作为服务器端，与特定地址端口的客户端通信 nPortLocal:服务器端端口号
	 * ipAddr:连接的客户端的ip地址 nPort:连接的客户端的端口号 nPortErrNotice:错误通知端口
	 * */
	public native boolean initUDP(int nPortLocal, String ipAddr, int nPort,
								  int nPortErrNotice);

	/*
	 * 返回连接状态，true为连接，false为非连接
	 */
	public native boolean isConnected();

	/*
	 * 缓冲区当前的帧数目
	 */
	public native int getReceiveNum();

	/*
	 * 获取帧数据，注意初始化数组 返回值：实际接收到的帧数目
	 */
	public native int recv(FrmData[] objs, int nLen);

	/*
	 * 发送帧数据 返回值：是否成功发送
	 */
	public native boolean send(FrmData[] objs, int nLen);

	/*
	 * 获取错误信息，返回true表明读取成功，否则失败
	 */
	public native boolean readErrInfo(ErrData errData);

	/*
	 * 清空缓冲区的帧数据
	 */
	public native void clearBuffer();

	/*
	 * 断开连接
	 */
	public native boolean close();
}
