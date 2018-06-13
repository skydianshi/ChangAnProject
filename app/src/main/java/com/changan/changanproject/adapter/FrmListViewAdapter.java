package com.changan.changanproject.adapter;

import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.changan.changanproject.R;
import com.changan.changanproject.view.FrmItemView;
import com.changan.changanproject.util.TimeGetter;

import cn.zlg.www.ItemData;


public class FrmListViewAdapter extends BaseAdapter {

	public static final int MODE_FLOW = 0;
	public static final int MODE_SORT = 1;

	private LayoutInflater mLayoutInflater;
	private FrmCycleBuffer mList;
	private int mMode;
	private Resources mResources;
	private Integer mIntegerTmp;


	public FrmListViewAdapter(int nMode) {
		mMode = nMode;
		mList = new FrmCycleBuffer(3000,
				MODE_SORT == mMode ? FrmCycleBuffer.Type.TYPE_SORT : FrmCycleBuffer.Type.TYPE_ALL);
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
	public View getView(int nPos, View convertview, ViewGroup parent) {
		// TODO Auto-generated method stub
		FrmItemView itemView;
		if (convertview == null) {
			itemView = new FrmItemView();
			convertview = mLayoutInflater.inflate(R.layout.frm_item, null);
			itemView.textViewIndex = (TextView) convertview
					.findViewById(R.id.itemIndex);
			itemView.textViewID = (TextView) convertview
					.findViewById(R.id.itemID);
			itemView.textViewType = (TextView) convertview
					.findViewById(R.id.itemType);
			itemView.textViewTime = (TextView) convertview
					.findViewById(R.id.itemTime);
			itemView.textViewData = (TextView) convertview
					.findViewById(R.id.itemData);
			itemView.textViewCount = (TextView) convertview
					.findViewById(R.id.itemCount);
			itemView.textViewInterval = (TextView) convertview
					.findViewById(R.id.itemInterval);
			convertview.setTag(itemView);
		} else {
			itemView = (FrmItemView) convertview.getTag();
		}
		ItemData itemData = mList.get(nPos);
		itemView.textViewIndex.setText(String.format("%08d", nPos + 1));
		itemView.textViewID.setText(String.format("%08x", itemData.nID));
		String string = itemData.bExtend ? "扩展":"标准";
		string += itemData.bRemote ?"远程帧" : "数据帧";
		itemView.textViewType.setText(string);

		if (MODE_FLOW == mMode) {
			itemView.textViewCount.setVisibility(View.GONE);
			itemView.textViewInterval.setVisibility(View.GONE);
		} else {
			itemView.textViewCount.setVisibility(View.VISIBLE);
			itemView.textViewInterval.setVisibility(View.VISIBLE);
			itemView.textViewCount.setText(itemData.count + "");
			itemView.textViewInterval.setText(itemData.getIntervalString());
		}

		itemView.textViewTime.setText(TimeGetter.getTimeString(itemData.nTime));
		if (!itemData.bRemote) {
			int nLen = itemData.nDataLen;
			string = "";
			for (int i = 0; i < nLen; i++) {
				string += String.format("%02x ", itemData.datas[i]);
			}
			itemView.textViewData.setText(string);
		} else {
			itemView.textViewData.setText("");
		}
		if (!itemData.bRecv) {
			convertview.setBackgroundResource(R.color.antiquewhite);
		} else {
			convertview.setBackgroundResource(R.color.white);
		}

		return convertview;
	}

	public synchronized void add(ItemData itemData) {
		if (MODE_SORT == mMode) {
			if ((mIntegerTmp = mList.find(itemData.nID)) != null) {
				mList.replace(mIntegerTmp, itemData);
				return;
			}
		}
		mList.push(itemData);
	}

	public void setLayoutInflater(LayoutInflater layoutInflater) {
		this.mLayoutInflater = layoutInflater;
		mResources = mLayoutInflater.getContext().getResources();
	}

	public void clear() {
		mList.clear();
	}

	public int getMode() {
		return mMode;
	}

	public void setListMode(int bMode) {
		if (mMode == bMode) {
			return;
		}
		mMode = bMode;
		mList.clear();
	}

	public CycleBuffer<ItemData> getList() {
		return mList;
	}

	public synchronized void setSize(int nSizeNew) {
		mList = new FrmCycleBuffer(nSizeNew,
				MODE_SORT == mMode ? FrmCycleBuffer.Type.TYPE_SORT : FrmCycleBuffer.Type.TYPE_ALL);
	}
}
