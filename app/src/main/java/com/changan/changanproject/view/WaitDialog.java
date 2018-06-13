package com.changan.changanproject.view;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.changan.changanproject.R;


public class WaitDialog extends Dialog {

	private TextView mTextViewTips;
	private Context mContext;

	public WaitDialog(Context context, int theme) {
		super(context, theme);
		// TODO Auto-generated constructor stub
		mContext = context;
		init();
	}

	private void init() {
		// TODO Auto-generated method stub
		setCancelable(false);
		LayoutInflater layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = layoutInflater.inflate(R.layout.connect_view, null);
		mTextViewTips = (TextView) view.findViewById(R.id.textViewCnnTips);
		setContentView(view);
	}

	public void setTips(String tips) {
		if (mTextViewTips != null) {
			mTextViewTips.setText(tips);
		}
	}
}
