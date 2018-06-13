#ifndef _FRMMANAGER_H_
#define _FRMMANAGER_H_
#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <netdb.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <arpa/inet.h>
#include <pthread.h>
#include <errno.h>
#include <list>
#include <time.h>
#include <string.h>
#include "ComDef.h"
#include "FdSets.h"
#include "AutoLock.h"
#include "CycleBuffer.h"

const int MAX_FRM_COUNT = 130000;
const int FRM_COUNT_RECV = 100;
typedef CCycleBuffer<CANFRAMEEx> RECVFRMSET;
typedef CCycleBuffer<CANFRAME> SENDFRMSET;
typedef CCycleBuffer<CMD_ERR_NOTICEEx> ERRSET;

static CANFRAME g_frms[FRM_COUNT_RECV+1];
static CANFRAME g_frm_tmp;
static int g_len_offset = 0;
static int g_len_to_read;
static int g_frm_size = sizeof(CANFRAME);

//处理接收CANFRAME部分的情况
int recv2(int s, char** data, int nLen, int flags)
{
	*data = reinterpret_cast<char*>(&g_frms[1]);
	g_len_to_read = nLen > FRM_COUNT_RECV ? FRM_COUNT_RECV : nLen;
	int len_read = ::recv(s, *data, g_len_to_read, flags);
	if (len_read > 0 && g_len_offset != 0)
	{
		*data -= g_len_offset;
		memcpy(*data, &g_frm_tmp, g_len_offset);
		len_read += g_len_offset;
		g_len_offset = 0;
	}
	if (len_read > 0 && len_read % g_frm_size != 0)
	{
		g_len_offset = len_read - len_read / g_frm_size * g_frm_size;
		len_read -= g_len_offset;
		memcpy(&g_frm_tmp, &((*data)[len_read]), g_len_offset);
	}
	return len_read;
}

class CFrmManager {
public:
	CFrmManager(void);
	virtual ~CFrmManager(void);
public:
	int CreateTcpServer(int nPort);
	int CreateTcpClient(const char* pIpAddr, int nPort);
	int CreateUdpServer(int nPort);
	bool InitTCPServer(int nServer);
	bool InitTCPClient(int nClient);
	bool InitTCPClientErr(int nClient);
	bool InitUDPServer(int nServer, int nWorkPort, const char* pIpAddr);
	void StopThread();
	void AddToSendList(const CANFRAME& frm);
	int GetFrmsRecv(CANFRAMEEx* pFrms, int nLen);
	int GetFrmsSend(CANFRAME* pFrms, int nLen);
	int GetErrorInfo(CMD_ERR_NOTICEEx* pErrInfos, int nLen);
	void MakeSendSet(CFdSet& allSet, const fd_set& writeSet, CFdSet& sendSet);
	bool SendFrms(PCANFRAME pFrms, int nCount, int nSocket);
	void ClearBuffer();
	int GetRecvNum();
	bool IsConnected();
	long long GetCurTime();
	void ClearThread();
private:
	static void* startTCPServer(void* arg);
	static void* startTCPClientRecv(void* arg);
	static void* startErrRecv(void* arg);
	static void* startTCPClientSend(void* arg);
	static void* startUDPServer(void* arg);
	static void* startUDPSend(void* arg);
	void AddTime(CANFRAME& frm);
	bool IsTargetSocket(const sockaddr_in& sockAddr);
	void ClearRecvBuffer();
	void ClearSendBuffer();
	void RunTCPServer();
	void RunUDPServer();
	void RunUDPSend();
	void RunTCPClientRecv();
	void RunErrRecv();
	void RunTCPClientSend();
	void AddToRecvList(PCANFRAME pFrms, int nLen);
	void AddToErrList(const CMD_ERR_NOTICE& err);
	void SetSocketRecvTimeout(int nSocket, long nSec, long nUsec);
	void SetSocketSendTimeout(int nSocket, long nSec, long nUsec);
	bool CheckFrame(PCANFRAME pFrm);
private:
	pthread_t m_threadIDRecv;
	pthread_t m_threadIDSend;
	pthread_t m_threadIDErr;
	volatile bool m_bThreadContinue;
	bool m_bConnected;
	int m_nType;
	int m_sTCPServer;
	int m_sTCPClient;
	int m_sErr;
	int m_sUDPServer;
	int m_nClientPortUDP;
	char* m_pIpAddrUDPClient;
	sockaddr_in m_addrUDPClient;
	socklen_t m_socklen;
	RECVFRMSET m_listRecv;
	SENDFRMSET m_listSend;
	ERRSET m_listErr;
	CANFRAMEEx m_frmRecv;
	CMD_ERR_NOTICEEx m_errRecv;
	int m_nCount;
};

