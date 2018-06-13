package com.changan.changanproject.view;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.changan.changanproject.R;


public class AlternativeDialog extends Dialog {

	private TextView mMsgTextView;
	private TextView mTextViewCancel, mTextViewSure;
	private Context mContext;

	public AlternativeDialog(Context context, int theme) {
		super(context, theme);
		// TODO Auto-generated constructor stub
		mContext = context;
		init();
	}

	public void setMsg(int nStrID) {
		mMsgTextView.setText(nStrID);
	}

	public void setMsg(String str) {
		mMsgTextView.setText(str);
	}

	private void init() {
		// TODO Auto-generated method stub
		LayoutInflater layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = layoutInflater.inflate(R.layout.alternative_text, null);
		mMsgTextView = (TextView) view.findViewById(R.id.textViewAlternative);
		mTextViewCancel = (TextView) view.findViewById(R.id.textViewCancel);
		mTextViewSure = (TextView) view.findViewById(R.id.textViewSure);
		setContentView(view);
	}

	public void setOnSureButtonClickListener(String nStrID,
			View.OnClickListener onClickListener) {
		if (mTextViewSure != null) {
			mTextViewSure.setText(nStrID);
			mTextViewSure.setOnClickListener(onClickListener);
		}
	}

	public void setOnCancelButtonClickListener(String nStrID,
			View.OnClickListener onClickListener) {
		if (mTextViewCancel != null) {
			mTextViewCancel.setText(nStrID);
			mTextViewCancel.setOnClickListener(onClickListener);
		}
	}
}
