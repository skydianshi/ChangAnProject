package com.changan.changanproject.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.changan.changanproject.R;
import com.changan.changanproject.activity.MainActivity;
import com.changan.changanproject.activity.SendActivity;
import com.changan.changanproject.adapter.CycleBuffer;
import com.changan.changanproject.adapter.ErrListViewAdapter;
import com.changan.changanproject.adapter.FrmListViewAdapter;
import com.changan.changanproject.constant.Constant;
import com.changan.changanproject.listener.DataServiceListener;

import cn.zlg.www.ControlCAN;
import cn.zlg.www.ErrData;
import cn.zlg.www.ItemData;

public class DataFragment  extends Fragment{
    private static String TAG = "DataFragment";
    private ListView mRecvListView, mSendListView, mErrListView;
    private FrmListViewAdapter mRecvListAdapter, mSendListAdapter;
    private ErrListViewAdapter mErrListAdapter;
    private LinearLayout mLayoutRecvList, mLayoutSendList, mLayoutErrList;
    private boolean mRoll = true;
    private OnViewClickListener mViewClickListener;
    private MainActivity mActivity;
    private boolean mRecvListVisible = true;
    private boolean mSendListVisible = true;
    private boolean mErrListVisible = true;
    private DataServiceListener dsListener;
    ImageView receivePlay;