CFrmManager::CFrmManager(void) {
	m_bConnected = false;
	m_bThreadContinue = true;
	m_pIpAddrUDPClient = NULL;
	m_listSend.SetLen(MAX_FRM_COUNT);
	m_listRecv.SetLen(MAX_FRM_COUNT);
	m_nCount = 0;
}

CFrmManager::~CFrmManager(void) {
	SAFE_DELETE(m_pIpAddrUDPClient);
}

bool CFrmManager::InitTCPServer(int nServer) {
	m_bThreadContinue = true;
	m_sTCPServer = nServer;
	if (pthread_create(&m_threadIDRecv, NULL, startTCPServer, (void*) this)
			!= 0) {
		return false;
	}
	m_bConnected = true;
	return true;
}

void* CFrmManager::startTCPServer(void* arg) {
	CFrmManager* pMgr = static_cast<CFrmManager*>(arg);
	if (pMgr) {
		pMgr->RunTCPServer();
	}
	return NULL;
}

void CFrmManager::RunTCPServer() {
	m_nType = TYPE_TCP_SERVER;
	int nMaxSocket = m_sTCPServer + 1;
	int nClientSocket = -1;
	struct timeval tv;
	tv.tv_sec = SELECT_TIME_OUT;
	tv.tv_usec = 0;
	fd_set fdRead;
	fd_set fdWrite;
	const int LEN = sizeof(CANFRAME);
	CANFRAME* frm_data;
	while (m_bThreadContinue) {
		FD_ZERO(&fdRead);
		FD_SET(m_sTCPServer, &fdRead);
		if (nClientSocket != -1)
		{
			FD_SET(nClientSocket, &fdRead);
		}
		FD_ZERO(&fdWrite);
		FD_SET(m_sTCPServer, &fdWrite);
		if (nClientSocket != -1)
		{
			FD_SET(nClientSocket, &fdWrite);
		}
		tv.tv_sec = SELECT_TIME_OUT;
		int nRet = select(nMaxSocket, &fdRead, &fdWrite, NULL, &tv);
		if (nRet > 0) {
			if (FD_ISSET(m_sTCPServer, &fdRead)) {
				sockaddr_in remoteAddr;
				int nAddLen = sizeof(remoteAddr);
				nClientSocket = ::accept(m_sTCPServer, (sockaddr*) &remoteAddr,
						&nAddLen);
				nMaxSocket = (nClientSocket > m_sTCPServer ? nClientSocket : m_sTCPServer) + 1;
				SetSocketRecvTimeout(nClientSocket, 0, RECV_TIME_OUT);
				SetSocketSendTimeout(nClientSocket, SEND_TIME_OUT, 0);
			}
			if (FD_ISSET(nClientSocket, &fdRead)) {
				int nRecv = recv2(nClientSocket, (char**)&frm_data, FRM_COUNT_RECV, 0);
				if (nRecv > 0) {
					AddToRecvList(frm_data, nRecv / LEN);
				} else if (0 == nRecv) {
					shutdown(nClientSocket, SHUT_RDWR);
				}
			}
			if (FD_ISSET(nClientSocket, &fdWrite)) {
				int nCount = m_listSend.GetLen();
				if (nCount <= 0)
					continue;
				PCANFRAME pFrms = new CANFRAME[nCount];
				if (nCount == GetFrmsSend(pFrms, nCount)) {
					SendFrms(pFrms, nCount, nClientSocket);
				}
				SAFE_DELETE_ARRAY(pFrms);
			}
		}
	}
	shutdown(m_sTCPServer, SHUT_RDWR);
	shutdown(nClientSocket, SHUT_RDWR);
	LOGI("TCPServer end");
}

