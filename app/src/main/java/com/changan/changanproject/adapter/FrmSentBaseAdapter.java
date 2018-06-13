package com.changan.changanproject.adapter;

import android.content.res.Resources;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.changan.changanproject.R;
import com.changan.changanproject.util.CommonTool;

import java.util.ArrayList;


public class FrmSentBaseAdapter extends BaseAdapter {

	public enum ViewType {
		Type_Check, Type_Format, Type_FrmType
	}

	public class Item {
		public boolean checked;
		public int index;
		public boolean extend;
		public boolean remote;
		public long id;
		public String data;
	}

	private class ItemView {
		public LinearLayout layoutCheck;
		public TextView checkBox;
		public TextView index;
		public TextView format;
		public TextView type;
		public EditText id;
		public EditText data;
		public int ItemIndex;
	}

	public final int MAX_COUNT = 100;
	private LayoutInflater mLayoutInflater;
	private Resources mResources;
	private static boolean sInit = false;
	private static ArrayList<Item> sList;

	public FrmSentBaseAdapter() {
		super();
		if (!sInit) {
			initList();
			sInit = true;
		}
	}

	private void initList() {
		// TODO Auto-generated method stub
		sList = new ArrayList<Item>(MAX_COUNT);
		for (int i = 0; i < MAX_COUNT; i++) {
			Item item = new Item();
			item.checked = false;
			item.extend = false;
			item.remote = false;
			item.id = 0x000007A0;
			item.index = i;
			item.data = "00 11 22 33 44 55 66 77";
			sList.add(item);
		}
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return sList.size();
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
	public View getView(int nPos, View convertview, ViewGroup vg) {
		// TODO Auto-generated method stub
		ItemView itemView;
		Item item = sList.get(nPos);
		if (null == convertview) {
			itemView = new ItemView();
			convertview = mLayoutInflater.inflate(R.layout.frm_send_set, null);
			itemView.layoutCheck = (LinearLayout) convertview
					.findViewById(R.id.layoutCheck);

			itemView.checkBox = (TextView) convertview
					.findViewById(R.id.checkBoxFrmSend);
			itemView.checkBox.setClickable(false);
			itemView.layoutCheck = (LinearLayout) convertview
					.findViewById(R.id.layoutCheck);
			itemView.layoutCheck.setOnClickListener(new OnViewClickListener(
					ViewType.Type_Check, itemView));
			itemView.index = (TextView) convertview
					.findViewById(R.id.textViewFrmSendIndex);
			itemView.format = (TextView) convertview
					.findViewById(R.id.textViewFrmSendSt);
			itemView.format.setOnClickListener(new OnViewClickListener(
					ViewType.Type_Format, itemView));
			itemView.type = (TextView) convertview
					.findViewById(R.id.textViewFrmSendType);
			itemView.type.setOnClickListener(new OnViewClickListener(
					ViewType.Type_FrmType, itemView));
			itemView.id = (EditText) convertview
					.findViewById(R.id.editTextFrmSendID);
			itemView.id.addTextChangedListener(new IDTextWatcherEx(true,
					itemView));
			itemView.data = (EditText) convertview
					.findViewById(R.id.editTextFrmSendData);
			itemView.data.addTextChangedListener(new IDTextWatcherEx(false,
					itemView));
			convertview.setTag(itemView);
		} else {
			itemView = (ItemView) convertview.getTag();
		}

		itemView.ItemIndex = nPos;
		itemView.checkBox.setBackgroundResource(item.checked ? R.mipmap.check
				: R.mipmap.uncheck);
		itemView.index.setText("" + item.index);
		itemView.format.setText(item.extend ? R.string.strFrmExtend
				: R.string.strFrmST);
		itemView.format.setTextColor(item.extend ? mResources
				.getColor(R.color.cornflowerblue) : mResources
				.getColor(R.color.button_text));
		itemView.type.setText(item.remote ? R.string.strRemoteFrm
				: R.string.strDataFrm);
		itemView.type.setTextColor(item.remote ? mResources
				.getColor(R.color.cornflowerblue) : mResources
				.getColor(R.color.button_text));
		itemView.id.setText(String.format("%08x", item.id));
		itemView.data.setText(item.data);

		return convertview;
	}

	public void setLayoutInflater(LayoutInflater layoutInflater) {
		mLayoutInflater = layoutInflater;
		mResources = mLayoutInflater.getContext().getResources();
	}

	public ArrayList<Item> getSendList() {
		ArrayList<Item> arrayList = new ArrayList<Item>();
		int nSize = sList.size();
		Item item;
		for (int i = 0; i < nSize; i++) {
			item = sList.get(i);
			if (item.checked) {
				arrayList.add(item);
			}
		}
		return arrayList;
	}

	private class IDTextWatcherEx implements TextWatcher {

		private final int IDMAXLEN = 8;
		private final int DATAMAXLEN = 23;
		private boolean mIdPart = true;
		private ItemView itemView;

		public IDTextWatcherEx(Boolean bId, ItemView itemView) {
			mIdPart = bId;
			this.itemView = itemView;
		}

		@Override
		public void afterTextChanged(Editable arg0) {
			// TODO Auto-generated method stub
		}

		@Override
		public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
									  int arg3) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onTextChanged(CharSequence cs, int nStart, int before,
								  int count) {
			// TODO Auto-generated method stub
			String str = cs.toString();
			int nLenBefore = cs.length();
			if (nLenBefore <= 0 || nStart == nLenBefore) {
				setData(str);
				return;
			}
			char ch;
			int nLenTmp;
			int nCountIsNotHexChar = 0;
			for (int i = 0; i < count; i++) {
				nLenTmp = str.length();
				ch = str.charAt(nStart);
				if (!CommonTool.IsHexChar(ch) && (ch != ' ' || mIdPart)) {
					nCountIsNotHexChar++;
					str = str.substring(0, nStart - 1 >= 0 ? nStart : 0)
							+ (nStart + 1 >= nLenTmp ? "" : str.substring(
									nStart + 1, nLenTmp));
				}
			}
			nLenTmp = str.length();
			if (mIdPart && nLenTmp > IDMAXLEN) {
				String string = str.substring(0, IDMAXLEN);
				itemView.id.setText(string);
				setData(string);
				itemView.id.setSelection(IDMAXLEN);
				return;
			}
			if (!mIdPart && nLenTmp > DATAMAXLEN) {
				String string = str.substring(0, DATAMAXLEN);
				itemView.data.setText(string);
				setData(string);
				itemView.data.setSelection(DATAMAXLEN);
				return;
			}
			if (nCountIsNotHexChar > 0) {
				(mIdPart ? itemView.id : itemView.data).setText(str);
				(mIdPart ? itemView.id : itemView.data).setSelection(nStart
						+ count - nCountIsNotHexChar);
			}
			setData(str);
		}

		private void setData(String string) {
			Item item = sList.get(itemView.ItemIndex);
			if (mIdPart) {
				try {
					item.id = Long.parseLong(string, 16);
					if ((item.extend && item.id > 0x1FFFFFFF)
							|| (!item.extend && item.id > 0x7FF)) {
						item.id = 0;
						itemView.id.setText("00000000");
					}
				} catch (NumberFormatException e) {
					// TODO: handle exception
					item.id = 0;
				}
			} else {
				item.data = string;
			}
		}
	}

