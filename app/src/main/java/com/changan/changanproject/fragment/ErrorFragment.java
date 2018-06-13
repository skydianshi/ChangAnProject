package com.changan.changanproject.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.changan.changanproject.R;
import com.changan.changanproject.activity.FrozenMessageActivity;
import com.changan.changanproject.activity.DtcDescActivity;
import com.changan.changanproject.activity.MainActivity;
import com.changan.changanproject.adapter.DtcAdapter;
import com.changan.changanproject.adapter.TotalFrozenMessageAdapter;
import com.changan.changanproject.bean.DtcMessage;
import com.changan.changanproject.constant.Constant;
import com.changan.changanproject.listener.ErrorServiceListener;
import com.changan.changanproject.view.AlternativeDialog;
import com.changan.changanproject.view.WaitDialog;
import com.changan.changanproject.view.progressbar.HorizontalProgressBarWithNumber;
import com.changan.changanproject.util.ConvertUtil;

import java.util.ArrayList;
import java.util.List;


public class ErrorFragment extends Fragment implements View.OnClickListener{
    private ErrorServiceListener errorServiceListener;
    View view;
    private LinearLayout dtcLayout;
    private LinearLayout frozenLayout;
    private LinearLayout clearDtcLayout;
    private LinearLayout versionLayout;
    private Button getDtcMenu;
    private Button getFrozenMenu;
    private Button clearDtcMenu;
    private Button getVersionMenu;
    private TextView startGetDtcTextView;
    private TextView startGetFrozenTextView;
    private TextView clearDtcTextView;
    private TextView getVersionTextView;
    private ListView dtcListView;
    private ListView fmListView;
    private TextView hardVersionTextView;
    private TextView softVersionTextView;
    private TextView supplyVersionTextView;
    private WaitDialog mWaitDialog;
    private AlternativeDialog mAlternativeDialog;
    private HorizontalProgressBarWithNumber progressBar;
    private String mode;//根据模式确定获取回来的数据数据哪种指令
    MainActivity m;
    List<String> dtcRawMessages = new ArrayList<>();//故障码原始data
    public static List<DtcMessage> dtcMessages = new ArrayList<>();//故障码信息
    List<String> versionMessages = new ArrayList<>();//版本信息
    List<String> frozenMessages = new ArrayList<>();//冻结帧信息
    DtcAdapter adapter;//故障帧adapter
    TotalFrozenMessageAdapter fmAdapter;//冻结帧adapter
    private int frozenIndex = 0;//发送的dtcMessage计数，获取发送冻结帧进度
    //版本信息
    String hardwareVersion;
    String softwareVersion;
    String supplycode;
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
        switch (msg.what){
            case 0://响应回复
                String response = (String)msg.obj;
                Toast.makeText(getActivity(),response,Toast.LENGTH_SHORT).show();
                break;
            case 1://更新故障码冻结帧界面
                Toast.makeText(getActivity(),(String)msg.obj,Toast.LENGTH_SHORT).show();
                adapter = new DtcAdapter(getActivity(),dtcMessages);
                dtcListView.setAdapter(adapter);
                break;
            case 2://更新获取冻结帧进度条
                int progress = (int)msg.obj;//当前发送故障帧的个数
                int size = dtcMessages.size();//故障帧的个数
                progressBar.setProgress(progress*100/size);
                if(progress==size){
                    progressBar.setVisibility(View.GONE);
                }
                break;
            case 3://更新版本
                hardVersionTextView.setText(hardwareVersion);
                softVersionTextView.setText(softwareVersion);
                supplyVersionTextView.setText(supplycode);
                break;
            case 4://更新冻结帧
                Toast.makeText(getActivity(),(String)msg.obj,Toast.LENGTH_SHORT).show();
                fmAdapter = new TotalFrozenMessageAdapter(getActivity(),dtcMessages);
                fmListView.setAdapter(fmAdapter);
                break;
        }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_error, null);
        initConnection();
        initView();
        initListener();
        return view;
    }

    public void initView(){
        MainActivity.tb.getMenu().findItem(R.id.changeChat).setVisible(false);
        MainActivity.tb.setTitle("故障信息");
        dtcLayout = (LinearLayout)view.findViewById(R.id.dtcLayout);
        frozenLayout = (LinearLayout)view.findViewById(R.id.frozenLayout);
        clearDtcLayout = (LinearLayout)view.findViewById(R.id.clearDtcLayout);
        versionLayout = (LinearLayout)view.findViewById(R.id.getVersionLayout);
        getDtcMenu = (Button)view.findViewById(R.id.getErrorMenu);
        getFrozenMenu = (Button)view.findViewById(R.id.getFrozenMenu);
        clearDtcMenu = (Button)view.findViewById(R.id.clearErrorCodeMenu);
        getVersionMenu = (Button)view.findViewById(R.id.getVersionMenu) ;
        startGetDtcTextView = (TextView)view.findViewById(R.id.startGetDtcTextView);
        startGetFrozenTextView = (TextView)view.findViewById(R.id.startGetFrozenTextView);
        clearDtcTextView = (TextView)view.findViewById(R.id.clearDtcTextView);
        getVersionTextView = (TextView)view.findViewById(R.id.getVersionTextView);
        hardVersionTextView = (TextView)view.findViewById(R.id.hVersionTextView);
        softVersionTextView = (TextView)view.findViewById(R.id.sVersionTextView);
        supplyVersionTextView = (TextView)view.findViewById(R.id.supplyVersionTextView);
        dtcListView = (ListView)view.findViewById(R.id.dtcListView);
        fmListView = (ListView)view.findViewById(R.id.frozenListView);
        progressBar = (HorizontalProgressBarWithNumber) view.findViewById(R.id.progressBar);
        m = (MainActivity)getActivity();
    }

    public void initListener(){
        getFrozenMenu.setOnClickListener(this);
        getDtcMenu.setOnClickListener(this);
        clearDtcMenu.setOnClickListener(this);
        getVersionMenu.setOnClickListener(this);
        startGetDtcTextView.setOnClickListener(this);
        startGetFrozenTextView.setOnClickListener(this);
        clearDtcTextView.setOnClickListener(this);
        getVersionTextView.setOnClickListener(this);

        dtcListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getActivity(), DtcDescActivity.class);
                intent.putExtra("position",i);
                startActivity(intent);
            }
        });
        fmListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getActivity(), FrozenMessageActivity.class);
                intent.putExtra("position",i);
                startActivity(intent);
            }
        });
    }

    //建立与Activity的连接
    public void initConnection(){
        errorServiceListener = new ErrorServiceListener() {
            //接收07A8数据
            @Override
            public void getResult(String message) {
                Message msg = new Message();
                if(mode.equals("clearDtc")){
                    msg.what = 0;
                    msg.obj = "清除故障码成功";
                    handler.sendMessage(msg);
                }else if(mode.equals("getFrozen")){
                    frozenMessages.add(message);//接收到冻结帧并存入
                }else if(mode.equals("getDtc")){
                    dtcRawMessages.add(message);//接收到dtc原始数据并存入
                } else if(mode.equals("getVersion")){
                    versionMessages.add(message);//接收到版本数据并存入
                }
            }
            //接收发送成功指令
            @Override
            public void getResponse(String message) {
                Message msg = new Message();
                msg.what = 0;
                if(message.equals("sendGetDtcSuccess")){//发送获取故障码成功
                    msg.obj = "获取故障码成功";
                    msg.what = 1;
                    getDetailDtcs();
                    mWaitDialog.dismiss();
                    handler.sendMessage(msg);
                }else if(message.equals("sendClearDtcSuccess")){//发送清除故障码指令成功
                    msg.obj = "清除故障码成功";
                    handler.sendMessage(msg);
                } else if(message.equals("sendOneFrozenSuccess")){//发送获取冻结帧成功
                    msg.what = 2;
                    msg.obj = ++frozenIndex;
                    handler.sendMessage(msg);
                }else if(message.equals("sendGetFrozenSuccess")){//发送获取冻结帧成功
                    msg.what = 4;
                    msg.obj = "获取冻结帧成功";
                    Constant.is_readFrozenMessage = false;
                    //将冻结帧分类到各个故障码之下
                    divideFrozenMessage();
                    handler.sendMessage(msg);
                }else if(message.equals("sendGetVersionSuccess")){//发送获取版本指令成功
                    msg.what = 3;
                    getVersion();
                    handler.sendMessage(msg);
                    mWaitDialog.dismiss();
                } else if(message.equals("dismissDialog")){//清除dialog
                    mWaitDialog.dismiss();
                }
            }
        };
        MainActivity.setErrorListener(errorServiceListener);
    }


    //获取软硬件版本
    public void getVersion(){
        String hex_hardwareVersion0 = versionMessages.get(0).substring(16,18);
        String hex_hardwareVersion1 = versionMessages.get(1).substring(6,10);
        hardwareVersion = "硬件版本：H"+ ConvertUtil.hexToascii(hex_hardwareVersion0)+"."+ConvertUtil.hexToascii(hex_hardwareVersion1);
        String hex_softwareVersion0 = versionMessages.get(2).substring(16,18);
        String hex_softwareVersion1 = versionMessages.get(3).substring(6,10);
        String hex_softwareVersion2 = versionMessages.get(3).substring(12,14);
        softwareVersion = "软件版本：S"+ConvertUtil.hexToascii(hex_softwareVersion0)+"."+ConvertUtil.hexToascii(hex_softwareVersion1)+"."+ConvertUtil.hexToascii(hex_softwareVersion2);
        String hex_supply = versionMessages.get(4).substring(12,16);
        supplycode = "供应商代码："+Integer.parseInt(hex_supply,16);
    }

    //将故障码从数据帧中截取出来
    public void getDetailDtcs(){
        StringBuilder dtcStringBuilder = new StringBuilder();//获取dtc串
        //将具体的故障码串成一个完整的字符串
        for(int i = 0;i<dtcRawMessages.size();i++){
            if(i==0){
                dtcStringBuilder.append(dtcRawMessages.get(i).substring(14));
            }else{
                dtcStringBuilder.append(dtcRawMessages.get(i).substring(6));
            }
        }
        String dtcString = dtcStringBuilder.toString();
        int length = dtcString.length();
        //将故障码串分割成一个一个的故障码
        while (length/8>0){
            String detailDtc = dtcString.substring(0,8);
            DtcMessage dtcMessage = new DtcMessage(detailDtc.substring(0,6));
            dtcMessages.add(dtcMessage);
            dtcString = dtcString.substring(8,length-1);
            length = dtcString.length();
        }
    }


    DtcMessage tempDtcMessage;
    //将冻结帧分类置各个故障码中
    public void divideFrozenMessage(){
        String frozenMessage;
        //先清空dtc中的故障帧
        for(DtcMessage dtcMessage:dtcMessages){
            dtcMessage.getFrozenMessages().clear();
        }
        for(int i = 0;i<frozenMessages.size();i++){
            frozenMessage = frozenMessages.get(i);
            if(frozenMessage.contains("037F1931")){
                continue;
            }
            if(frozenMessage.contains("5904")){
                String dtc = frozenMessage.substring(12,18);
                for(int j = 0;j<dtcMessages.size();j++){
                    if(dtcMessages.get(j).getDtc().equals(dtc)){
                        tempDtcMessage = dtcMessages.get(j);
                        tempDtcMessage.setHas_frozenMessage(true);
                    }
                }
            }
            tempDtcMessage.getFrozenMessages().add(frozenMessage);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.getErrorMenu:
                getDtcMenu.setTextColor(getResources().getColor(R.color.menu_select_color));
                getFrozenMenu.setTextColor(getResources().getColor(R.color.dlg_text_color));
                clearDtcMenu.setTextColor(getResources().getColor(R.color.dlg_text_color));
                getVersionMenu.setTextColor(getResources().getColor(R.color.dlg_text_color));
                dtcLayout.setVisibility(View.VISIBLE);
                frozenLayout.setVisibility(View.GONE);
                clearDtcLayout.setVisibility(View.GONE);
                versionLayout.setVisibility(View.GONE);
                MainActivity.tb.setTitle("故障码");
                break;
            case R.id.getFrozenMenu:
                getDtcMenu.setTextColor(getResources().getColor(R.color.dlg_text_color));
                getFrozenMenu.setTextColor(getResources().getColor(R.color.menu_select_color));
                clearDtcMenu.setTextColor(getResources().getColor(R.color.dlg_text_color));
                getVersionMenu.setTextColor(getResources().getColor(R.color.dlg_text_color));
                dtcLayout.setVisibility(View.GONE);
                frozenLayout.setVisibility(View.VISIBLE);
                clearDtcLayout.setVisibility(View.GONE);
                versionLayout.setVisibility(View.GONE);
                MainActivity.tb.setTitle("冻结帧");
                break;
            case R.id.clearErrorCodeMenu:
                getDtcMenu.setTextColor(getResources().getColor(R.color.dlg_text_color));
                getFrozenMenu.setTextColor(getResources().getColor(R.color.dlg_text_color));
                clearDtcMenu.setTextColor(getResources().getColor(R.color.menu_select_color));
                getVersionMenu.setTextColor(getResources().getColor(R.color.dlg_text_color));
                dtcLayout.setVisibility(View.GONE);
                frozenLayout.setVisibility(View.GONE);
                clearDtcLayout.setVisibility(View.VISIBLE);
                versionLayout.setVisibility(View.GONE);
                MainActivity.tb.setTitle("清除故障码");
                break;
            case R.id.getVersionMenu:
                getDtcMenu.setTextColor(getResources().getColor(R.color.dlg_text_color));
                getFrozenMenu.setTextColor(getResources().getColor(R.color.dlg_text_color));
                clearDtcMenu.setTextColor(getResources().getColor(R.color.dlg_text_color));
                getVersionMenu.setTextColor(getResources().getColor(R.color.menu_select_color));
                dtcLayout.setVisibility(View.GONE);
                frozenLayout.setVisibility(View.GONE);
                clearDtcLayout.setVisibility(View.GONE);
                versionLayout.setVisibility(View.VISIBLE);
                MainActivity.tb.setTitle("版本信息");
                break;
            case R.id.startGetDtcTextView:
                if(Constant.is_readFrozenMessage){
                    Toast.makeText(getActivity(),"正在读取冻结帧消息，请等待检测完成之后再进行操作",Toast.LENGTH_SHORT).show();
                    return;
                }
                dtcRawMessages = new ArrayList<>();
                dtcMessages = new ArrayList<>();
                m.sendFragmentRequest("getDtc");
                mode = "getDtc";
                if(Constant.is_connect){
                    showWaitDialog();
                }
                break;
            case R.id.startGetFrozenTextView:
                if(dtcMessages.size()==0){
                    Toast.makeText(getActivity(),"尚未读取故障码或没有故障码，请读取故障码后进行此操作",Toast.LENGTH_SHORT).show();
                    return;
                }
                frozenMessages = new ArrayList<>();
                m.sendFragmentRequest("getFrozen");
                mode = "getFrozen";
                frozenIndex = 0;
                if(Constant.is_connect){
                    progressBar.setVisibility(View.VISIBLE);
                    Constant.is_readFrozenMessage = true;
                }
                break;
            case R.id.clearDtcTextView:
                if(Constant.is_readFrozenMessage){
                    Toast.makeText(getActivity(),"正在读取冻结帧消息，请等待检测完成之后再进行操作",Toast.LENGTH_SHORT).show();
                    return;
                }
                mAlternativeDialog = new AlternativeDialog(getActivity(),R.style.dialog);
                mAlternativeDialog.setOnSureButtonClickListener("确定", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        m.sendFragmentRequest("clearDtc");
                        mode = "clearDtc";
                    }
                });
                mAlternativeDialog.setOnCancelButtonClickListener("取消", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mAlternativeDialog.dismiss();
                    }
                });
                mAlternativeDialog.setMsg("确定清除故障码？此操作不可撤销！");
                mAlternativeDialog.show();
                break;
            case R.id.getVersionTextView:
                if(Constant.is_readFrozenMessage){
                    Toast.makeText(getActivity(),"正在读取冻结帧消息，请等待检测完成之后再进行操作",Toast.LENGTH_SHORT).show();
                    return;
                }
                versionMessages = new ArrayList<>();
                m.sendFragmentRequest("getVersion");
                mode = "getVersion";
                if(Constant.is_connect){
                    showWaitDialog();
                }
                break;
        }
    }

    //等待进度条
    public void showWaitDialog(){
        mWaitDialog = new WaitDialog(getActivity(), R.style.dialog);
        mWaitDialog.setTips("获取中...");
        mWaitDialog.show();
    }


    @Override
    public void onResume() {
        super.onResume();
        if(adapter!=null){
            dtcListView.setAdapter(adapter);
            if(frozenIndex!=dtcMessages.size()){
                progressBar.setVisibility(View.VISIBLE);
            }
        }
        if(fmAdapter!=null){
            fmListView.setAdapter(fmAdapter);
        }
        if(hardwareVersion!=null){
            hardVersionTextView.setText(hardwareVersion);
            softVersionTextView.setText(softwareVersion);
            supplyVersionTextView.setText(supplycode);
        }
    }
}