void CFrmManager::StopThread() {
	if (m_bThreadContinue) {
		m_bThreadContinue = false;
		m_nCount = 0;
		m_listRecv.Clear();
		m_listSend.Clear();
		m_listErr.Clear();
		pthread_join(m_threadIDRecv, NULL);
		pthread_join(m_threadIDErr, NULL);
		if (TYPE_TCP_SERVER == m_nType) {
			shutdown(m_sTCPServer, SHUT_RDWR);
		} else if (TYPE_TCP_CLIENT == m_nType) {
			shutdown(m_sTCPClient, SHUT_RDWR);
		} else if (TYPE_UDP_SERVER == m_nType) {
			shutdown(m_sUDPServer, SHUT_RDWR);
		}
		shutdown(m_sErr, SHUT_RDWR);
		m_bConnected = false;
	}
}

void CFrmManager::AddToRecvList(PCANFRAME pFrms, int nLen) {
	for (int i = 0; i < nLen; ++i) {
		if (!CheckFrame(&pFrms[i]))
		{
			continue;
		}
		m_frmRecv.frm = pFrms[i];
		m_frmRecv.nTime = GetCurTime();
		m_listRecv.AddObj(&m_frmRecv, 1);
	}
}

int CFrmManager::GetFrmsRecv(CANFRAMEEx* pFrms, int nLen) {
	if (NULL == pFrms) {
		return 0;
	}
	m_listRecv.GetObj(pFrms, nLen);
	return nLen;
}

int CFrmManager::GetFrmsSend(CANFRAME* pFrms, int nLen) {
	if (NULL == pFrms) {
		return 0;
	}
	m_listSend.GetObj(pFrms, nLen);
	return nLen;
}

bool CFrmManager::SendFrms(PCANFRAME pFrms, int nCount, int nSocket) {
	for (int j = 0; j < nCount; ++j) {
		send(nSocket, (char*) &pFrms[j], sizeof(CANFRAME), 0);
	}
	return true;
}

void CFrmManager::MakeSendSet(CFdSet& allSet, const fd_set& writeSet,
		CFdSet& sendSet) {
	int nSize = allSet.size();
	for (int i = 0; i < nSize; ++i) {
		if (FD_ISSET(allSet.GetAt(i), &writeSet)) {
			sendSet.Add(allSet.GetAt(i));
		}
	}
}

void CFrmManager::AddToSendList(const CANFRAME& frm) {
	if (!m_bConnected)
		return;
	m_listSend.AddObj(&frm, 1);
}

void CFrmManager::RunTCPClientRecv() {
	m_nType = TYPE_TCP_CLIENT;
	SetSocketRecvTimeout(m_sTCPClient, 0, RECV_TIME_OUT);
	const int LEN = sizeof(CANFRAME);
	CANFRAME* frm_data;
	while (m_bThreadContinue) {
		int nRecv = recv2(m_sTCPClient, (char**)&frm_data, FRM_COUNT_RECV, 0);
		if (nRecv > 0) {
			AddToRecvList(frm_data, nRecv / LEN);
		} else if (0 == nRecv) {
			m_bConnected = false;
			m_bThreadContinue = false;
			m_listRecv.Clear();
			m_listSend.Clear();
			LOGI("tcp client unconnect");
			break;
		}
	}
	LOGI("close client:%d", m_sTCPClient);
	shutdown(m_sTCPClient, SHUT_RDWR);
}

bool CFrmManager::InitTCPClient(int nClient) {
	m_bThreadContinue = true;
	m_sTCPClient = nClient;
	if (pthread_create(&m_threadIDRecv, NULL, startTCPClientRecv, (void*) this)
			!= 0) {
		return false;
	}
	if (pthread_create(&m_threadIDSend, NULL, startTCPClientSend, (void*) this)
			!= 0) {
		return false;
	}
	m_bConnected = true;
	return true;
}

