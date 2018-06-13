package com.changan.changanproject.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.changan.changanproject.R;
import com.changan.changanproject.activity.BaseActivity;
import com.changan.changanproject.adapter.FrmSentBaseAdapter;
import com.changan.changanproject.constant.Constant;

import java.util.ArrayList;

import cn.zlg.www.ItemData;


public class SendActivity extends BaseActivity{

	private FrmSentBaseAdapter mFrmSentBaseAdapter;
	private ListView mFrmSentListView;
	private LayoutInflater mLayoutInflater;
	private TextView mSendType;
	private TextView mSendButton;
	private PopupWindow mPopupWindow;
	private boolean mSendNormal = true;
	private boolean mSending = false;
	private OnItemClickListener mOnItemClickListener;

	public static ArrayList<FrmSentBaseAdapter.Item> sendList;


	@Override
	protected View getContentView() {
		//隐藏状态栏
		//定义全屏参数
		int flag= WindowManager.LayoutParams.FLAG_FULLSCREEN;
		//设置当前窗体为全屏显示
		getWindow().setFlags(flag, flag);
		return inflateView(R.layout.activity_frm_send);
	}

	@Override
	protected void setContentViewAfter(View contentView) {
		initView();
	}

	public void initView(){
		mOnItemClickListener = new OnItemClickListener();
		mFrmSentBaseAdapter = new FrmSentBaseAdapter();
		mLayoutInflater = getLayoutInflater();
		mFrmSentBaseAdapter.setLayoutInflater(mLayoutInflater);
		mFrmSentListView = (ListView)findViewById(R.id.ListFrmSent);
		mFrmSentListView.setAdapter(mFrmSentBaseAdapter);

		mSendButton = (TextView)findViewById(R.id.TextViewSend);
		mSendButton.setText(mSending ? R.string.strSendStop : R.string.strSend);
		mSendButton.setOnClickListener(mOnItemClickListener);
	}



	private boolean sendFrm() {
		String string = ((EditText)findViewById(R.id.editTextSendTime)).getText().toString();
		int nCount = 1;
		int nTimeInterval = 1;
		int nFrmInterval = 1;
		try {
			nCount = Integer.parseInt(string);
		} catch (NumberFormatException e) {
			// TODO: handle exception
			nCount = 1;
			((EditText) findViewById(R.id.editTextSendTime)).setText("1");
		}
		string = ((EditText) findViewById(R.id.editTextSendInterval)).getText().toString();
		try {
			nTimeInterval = Integer.parseInt(string);
		} catch (NumberFormatException e) {
			// TODO: handle exception
			nTimeInterval = 1;
			((EditText)findViewById(R.id.editTextSendInterval)).setText("1");
		}
		string = ((EditText)findViewById(R.id.editTextFrmInterval)).getText().toString();
		try {
			nFrmInterval = Integer.parseInt(string);
		} catch (NumberFormatException e) {
			// TODO: handle exception
			nFrmInterval = 1;
			((EditText)findViewById(R.id.editTextFrmInterval))
					.setText("1");
		}
		Constant.is_send = true;
		Intent resultIntent = new Intent();
		resultIntent.putExtra("count",nCount);
		resultIntent.putExtra("timeInterval",nTimeInterval);
		resultIntent.putExtra("frmInterval",nFrmInterval);
		setResult(0,resultIntent);
		finish();
		return true;
	}

	private class OnItemClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.layoutSendTypeNormal:
				mPopupWindow.dismiss();
				mSendNormal = true;
				mSendType.setText(R.string.strSendNormal);
				break;
			case R.id.layoutSendTypeSelf:
				mPopupWindow.dismiss();
				mSendNormal = false;
				mSendType.setText(R.string.strSendSelf);
				break;
			case R.id.TextViewSend:
				if (!Constant.is_connect) {
					Toast.makeText(SendActivity.this, R.string.strUnConnect, Toast.LENGTH_SHORT).show();
					return;
				}
				if (!mSending) {
					sendList = mFrmSentBaseAdapter.getSendList();
					int nSize = sendList.size();
					if (0 == nSize) {
						Toast.makeText(SendActivity.this, R.string.strNoItem, Toast.LENGTH_SHORT).show();
						return;
					}
					mSendButton.setText(R.string.strSendStop);
					sendFrm();
				} else {

				}
				break;
			}
		}

	}

}
