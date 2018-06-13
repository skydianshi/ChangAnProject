package com.changan.changanproject.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Environment;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.changan.changanproject.activity.SendActivity;
import com.changan.changanproject.adapter.FrmSentBaseAdapter;
import com.changan.changanproject.canmessage.figuremessage.BCUBattIFigureData;
import com.changan.changanproject.canmessage.figuremessage.BCUBattUFigureData;
import com.changan.changanproject.canmessage.figuremessage.BCUFltRnkFigureData;
import com.changan.changanproject.canmessage.figuremessage.BCUMaxBattTFigureData;
import com.changan.changanproject.canmessage.figuremessage.BCUMaxChrgPwrShoTFigureData;
import com.changan.changanproject.canmessage.figuremessage.BCUMinBattTFigureData;
import com.changan.changanproject.canmessage.figuremessage.BCUOperModFigureData;
import com.changan.changanproject.canmessage.figuremessage.BCUSOCFigureData;
import com.changan.changanproject.canmessage.ordermessage.ClearErrorMessageOrderData;
import com.changan.changanproject.canmessage.ordermessage.FlowControlOrderData;
import com.changan.changanproject.canmessage.ordermessage.GetDtcListOrderData;
import com.changan.changanproject.canmessage.ordermessage.GetFrozenMessageInfoOrderData;

import cn.zlg.www.ControlCAN;
import cn.zlg.www.ErrData;
import cn.zlg.www.ItemData;

import com.changan.changanproject.canmessage.vesionCommand.SupplierOrderData;
import com.changan.changanproject.canmessage.vesionCommand.VcuHardwareVersionOrderData;
import com.changan.changanproject.canmessage.vesionCommand.VcuSoftwareVersionOrderData;
import com.changan.changanproject.constant.Constant;
import com.changan.changanproject.fragment.ErrorFragment;
import com.changan.changanproject.listener.IPostListener;
import com.changan.changanproject.util.TimeGetter;
import com.changan.changanproject.util.WriteToCsv;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;


public class MyService extends Service {
    private static String TAG = "MyService";
    private MyBinder binder = new MyBinder();
    WriteToCsv writeToCsv;//csv录入工具
    private BufferedWriter csvWriter = null;//写入sd目录
    private IPostListener callback;
    private Thread readThread;//读取线程
    private Thread sendThread;//读取线程
    private Thread readErrThread;//读取错误信息线程
    private BlockingDeque<ItemData> sendQueue = new LinkedBlockingDeque<>();

    private ControlCAN controlCAN = new ControlCAN();

    private String response;

    public  class MyBinder extends Binder {
        public void setListener(IPostListener _callback){
            callback = _callback;
        }
        //开始接收汽车CAN报文
        public void connectCar(){
            connect();
        }
        public void unConnect(){
            if(controlCAN.close()){
                Constant.is_connect = false;
                callback.toast("断开连接");
                callback.updateMenu("unconnect");
            }
        }
        //更新数据界面数据
        public void updateFigure(){
            Constant.is_read = true;
        }
        public void stopUpdate(){
            Constant.is_read = false;
        }
        //将CAN报文写入文件
        public void saveFigure(){
            Constant.is_save = true;
            startWrite();
        }
        public void stopSave(){
            Constant.is_save = false;
        }

        //获取Fragment发来的请求并进行处理
        public void getFragmentRequest(String request){
            try {
                handleRequest(request);
                Constant.is_sendOrder = true;//通知发送指令
            } catch (InterruptedException e) {
                Log.e(TAG,"添加发送指令出错");
                e.printStackTrace();
                return;
            }
        }

        public void sendFrames(int count,int timeInterval,int frmInterval){
            sendFrame(count,timeInterval,frmInterval);
        }
    }