void* CFrmManager::startTCPClientRecv(void* arg) {
	CFrmManager* pMgr = static_cast<CFrmManager*>(arg);
	if (pMgr) {
		pMgr->RunTCPClientRecv();
	}
	return NULL;
}

void* CFrmManager::startTCPClientSend(void* arg) {
	CFrmManager* pMgr = static_cast<CFrmManager*>(arg);
	if (pMgr) {
		pMgr->RunTCPClientSend();
	}
	return NULL;
}

void CFrmManager::RunTCPClientSend() {
	PCANFRAME pFrms;
	int nCount;
	SetSocketSendTimeout(m_sTCPClient, SEND_TIME_OUT, 0);
	while (m_bThreadContinue) {
		nCount = m_listSend.GetLen();
		if (nCount <= 0)
			continue;
		pFrms = new CANFRAME[nCount];
		if (nCount == GetFrmsSend(pFrms, nCount)) {
			for (int i = 0; i < nCount; ++i) {
				send(m_sTCPClient, (char*) &pFrms[i], sizeof(CANFRAME), 0);
			}
		}
	}
	LOGI("TCPClientSend end");
}

void CFrmManager::RunUDPServer() {
	m_nType = TYPE_UDP_SERVER;
	sockaddr_in remoteAddr;
	socklen_t nAddLen = sizeof(remoteAddr);
	CANFRAME frm;

	SetSocketRecvTimeout(m_sUDPServer, 0, RECV_TIME_OUT);
	int nLen;
	const int nFrmLen = sizeof(frm);
	while (m_bThreadContinue) {
		nLen = recvfrom(m_sUDPServer, (char*) &frm, nFrmLen, 0,
				(struct sockaddr*) &remoteAddr, &nAddLen);
		if (nLen == nFrmLen && IsTargetSocket(remoteAddr)) {
			AddToRecvList(&frm, 1);
		}
		if (0 == nLen)//连接中止
		{
			m_bThreadContinue = false;
			break;
			LOGI("connect break");
		}
	}
	LOGI("exit UDPServer");
	shutdown(m_sUDPServer, SHUT_RDWR);
}

bool CFrmManager::InitUDPServer(int nServer, int nWorkPort,
		const char* pIpAddr) {
	m_bThreadContinue = true;
	m_sUDPServer = nServer;
	m_nClientPortUDP = nWorkPort;
	SAFE_DELETE(m_pIpAddrUDPClient);
	int nLen = strlen(pIpAddr) + 1;
	m_pIpAddrUDPClient = new char[nLen];
	strcpy(m_pIpAddrUDPClient, pIpAddr);
	if (pthread_create(&m_threadIDRecv, NULL, startUDPServer, (void*) this)
			!= 0) {
		return false;
	}
	if (pthread_create(&m_threadIDSend, NULL, startUDPSend, (void*) this)
			!= 0) {
		return false;
	}
	m_bConnected = true;
	return true;
}

void* CFrmManager::startUDPServer(void* arg) {
	CFrmManager* pMgr = static_cast<CFrmManager*>(arg);
	if (pMgr) {
		pMgr->RunUDPServer();
	}
	return NULL;
}

bool CFrmManager::IsTargetSocket(const sockaddr_in& sockAddr) {
	if (sockAddr.sin_port != htons(m_nClientPortUDP)) {
		return false;
	}
	const char* pIPAddr = inet_ntoa(sockAddr.sin_addr);
	if (strcmp(pIPAddr, m_pIpAddrUDPClient)) {
		return false;
	}
	return true;
}

void* CFrmManager::startUDPSend(void* arg) {
	CFrmManager* pMgr = static_cast<CFrmManager*>(arg);
	if (pMgr) {
		pMgr->RunUDPSend();
	}
	return NULL;
}

