LOCAL_PATH:=$(call my-dir)
include $(CLEAR_VARS)
LOCAL_LDLIBS :=-llog
LOCAL_MODULE:=CANWifi
LOCAL_SRC_FILES:=cn_zlg_www_ControlCAN.cpp
include $(BUILD_SHARED_LIBRARY)