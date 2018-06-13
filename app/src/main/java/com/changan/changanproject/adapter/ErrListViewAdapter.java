package com.changan.changanproject.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.changan.changanproject.R;
import com.changan.changanproject.view.ErrItemView;
import com.changan.changanproject.util.TimeGetter;

import cn.zlg.www.ErrData;


public class ErrListViewAdapter extends BaseAdapter {

	private CycleBuffer<ErrData> mList;
	private LayoutInflater mLayoutInflater;

	public ErrListViewAdapter() {
		super();
		mList = new CycleBuffer<ErrData>(3000);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mList.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int index, View convertview, ViewGroup arg2) {
		// TODO Auto-generated method stub
		ErrItemView errItemView;
		if (null == convertview) {
			errItemView = new ErrItemView();
			convertview = mLayoutInflater.inflate(R.layout.errinfi, null);
			errItemView.textViewIndex = (TextView) convertview
					.findViewById(R.id.textViewErrIndex);
			errItemView.textViewCmd = (TextView) convertview
					.findViewById(R.id.textViewErrCmd);
			errItemView.textViewCount = (TextView) convertview
					.findViewById(R.id.textViewErrCount);
			errItemView.textViewInfo = (TextView) convertview
					.findViewById(R.id.textViewErrInfo);
			errItemView.textViewTime = (TextView) convertview
					.findViewById(R.id.textViewErrTime);
			convertview.setTag(errItemView);
		} else {
			errItemView = (ErrItemView) convertview.getTag();
		}
		ErrData errData = mList.get(index);
		errItemView.textViewIndex.setText(String.format("%08d", index + 1));
		errItemView.textViewTime.setText(TimeGetter
				.getTimeString(errData.nTime));
		errItemView.textViewCmd.setText(String
				.format("%02X", errData.nCmdValue));
		errItemView.textViewCount.setText(errData.nCount + "");
		errItemView.textViewInfo.setText(getStatusInfo(errData.nCmdValue));

		return convertview;
	}

	public void setLayoutInflater(LayoutInflater layoutInflater) {
		this.mLayoutInflater = layoutInflater;
	}

	public static int getStatusInfo(byte nCmd) {
		switch (nCmd) {
		case 0x01:
			return R.string.strErr01;
		case 0x02:
			return R.string.strErr02;
		case 0x03:
			return R.string.strErr03;
		case 0x04:
			return R.string.strErr04;
		case 0x05:
			return R.string.strErr05;
		case 0x06:
			return R.string.strErr06;
		case 0x07:
			return R.string.strErr07;
		case 0x08:
			return R.string.strErr08;
		case 0x09:
			return R.string.strErr09;
		case 0x0A:
			return R.string.strErr0A;
		case 0x0B:
			return R.string.strErr0B;
		}
		return R.string.strErr0C;
	}

	public void clear() {
		// TODO Auto-generated method stub
		mList.clear();
	}

	public synchronized void add(ErrData obj) {
		// TODO Auto-generated method stub
		mList.push(obj);
	}

	public CycleBuffer<ErrData> getList() {
		return mList;
	}
	
	public synchronized void setSize(int nSizeNew) {
		mList = new CycleBuffer<ErrData>(nSizeNew);
	}
}