void CFrmManager::RunUDPSend() {
	CANFRAME frm;
	m_addrUDPClient.sin_family = AF_INET;
	m_addrUDPClient.sin_port = htons(m_nClientPortUDP);
	m_addrUDPClient.sin_addr.s_addr = inet_addr(m_pIpAddrUDPClient);
	m_socklen = sizeof(m_addrUDPClient);
	int nCount;
	PCANFRAME pFrms;
	SetSocketSendTimeout(m_sUDPServer, RECV_TIME_OUT, 0);
	while (m_bThreadContinue) {
		nCount = m_listSend.GetLen();
		if (nCount > 0)
		{
			pFrms = new CANFRAME[nCount];
			if (nCount == GetFrmsSend(pFrms, nCount)) {
				for (int i = 0; i < nCount; ++i) {
					sendto(m_sUDPServer, (char*) &pFrms[i], sizeof(CANFRAME), 0,
						(struct sockaddr*) &m_addrUDPClient, m_socklen);
				}
			}
			SAFE_DELETE_ARRAY(pFrms);
		}
// 		else{
// 			sleep(SEND_THREAD_WAIT_TIME);
// 		}
	}
	LOGI("exit UDPSend");
}

void CFrmManager::ClearBuffer() {
	ClearRecvBuffer();
	ClearSendBuffer();
}

void CFrmManager::ClearRecvBuffer() {
	m_listRecv.Clear();
}

void CFrmManager::ClearSendBuffer() {
	m_listSend.Clear();
}

int CFrmManager::GetRecvNum() {
	return m_listRecv.GetLen();
}

int CFrmManager::CreateTcpServer(int nPort) {
	int sListen;
	if ((sListen = socket(AF_INET, SOCK_STREAM, 0)) == INVALID_SOCKET) {
		LOGI("create socket failed");
		return sListen;
	}
	int opt = 1;
	setsockopt(sListen, SOL_SOCKET, SO_REUSEADDR, &opt, sizeof(opt));
	sockaddr_in sin;
	memset(&sin, 0, sizeof(sin));
	sin.sin_family = AF_INET;
	sin.sin_port = htons(nPort);
	LOGI("Server port:%d", nPort);
	sin.sin_addr.s_addr = htonl(INADDR_ANY);
	if (bind(sListen, (sockaddr*) &sin, sizeof(sin)) == SOCKET_ERROR) {
		LOGI("bind socket failed");
		shutdown(sListen, SHUT_RDWR);
		return INVALID_SOCKET;
	}
	if (listen(sListen, MAX_CONNECT_COUNT) == SOCKET_ERROR) {
		LOGI("listen failed");
		shutdown(sListen, SHUT_RDWR);
		return INVALID_SOCKET;
	}
	return sListen;
}

int CFrmManager::CreateTcpClient(const char* pIpAddr, int nPort) {
	int sClient;
	if ((sClient = socket(AF_INET, SOCK_STREAM, 0)) == INVALID_SOCKET) {
		LOGI("create socket failed");
		return sClient;
	}
	sockaddr_in sin;
	memset(&sin, 0, sizeof(sin));
	sin.sin_family = AF_INET;
	sin.sin_port = htons(nPort);
	sin.sin_addr.s_addr = inet_addr(pIpAddr);

	struct timeval tout;
	socklen_t slen = sizeof(tout);
	tout.tv_sec = CONNECT_TIME_OUT;
	tout.tv_usec = 0;
	setsockopt(sClient, SOL_SOCKET, SO_SNDTIMEO, &tout, slen);
	if (connect(sClient, (struct sockaddr*) &sin, sizeof(sin)) == SOCKET_ERROR) {
		LOGI("connect socket failed");
		shutdown(sClient, SHUT_RDWR);
		return INVALID_SOCKET;
	}
	return sClient;
}

