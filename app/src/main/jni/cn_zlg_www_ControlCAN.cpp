#include "cn_zlg_www_ControlCAN.h"
#include "ComDef.h"
#include "FrmManager.h"

CFrmManager frmMgr;

JNIEXPORT jboolean JNICALL Java_cn_zlg_www_ControlCAN_initTCPServer(JNIEnv *env,
		jobject obj, jint nPort, jstring ipAddrErrNotice, jint nPortErrNotice) {
	frmMgr.ClearThread();
	int sListen;
	if (INVALID_SOCKET == (sListen = frmMgr.CreateTcpServer(nPort))) {
		return false;
	}
	const char* pAddr = env->GetStringUTFChars(ipAddrErrNotice, 0);
	/*
	int sErrClient;
	if (INVALID_SOCKET
			== (sErrClient = frmMgr.CreateTcpClient(pAddr, nPortErrNotice))) {
		shutdown(sListen, SHUT_RDWR);
		env->ReleaseStringUTFChars(ipAddrErrNotice, pAddr);
		return false;
	}
	if (!frmMgr.InitTCPClientErr(sErrClient)) {
		LOGI("InitTCPClientErr failed");
		env->ReleaseStringUTFChars(ipAddrErrNotice, pAddr);
		shutdown(sListen, SHUT_RDWR);
		return false;
	}
	*/
	if (!frmMgr.InitTCPServer(sListen)) {
		LOGI("InitTCPServer failed");
		env->ReleaseStringUTFChars(ipAddrErrNotice, pAddr);
		shutdown(sListen, SHUT_RDWR);
		frmMgr.StopThread();
		return false;
	}
	env->ReleaseStringUTFChars(ipAddrErrNotice, pAddr);
	return true;
}

JNIEXPORT jboolean JNICALL Java_cn_zlg_www_ControlCAN_initTCPClient(JNIEnv *env,
		jobject obj, jstring ipAddr, jint nPort, jint nPortErrNotice) {
	frmMgr.ClearThread();
	int sClient;
	const char* pAddr = env->GetStringUTFChars(ipAddr, 0);
	if (INVALID_SOCKET == (sClient = frmMgr.CreateTcpClient(pAddr, nPort))) {
		env->ReleaseStringUTFChars(ipAddr, pAddr);
		LOGI("CreateTcpClient failed");
		return false;
	}
	/*
	int sErrClient;
	if (INVALID_SOCKET
			== (sErrClient = frmMgr.CreateTcpClient(pAddr, nPortErrNotice))) {
		env->ReleaseStringUTFChars(ipAddr, pAddr);
		LOGI("CreateTcpClient failed");
		shutdown(sClient, SHUT_RDWR);
		return false;
	}
	if (!frmMgr.InitTCPClientErr(sErrClient)) {
		LOGI("InitTCPClientErr failed");
		env->ReleaseStringUTFChars(ipAddr, pAddr);
		shutdown(sClient, SHUT_RDWR);
		return false;
	}
	*/
	if (!frmMgr.InitTCPClient(sClient)) {
		LOGI("InitTCPClient failed");
		env->ReleaseStringUTFChars(ipAddr, pAddr);
		shutdown(sClient, SHUT_RDWR);
		frmMgr.StopThread();
		return false;
	}
	return true;
}

JNIEXPORT jboolean JNICALL Java_cn_zlg_www_ControlCAN_initUDP(JNIEnv *env,
		jobject obj, jint nPortLocal, jstring ipAddr, jint nPort,
		jint nPortErrNotice) {
	frmMgr.ClearThread();
	int sListen;
	if (INVALID_SOCKET == (sListen = frmMgr.CreateUdpServer(nPortLocal))) {
		return false;
	}
	const char* pAddr = env->GetStringUTFChars(ipAddr, 0);
	/*
	int sErrClient;
	if (INVALID_SOCKET
			== (sErrClient = frmMgr.CreateTcpClient(pAddr, nPortErrNotice))) {
		env->ReleaseStringUTFChars(ipAddr, pAddr);
		shutdown(sListen, SHUT_RDWR);
		return false;
	}
	if (!frmMgr.InitTCPClientErr(sErrClient)) {
		LOGI("InitTCPClientErr failed");
		env->ReleaseStringUTFChars(ipAddr, pAddr);
		shutdown(sListen, SHUT_RDWR);
		return false;
	}
	*/
	if (!frmMgr.InitUDPServer(sListen, nPort, pAddr)) {
		LOGI("InitUDPServer failed");
		env->ReleaseStringUTFChars(ipAddr, pAddr);
		shutdown(sListen, SHUT_RDWR);
		frmMgr.StopThread();
		return false;
	}
	return true;
}

