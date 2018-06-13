package com.changan.changanproject.view;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.changan.changanproject.R;


public class ExportAlertDialog extends Dialog {


	public enum ListEnum {
		LIST_RECV, LIST_SEND, LIST_ERR
	};

	private static ListEnum mCurList = ListEnum.LIST_RECV;

	public EditText mFilePath;
	private ProgressBar mProgressBar;
	private TextView mPositiveBtnTextView, mNegativeBtnTextView,
			mTextViewSelect;
	private RelativeLayout mLayoutSelect;
	private Context mContext;
	private static String sFileName = "";
	private LinearLayout mListTypelayout;

	public ExportAlertDialog(Context context, int theme) {
		super(context, theme);
		// TODO Auto-generated constructor stub
		mContext = context;
		init();
	}


	private void init() {
		// TODO Auto-generated method stub
		setCancelable(false);
		LayoutInflater layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = layoutInflater.inflate(R.layout.export, null);
		mFilePath = (EditText) view.findViewById(R.id.editTextExportFileName);
		mFilePath.setText(sFileName);
		mProgressBar = (ProgressBar) view.findViewById(R.id.progressBarExport);
		mNegativeBtnTextView = (TextView) view.findViewById(R.id.textViewNegativeButton);
		mPositiveBtnTextView = (TextView) view.findViewById(R.id.textViewPositiveButton);
		mLayoutSelect = (RelativeLayout) view.findViewById(R.id.RelativeLayoutSelect);
		mLayoutSelect.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
			}
		});
		mTextViewSelect = (TextView) view.findViewById(R.id.textViewSelect);
		mListTypelayout = (LinearLayout) view.findViewById(R.id.layout_list_type);

		mNegativeBtnTextView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				dismiss();
			}
		});


		setContentView(view);
	}

	public void setOnPositiveLister(View.OnClickListener listener){
		mPositiveBtnTextView.setOnClickListener(listener);
	}


}