int CFrmManager::CreateUdpServer(int nPort) {
	int sListen;
	if ((sListen = socket(AF_INET, SOCK_DGRAM, 0)) == INVALID_SOCKET) {
		return sListen;
	}
	int opt = 1;
	setsockopt(sListen, SOL_SOCKET, SO_REUSEADDR, &opt, sizeof(opt));
	sockaddr_in sin;
	memset(&sin, 0, sizeof(sin));
	sin.sin_family = AF_INET;
	sin.sin_port = htons(nPort);
	sin.sin_addr.s_addr = htonl(INADDR_ANY);
	if (bind(sListen, (sockaddr*) &sin, sizeof(sin)) == SOCKET_ERROR) {
		LOGI("bind socket failed");
		shutdown(sListen, SHUT_RDWR);
		return INVALID_SOCKET;
	}
	return sListen;
}

bool CFrmManager::InitTCPClientErr(int nClient) {
	m_bThreadContinue = true;
	m_sErr = nClient;
	if (pthread_create(&m_threadIDErr, NULL, startErrRecv, (void*) this) != 0) {
		return false;
	}
	return true;
}

void* CFrmManager::startErrRecv(void* arg) {
	CFrmManager* pMgr = static_cast<CFrmManager*>(arg);
	if (pMgr) {
		pMgr->RunErrRecv();
	}
	return NULL;
}

void CFrmManager::RunErrRecv() {
	SetSocketRecvTimeout(m_sErr, 0, RECV_TIME_OUT);
	const int LEN = sizeof(CMD_ERR_NOTICE);
	while (m_bThreadContinue) {
		CMD_ERR_NOTICE err;
		int nRecv = ::recv(m_sErr, &err, LEN, 0);
		if (LEN == nRecv) {
			AddToErrList(err);
			continue;
		} else if (0 == nRecv) {
			m_bConnected = false;
			m_bThreadContinue = false;
			m_listRecv.Clear();
			m_listSend.Clear();
			break;
		}
		sleep(ERR_THREAD_WAIT_TIME);
		//LOGI("RunErrRecv");
	}
	LOGI("close err socket");
	shutdown(m_sErr, SHUT_RDWR);
}

void CFrmManager::AddToErrList(const CMD_ERR_NOTICE& err) {
	m_errRecv.err = err;
	m_errRecv.nTime = GetCurTime();
	m_listErr.AddObj(&m_errRecv, 1);
}

int CFrmManager::GetErrorInfo(CMD_ERR_NOTICEEx* pErrInfos, int nLen) {
	if (NULL == pErrInfos) {
		return 0;
	}
	m_listErr.GetObj(pErrInfos, nLen);
	return nLen;
}

void CFrmManager::SetSocketRecvTimeout(int nSocket, long nSec, long nUsec) {
	struct timeval tv;
	tv.tv_sec = nSec;
	tv.tv_usec = nUsec;
	setsockopt(nSocket, SOL_SOCKET, SO_RCVTIMEO, (char*) &tv, sizeof(tv));
}

void CFrmManager::SetSocketSendTimeout(int nSocket, long nSec, long nUsec) {
	struct timeval tv;
	tv.tv_sec = nSec;
	tv.tv_usec = nUsec;
	setsockopt(nSocket, SOL_SOCKET, SO_SNDTIMEO, (char*) &tv, sizeof(tv));
}

bool CFrmManager::IsConnected() {
	return m_bConnected;
}

long long CFrmManager::GetCurTime() {
	struct timespec curTime = { 0, 0 };
	clock_gettime(CLOCK_REALTIME, &curTime);
	long long time64 = curTime.tv_sec;
	return time64 * 1000 + curTime.tv_nsec / 1000000;
}

void CFrmManager::ClearThread()
{
	m_bConnected = false;
	m_bThreadContinue = false;
	sleep(CLEAR_THREAD_WAIT_TIME);
}

bool CFrmManager::CheckFrame(PCANFRAME pFrm)
{
	int nlen = sizeof(CANFRAME);
	unsigned char* data = reinterpret_cast<unsigned char*>(pFrm);
	unsigned char nCrc = 0;
	if (data)
	{
		for (int i = 0; i < nlen - 1; ++i)
		{
			nCrc ^= data[i];
		}
		return nCrc == pFrm->crc;
	}
	return false;
}

#endif // _FRMMANAGER_H_
