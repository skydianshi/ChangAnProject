#ifndef _COMDEF_H_
#define _COMDEF_H_
#include <android/log.h>

#define INVALID_SOCKET -1
#define SOCKET_ERROR -1
#define MAX_CONNECT_COUNT 128
#define SAFE_DELETE(p) if (p) { delete p; p = NULL;}
#define SAFE_DELETE_ARRAY(p) if (p) { delete[] p; p = NULL;}
#define SELECT_TIME_OUT 5
#define CONNECT_TIME_OUT 2
#define RECV_TIME_OUT (200 * 1000)//200毫秒
#define SEND_TIME_OUT 1
#define ERR_THREAD_WAIT_TIME 5
#define SEND_THREAD_WAIT_TIME 1
#define CLEAR_THREAD_WAIT_TIME 1

#define TYPE_TCP_SERVER 0
#define TYPE_TCP_CLIENT 1
#define TYPE_UDP_SERVER 2

#define LOG    "CANWifi" // 这个是自定义的LOG的标识
#define LOGD(...)  __android_log_print(ANDROID_LOG_DEBUG,LOG,__VA_ARGS__) // 定义LOGD类型
#define LOGI(...)  __android_log_print(ANDROID_LOG_INFO,LOG,__VA_ARGS__) // 定义LOGI类型
#define LOGW(...)  __android_log_print(ANDROID_LOG_WARN,LOG,__VA_ARGS__) // 定义LOGW类型
#define LOGE(...)  __android_log_print(ANDROID_LOG_ERROR,LOG,__VA_ARGS__) // 定义LOGE类型
#define LOGF(...)  __android_log_print(ANDROID_LOG_FATAL,LOG,__VA_ARGS__) // 定义LOGF类型

#define HEADER0 0xfe
#define HEADER1 0xfd
typedef struct _tagCANFrame {
	unsigned char header[2]; //0xfe 0xfd
	unsigned char sendtype; //0:正常发送，1：自发自收//reserved;
	unsigned char bDLC :4;
	unsigned char bReserved :2;
	unsigned char bRemote :1;
	unsigned char bExtern :1;
	unsigned int id;
	unsigned char data[8];
	unsigned char reserved[3]; //字节对齐保留，全为0
	unsigned char crc; //前面所有字节异或
} CANFRAME, *PCANFRAME;

typedef struct _tagCANFrameEx {
	CANFRAME frm;
	long long nTime;
} CANFRAMEEx, *PCANFRAMEEx;

#pragma pack(1)
typedef struct _CMD_ERR_NOTICE {
	char header[2]; //固定为0xaa，0x00
	char ErrType;
	int ErrCount; //错误发生计数（32bit），高位在前
	char tail; //固定为0x55
} CMD_ERR_NOTICE;
#pragma  pack()

typedef struct _CMD_ERR_NOTICEEx {
	CMD_ERR_NOTICE err;
	long long nTime;
} CMD_ERR_NOTICEEx;

#endif // _COMDEF_H_