    ItemData receiveData;
    ItemData sendData;
    ErrData errData;
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0://更新接收列表
                    receiveData = (ItemData)msg.obj;
                    addFrmRecv(receiveData);
                    refreshRecvList();
                    break;
                case 1://更新发送列表
                    sendData = (ItemData)msg.obj;
                    addFrmSent(sendData);
                    refreshSendList();
                    break;
                case 2://更新错误信息列表
                    errData = (ErrData)msg.obj;
                    addErrData(errData);
                    refreshErrList();
                    break;
            }
            super.handleMessage(msg);
        }
    };

    public DataFragment() {
        super();
        mRecvListAdapter = new FrmListViewAdapter(FrmListViewAdapter.MODE_SORT);
        mSendListAdapter = new FrmListViewAdapter(FrmListViewAdapter.MODE_FLOW);
        mErrListAdapter = new ErrListViewAdapter();
        mViewClickListener = new OnViewClickListener();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        View view = inflater.inflate(R.layout.fragment_data, container, false);
        initView(view, inflater);
        initConnection();
        return view;
    }

    //建立与Activity的连接
    public void initConnection(){
        dsListener = new DataServiceListener() {
            @Override
            public void addFrameData(ItemData itemData) {
                Message receiveMessage = new Message();
                receiveMessage.obj = itemData;
                receiveMessage.what = 0;
                handler.sendMessage(receiveMessage);
            }
            @Override
            public void addSendFrame(ItemData itemData) {
                Message sendMessage = new Message();
                sendMessage.obj = itemData;
                sendMessage.what = 1;
                handler.sendMessage(sendMessage);
            }

            @Override
            public void addErrFrame(ErrData errData) {
                Message errMessage = new Message();
                errMessage.obj = errData;
                errMessage.what = 2;
                handler.sendMessage(errMessage);
            }
        };
        MainActivity.setDataListener(dsListener);
    }

    private void initView(View view, LayoutInflater inflater) {
        // TODO Auto-generated method stub
        if (null == mActivity) {
            mActivity = (MainActivity) getActivity();
        }
        LinearLayout layout = (LinearLayout) view.findViewById(R.id.LinearLayoutRecvEx);
        layout.setOnClickListener(mViewClickListener);
        layout = (LinearLayout)view.findViewById(R.id.LinearLayoutSendMenu);
        layout.setOnClickListener(mViewClickListener);
        layout = (LinearLayout)view.findViewById(R.id.LinearLayoutErrorClear);
        layout.setOnClickListener(mViewClickListener);
        layout = (LinearLayout)view.findViewById(R.id.LinearLayoutReadClear);
        layout.setOnClickListener(mViewClickListener);
        layout = (LinearLayout)view.findViewById(R.id.LinearLayoutSendClear);
        layout.setOnClickListener(mViewClickListener);
        if (null == mRecvListView) {
            mRecvListAdapter.setLayoutInflater(inflater);
        }
        if (null == mSendListView) {
            mSendListAdapter.setLayoutInflater(inflater);
        }
        if (null == mErrListView) {
            mErrListAdapter.setLayoutInflater(inflater);
        }
        mRecvListView = (ListView) view.findViewById(R.id.listViewRecv);
        mRecvListView.setOnScrollListener(new onListViewScrollListener());
        mRecvListView.setAdapter(mRecvListAdapter);
        mSendListView = (ListView) view.findViewById(R.id.listViewSend);
        mSendListView.setOnScrollListener(new onListViewScrollListener());
        mSendListView.setAdapter(mSendListAdapter);
        mErrListView = (ListView) view.findViewById(R.id.listViewErr);
        mErrListView.setOnScrollListener(new onListViewScrollListener());
        mErrListView.setAdapter(mErrListAdapter);
        mLayoutRecvList = (LinearLayout) view.findViewById(R.id.recvlistlayout);
        mLayoutRecvList.setVisibility(mRecvListVisible ? View.VISIBLE : View.GONE);
        mLayoutSendList = (LinearLayout) view.findViewById(R.id.sendlistlayout);
        mLayoutSendList.setVisibility(mSendListVisible ? View.VISIBLE : View.GONE);
        mLayoutErrList = (LinearLayout) view.findViewById(R.id.errlistlayout);
        mLayoutErrList.setVisibility(mErrListVisible ? View.VISIBLE : View.GONE);
        RelativeLayout relativeLayout = (RelativeLayout) view.findViewById(R.id.RelativeLayoutFrmRecvEx);
        relativeLayout.setOnClickListener(mViewClickListener);
        relativeLayout = (RelativeLayout) view.findViewById(R.id.RelativeLayoutFrmSendEx);
        relativeLayout.setOnClickListener(mViewClickListener);
        relativeLayout = (RelativeLayout) view.findViewById(R.id.RelativeLayoutFrmErrEx);
        relativeLayout.setOnClickListener(mViewClickListener);
        receivePlay = (ImageView)view.findViewById(R.id.receivePlay);
        if(Constant.is_read){
            receivePlay.setBackground(getResources().getDrawable(R.mipmap.icon_stop));
        }else {
            receivePlay.setBackground(getResources().getDrawable(R.mipmap.icon_play));
        }
    }

    public synchronized void addFrmRecv(ItemData itemData) {
        mRecvListAdapter.add(itemData);
    }

    public synchronized void addFrmSent(ItemData itemData) {
        mSendListAdapter.add(itemData);
    }

    public synchronized void addErrData(ErrData errData) {
        mErrListAdapter.add(errData);
    }

    public void refreshErrList() {
        // TODO Auto-generated method stub
        mErrListAdapter.notifyDataSetChanged();
        if (mRoll && mErrListVisible) {
            mErrListView.setSelection(mErrListAdapter.getCount() - 1);
        }
    }

    public void refreshRecvList() {
        // TODO Auto-generated method stub
        mRecvListAdapter.notifyDataSetChanged();
        if (mRoll && mRecvListVisible) {
            mRecvListView.setSelection(mRecvListAdapter.getCount() - 1);
        }
    }

    public void refreshSendList() {
        mSendListAdapter.notifyDataSetChanged();
        if (mRoll && mSendListVisible) {
            mSendListView.setSelection(mSendListAdapter.getCount() - 1);
        }
    }


    public void clearRecvList() {
        mRecvListAdapter.clear();
        mRecvListView.setAdapter(mRecvListAdapter);
    }

    public void clearSendList() {
        mSendListAdapter.clear();
        mSendListView.setAdapter(mSendListAdapter);
    }

    public void clearErrList() {
        mErrListAdapter.clear();
        mErrListView.setAdapter(mErrListAdapter);
    }

    public CycleBuffer<ItemData> getRecvList() {
        return mRecvListAdapter.getList();
    }

    public CycleBuffer<ItemData> getSentList() {
        return mSendListAdapter.getList();
    }

    public CycleBuffer<ErrData> getErrList() {
        return mErrListAdapter.getList();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case 0:
                try {
                    int count = data.getIntExtra("count",1);
                    int timeInterval = data.getIntExtra("timeInterval",1);
                    int frmInterval = data.getIntExtra("frmInterval",1);
                    mActivity.sendFrames(count,timeInterval,frmInterval);
                }catch (NullPointerException e){
                    Log.e(TAG,"取消发送");
                }
                break;
        }
    }

    private class OnViewClickListener implements View.OnClickListener {

        private final long DOULE_CLICK_INTERVAL = 500;
        private long mRecvTimeCount;
        private long mSendTimeCount;
        private long mErrTimeCount;

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            switch (v.getId()) {
                case R.id.LinearLayoutRecvEx://开关接收
                    if(!Constant.is_connect){
                        Toast.makeText(getActivity(),"请先进行连接再读取数据",Toast.LENGTH_SHORT).show();
                        break;
                    }
                    if(Constant.is_read){
                        receivePlay.setBackground(getResources().getDrawable(R.mipmap.icon_play));
                        Constant.is_read = false;
                    }else {
                        //暂停之后再读取一定要先清空硬件缓冲区，不然一下子会取出好多数据，手机直接卡死
                        new ControlCAN().clearBuffer();
                        receivePlay.setBackground(getResources().getDrawable(R.mipmap.icon_stop));
                        Constant.is_read = true;
                    }
                    break;
                case R.id.LinearLayoutSendMenu:
                    Intent sendIntent = new Intent(getActivity(),SendActivity.class);
                    startActivityForResult(sendIntent,0);
                    break;
                case R.id.LinearLayoutSendClear:
                    clearSendList();
                    break;
                case R.id.LinearLayoutReadClear:
                    clearRecvList();
                    break;
                case R.id.LinearLayoutErrorClear:
                    clearErrList();
                    break;
                case R.id.RelativeLayoutFrmRecvEx://折叠页面
                    if (System.currentTimeMillis() - mRecvTimeCount <= DOULE_CLICK_INTERVAL) {
                        mLayoutRecvList.setVisibility(mRecvListVisible ? View.GONE
                                : View.VISIBLE);
                        mRecvListVisible = !mRecvListVisible;
                    } else {
                        mRecvTimeCount = System.currentTimeMillis();
                    }
                    break;
                case R.id.RelativeLayoutFrmSendEx:
                    if (System.currentTimeMillis() - mSendTimeCount <= DOULE_CLICK_INTERVAL) {
                        mLayoutSendList.setVisibility(mSendListVisible ? View.GONE
                                : View.VISIBLE);
                        mSendListVisible = !mSendListVisible;
                    } else {
                        mSendTimeCount = System.currentTimeMillis();
                    }
                    break;
                case R.id.RelativeLayoutFrmErrEx:
                    if (System.currentTimeMillis() - mErrTimeCount <= DOULE_CLICK_INTERVAL) {
                        mLayoutErrList.setVisibility(mErrListVisible ? View.GONE
                                : View.VISIBLE);
                        mErrListVisible = !mErrListVisible;
                    } else {
                        mErrTimeCount = System.currentTimeMillis();
                    }
                    break;
            }
        }

    }


    public synchronized void setSize(int nSizeNew) {
        mRecvListAdapter.setSize(nSizeNew);
        if (mRecvListView != null) {
            mRecvListView.setAdapter(mRecvListAdapter);
        }
        mSendListAdapter.setSize(nSizeNew);
        if (mSendListView != null) {
            mSendListView.setAdapter(mSendListAdapter);
        }
        mErrListAdapter.setSize(nSizeNew);
        if (mErrListView != null) {
            mErrListView.setAdapter(mErrListAdapter);
        }
    }

    private class onListViewScrollListener implements AbsListView.OnScrollListener {

        @Override
        public void onScroll(AbsListView arg0, int arg1, int arg2, int arg3) {

        }

        @Override
        public void onScrollStateChanged(AbsListView view, int nScrollState) {
            // TODO Auto-generated method stub
            mRoll = view.getLastVisiblePosition() == (view.getCount() - 1) ? true
                    : false;
        }

    }
}