	private class OnViewClickListener implements OnClickListener {

		private ViewType mType;
		private ItemView itemView;

		public OnViewClickListener(ViewType type, ItemView itemView) {
			mType = type;
			this.itemView = itemView;
		}

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Item item = sList.get(itemView.ItemIndex);
			if (ViewType.Type_FrmType == mType) {
				TextView tv = (TextView) v;
				item.remote = !item.remote;
				tv.setText(item.remote ? R.string.strRemoteFrm
						: R.string.strDataFrm);
				tv.setTextColor(item.remote ? mResources
						.getColor(R.color.cornflowerblue) : mResources
						.getColor(R.color.button_text));
			} else if (ViewType.Type_Format == mType) {
				TextView tv = (TextView) v;
				item.extend = !item.extend;
				tv.setText(item.extend ? R.string.strFrmExtend
						: R.string.strFrmST);
				tv.setTextColor(item.extend ? mResources
						.getColor(R.color.cornflowerblue) : mResources
						.getColor(R.color.button_text));
				if (!item.extend && item.id > CommonTool.ST_FRM_MAX_ID) {
					item.id = 0;
					itemView.id.setText("00000000");
				}
			} else {
				item.checked = !item.checked;
				itemView.checkBox
						.setBackgroundResource(item.checked ? R.mipmap.check
								: R.mipmap.uncheck);
			}
		}

	}
}