    //连接汽车并启动读取和发送线程
    public void connect(){
        callback.showDialog("连接中···");
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if(controlCAN.initTCPClient(Constant.IP_ADDRESS, Constant.TARGET_PORT, Constant.ERR_PORT)){
                    //刚连接上时要清空缓冲区，防止缓冲区数据太大卡死程序
                    controlCAN.clearBuffer();
                    Constant.is_connect = true;
                    callback.toast("连接成功");
                    callback.updateMenu("connect");
                    //接收线程
                    readThread = new Thread(readTask);
                    readThread.start();
                    //发送线程时刻处于唤醒状态，随时监听需要发送的指令
                    sendThread = new Thread(sendTask);
                    sendThread.start();
                    //定时读取错误信息
                    readErrThread = new Thread(readErrTask);
                    readErrThread.start();
                }else{
                    Constant.is_connect = false;
                    callback.toast("连接出错，请检查配置是否正确，wifi是否连接后重新连接");
                    callback.updateMenu("unconnect");
                }
                callback.dismissDialog();
            }
        }).start();



    }
    //读取任务
    Runnable readTask = new Runnable() {
        @Override
        public void run() {
            int nCount = 0;
            String message = "";
            ItemData[] objs;
            while (Constant.is_connect) {
                if (!controlCAN.isConnected()) {
                    Constant.is_connect = false;
                    callback.toast("连接出错");
                    callback.updateMenu("unconnect");
                    return;
                }
                if ((nCount = controlCAN.getReceiveNum()) <= 0) {
                    continue;
                }
                objs = new ItemData[nCount];
                for (int i = 0; i < nCount; i++) {
                    objs[i] = new ItemData();
                    objs[i].bRecv = true;
                }
                if (controlCAN.recv(objs, nCount) != nCount) {
                    continue;
                }
                for (int i = 0; i < nCount; i++) {
                    if(Constant.is_read){
                        callback.addFrameData(objs[i]);
                    }
                    if(Long.toHexString(objs[i].nID).equals("7a8")){
                        message = "";
                        for(int j = 0;j<8;j++){
                            message += String.format("%02x",objs[i].datas[j]);
                        }
                        handleUpdate("07a8"+message);
                    }
                    if(Constant.is_save){
                        List<Object> rowList = new ArrayList();
                        rowList.add(Constant.writeIndex++);
                        rowList.add(objs[i].bRecv?"接收":"发送");
                        rowList.add(TimeGetter.getTimeString(objs[i].nTime));
                        rowList.add("0x"+String.format("%08x",objs[i].nID));
                        rowList.add(objs[i].bRemote?"远程帧":"数据帧");
                        rowList.add(objs[i].bExtend ? "扩展帧":"标准帧");
                        rowList.add(objs[i].nDataLen);
                        message = "";
                        for(int j = 0;j<8;j++){
                            message += String.format("%02x ",objs[i].datas[j]);
                        }
                        rowList.add(message);
                        try {
                            writeToCsv.writeRow(rowList,csvWriter);
                        } catch (IOException e) {
                            callback.toast("写入数据出错");
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    };


    //发送任务
    Runnable sendTask = new Runnable() {
        @Override
        public void run() {
        while (Constant.is_connect){
            try {
                if(Constant.is_sendOrder){
                    //发送Fragment传过来的指令
                    while (!sendQueue.isEmpty()){
                        ItemData command = sendQueue.take();
                        if(sendOrderData(command)){
                            Thread.sleep(100);
                        }else{
                            sendOrderData(command);//发送失败则再发送一次，增加一定的容错率
                            Thread.sleep(100);
                        }
                        //如果进行的是获取冻结帧任务，则更新进度条操作
                        if("sendGetFrozenSuccess".equals(response)){
                            handleUpdate("sendOneFrozenSuccess");
                            sendOrderData(new FlowControlOrderData());
                            Thread.sleep(1000);
                        }
                    }
                    Constant.is_sendOrder = false;
                    Thread.sleep(1000);
                    handleUpdate(response);
                }
                //发送数据流指令
               else{
                    for(int i = 0;i<Constant.figureDatas.size();i++){
                        sendOrderData(Constant.figureDatas.get(i));
                        Thread.sleep(100);
                    }
                }
            }catch (Exception e){
                callback.toast("发送指令出错，请确认网络连接是否正确");
                callback.updateMenu("unconnect");
                handleUpdate("dismissDialog");
                e.printStackTrace();
                return;
            }

        }
        }
    };

    //读取错误信息任务
    Runnable readErrTask = new Runnable() {
        @Override
        public void run() {
            ErrData errData = new ErrData();
            while (Constant.is_connect) {
                if (controlCAN.readErrInfo(errData)) {
                    callback.addErrData(errData);
                } else {
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        }
    };

    //自定义发送CAN数据
    public void sendFrame(final int count,final int timeInterval ,final int frmInterval){
        new Thread(new Runnable() {
            int mCount;
            @Override
            public void run() {
                mCount = count;
                int nSize = SendActivity.sendList.size();
                FrmSentBaseAdapter.Item itemTmp = null;
                ItemData item;
                while (mCount-- > 0) {
                    synchronized (this) {
                        try {
                            for (int i = 0; i < nSize; i++) {
                                if (!Constant.is_send) {
                                    callback.toast("停止发送成功");
                                    return;
                                }
                                itemTmp = SendActivity.sendList.get(i);
                                item = new ItemData();
                                item.bExtend = itemTmp.extend;
                                item.bRecv = false;
                                item.bRemote = itemTmp.remote;
                                item.nID = itemTmp.id;
                                item.nDataLen = (byte) splitString(item.datas, itemTmp.data);
                                item.nSendType = (byte) ( 1);
                                item.nTime = System.currentTimeMillis();
                                send(item);
                                if (frmInterval > 0) {
                                    // Thread.sleep(nFrmInterval);
                                    this.wait(frmInterval);
                                }
                            }
                            if (mCount > 1 && timeInterval > 0) {
                                // Thread.sleep(nTimeInterval);
                                this.wait(timeInterval);
                            }
                        } catch (InterruptedException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                }
                Constant.is_send = false;
            }
        }).start();
    }

    private int splitString(short[] datas, String strData) {
        String[] strings = strData.split(" ");
        int nCount = 0;
        for (int i = 0; i < strings.length; i++) {
            if (!strings[i].equals("")) {
                try {
                    datas[nCount++] = Short.parseShort(strings[i].substring(0,
                            strings[i].length() < 2 ? 1 : 2), 16);
                } catch (NumberFormatException e) {
                    // TODO: handle exception
                    datas[nCount++] = 0;
                }
                if (nCount >= 8) {
                    return 8;
                }
            }
        }
        return nCount;
    }

    //发送一条CAN数据
    ItemData[] datas = new ItemData[1];
    public boolean send(ItemData itemData) {
        datas[0] = itemData;
        if (!controlCAN.send(datas, 1)) {
            return false;
        }
        callback.addSendData(itemData);
        return true;
    }
    //发送一条指令式CAN数据，区别在于不用更新发送ListView
    public boolean sendOrderData(ItemData itemData) {
        datas[0] = itemData;
        if (!controlCAN.send(datas, 1)) {
            return false;
        }
        return true;
    }

    //处理Fragment传过来的请求
    public void handleRequest(String request) throws InterruptedException {
        if(request.equals("getDtc")){
            sendQueue.put(new GetDtcListOrderData());
            sendQueue.put(new FlowControlOrderData());
            response = "sendGetDtcSuccess";
        }else if(request.equals("clearDtc")){
            sendQueue.put(new ClearErrorMessageOrderData());
            response = "sendClearDtcSuccess";
        }else if(request.equals("getFrozen")){
            for(int i = 0;i< ErrorFragment.dtcMessages.size();i++){
                sendQueue.put(new GetFrozenMessageInfoOrderData(ErrorFragment.dtcMessages.get(i).getDtc()));
            }
            response = "sendGetFrozenSuccess";
        }else if(request.equals("getVersion")){
            sendQueue.put(new VcuHardwareVersionOrderData());
            sendQueue.put(new FlowControlOrderData());
            sendQueue.put(new VcuSoftwareVersionOrderData());
            sendQueue.put(new FlowControlOrderData());
            sendQueue.put(new SupplierOrderData());
            response = "sendGetVersionSuccess";
        }
    }

    //给Fragment发送新消息
    public void handleUpdate(String message){
        callback.figureUpdate(message);
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG,"Service is Binded");
        return binder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG,"service is created");

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG,"-----onStartCommand");
        return super.onStartCommand(intent, flags, startId);
    }


    //开始写入数据
    public void startWrite(){
        if(!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
            Log.d(TAG,"不支持sdcard");
            callback.toast("不支持sacard，无法写入CAN数据");
        }else{
            Log.d(TAG,"获取sdcard成功");
        }
        try {
            Object [] head = {"序号","传输方向","时间","帧ID","帧格式","帧类型","数据长度","数据",};
            writeToCsv = new WriteToCsv(Constant.saveFilePath,Environment.getExternalStorageDirectory().getPath()+"/CANWifi/",head);
            csvWriter = writeToCsv.csvWriter;
        } catch (Exception e) {
            Log.d(TAG,"获取SDcard出错");
            e.printStackTrace();
        }
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Constant.is_save = false;
        Constant.is_read = false;
        Constant.is_send = false;
        Constant.is_sendOrder = false;
        try {
            csvWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }
}