JNIEXPORT jint JNICALL Java_cn_zlg_www_ControlCAN_recv(JNIEnv *env, jobject obj,
		jobjectArray objArray, jint nLen) {
	static jclass jcs = env->FindClass("cn/zlg/www/FrmData");
	static jfieldID jID = env->GetFieldID(jcs, "nID", "J");
	static jfieldID jExternFlag = env->GetFieldID(jcs, "bExtend", "Z");
	static jfieldID jRemoteFlag = env->GetFieldID(jcs, "bRemote", "Z");
	static jfieldID jDataLen = env->GetFieldID(jcs, "nDataLen", "B");
	static jfieldID jData = env->GetFieldID(jcs, "datas", "[S");
	static jfieldID jTime = env->GetFieldID(jcs, "nTime", "J");
	CANFRAMEEx* pFrms = new CANFRAMEEx[nLen];
	int nRet = frmMgr.GetFrmsRecv(pFrms, nLen);
	for (int i = 0; i < nRet; ++i) {
		jobject obj = env->GetObjectArrayElement(objArray, i);
		env->SetLongField(obj, jTime, pFrms[i].nTime);
		env->SetLongField(obj, jID, htonl(pFrms[i].frm.id));
		env->SetBooleanField(obj, jExternFlag, pFrms[i].frm.bExtern);
		env->SetBooleanField(obj, jRemoteFlag, pFrms[i].frm.bRemote);
		env->SetByteField(obj, jDataLen, pFrms[i].frm.bDLC);
		int nLen = pFrms[i].frm.bDLC;
		jshortArray jArray = env->NewShortArray(nLen);
		jshort* pDatas = env->GetShortArrayElements(jArray, 0);
		for (int j = 0; j < nLen; ++j) {
			pDatas[j] = pFrms[i].frm.data[j];
		}
		env->SetShortArrayRegion(jArray, 0, nLen, pDatas);
		env->SetObjectField(obj, jData, jArray);
		env->ReleaseShortArrayElements(jArray, pDatas, 0);
		env->DeleteLocalRef(jArray);
		env->DeleteLocalRef(obj);
	}
	SAFE_DELETE_ARRAY(pFrms);
	return nRet;
}

JNIEXPORT jboolean JNICALL Java_cn_zlg_www_ControlCAN_send(JNIEnv *env,
		jobject obj, jobjectArray objArray, jint nLen) {
	static jclass jcs = env->FindClass("cn/zlg/www/FrmData");
	static jfieldID jID = env->GetFieldID(jcs, "nID", "J");
	static jfieldID jExternFlag = env->GetFieldID(jcs, "bExtend", "Z");
	static jfieldID jRemoteFlag = env->GetFieldID(jcs, "bRemote", "Z");
	static jfieldID jDataLen = env->GetFieldID(jcs, "nDataLen", "B");
	static jfieldID jData = env->GetFieldID(jcs, "datas", "[S");
	static jfieldID jSendType = env->GetFieldID(jcs, "nSendType", "B");
	CANFRAME frm;
	frm.header[0] = HEADER0;
	frm.header[1] = HEADER1;
	frm.reserved[0] = frm.reserved[1] = frm.reserved[2] = 0;
	frm.bReserved = 0;
	long tmp;
	char* pData;
	for (int i = 0; i < nLen; ++i) {
		jobject obj = env->GetObjectArrayElement(objArray, i);
		frm.bDLC = env->GetByteField(obj, jDataLen);
		frm.bExtern = env->GetBooleanField(obj, jExternFlag);
		frm.bRemote = env->GetBooleanField(obj, jRemoteFlag);
		frm.sendtype = env->GetByteField(obj, jSendType);
		frm.id = env->GetLongField(obj, jID)/*htonl(tmp)*/;
		pData = (char*) &frm;
		unsigned char crc = HEADER0 ^ HEADER1 ^ pData[3] ^ tmp;
		jshortArray jArray = (jshortArray) env->GetObjectField(obj, jData);
		jshort* jDatas = env->GetShortArrayElements(jArray, 0);
		for (int j = 0; j < frm.bDLC; ++j) {
			frm.data[j] = jDatas[j];
		}
		frm.crc = 0;
		char* pTmp = (char*) &frm;
		for (int k = 0; k < sizeof(CANFRAME) - 1; ++k) {
			frm.crc ^= pTmp[k];
		}
		frm.id = htonl(frm.id);
		env->ReleaseShortArrayElements(jArray, jDatas, 0);
		frmMgr.AddToSendList(frm);
		env->DeleteLocalRef(jArray);
		env->DeleteLocalRef(obj);
	}
	return true;
}

JNIEXPORT jboolean JNICALL Java_cn_zlg_www_ControlCAN_close(JNIEnv *env,
		jobject obj) {
	frmMgr.StopThread();
	return true;
}

JNIEXPORT void JNICALL Java_cn_zlg_www_ControlCAN_clearBuffer( JNIEnv *env, jobject obj)
{
	frmMgr.ClearBuffer();
}

JNIEXPORT jint JNICALL Java_cn_zlg_www_ControlCAN_getReceiveNum(JNIEnv *env,
		jobject obj) {
	return frmMgr.GetRecvNum();
}

JNIEXPORT jboolean JNICALL Java_cn_zlg_www_ControlCAN_readErrInfo(JNIEnv *env,
		jobject obj, jobject errObj) {
	/*
	static jclass jcs = env->FindClass("cn/zlg/www/ErrData");
	static jfieldID jCMD = env->GetFieldID(jcs, "nCmdValue", "B");
	static jfieldID jCount = env->GetFieldID(jcs, "nCount", "J");
	static jfieldID jTime = env->GetFieldID(jcs, "nTime", "J");

	bool bResult = false;
	CMD_ERR_NOTICEEx crn;
	if (1 == frmMgr.GetErrorInfo(&crn, 1)) {
		env->SetByteField(errObj, jCMD, crn.err.ErrType);
		env->SetLongField(errObj, jCount, htonl(crn.err.ErrCount));
		env->SetLongField(errObj, jTime, crn.nTime);
		bResult = true;
	}
	return bResult;
	*/
	return false;
}

JNIEXPORT jboolean JNICALL Java_cn_zlg_www_ControlCAN_isConnected(JNIEnv *env,
		jobject obj) {
	return frmMgr.IsConnected();
}
