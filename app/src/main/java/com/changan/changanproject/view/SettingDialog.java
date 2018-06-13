package com.changan.changanproject.view;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.changan.changanproject.R;
import com.changan.changanproject.constant.Constant;


public class SettingDialog extends Dialog{
	private EditText et_workport;
	private EditText et_errorport;
	private TextView tv_confirm;
	private EditText et_ipAdd;
	private Context mContext;


	public SettingDialog(Context context, int theme) {
		super(context, theme);
		// TODO Auto-generated constructor stub
		mContext = context;
		init();
	}




	private void init() {
		LayoutInflater layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = layoutInflater.inflate(R.layout.user_setting, null);

		et_workport = (EditText)view.findViewById(R.id.EditTextTargetPort);
		et_errorport = (EditText)view.findViewById(R.id.EditTextErrNoticePort);
		tv_confirm = (TextView)view.findViewById(R.id.tv_confirm);
		et_ipAdd = (EditText)view.findViewById(R.id.textViewIpAddr);
		tv_confirm = (TextView)view.findViewById(R.id.tv_confirm);
		et_workport.setText(String.valueOf(Constant.TARGET_PORT));
		et_errorport.setText(String.valueOf(Constant.ERR_PORT));
		et_ipAdd.setText(Constant.IP_ADDRESS);
		tv_confirm.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Constant.TARGET_PORT = Integer.parseInt(et_workport.getText().toString());
				Constant.ERR_PORT = Integer.parseInt(et_errorport.getText().toString());
				Constant.IP_ADDRESS = et_ipAdd.getText().toString();
				dismiss();
			}
		});

		setContentView(view);
	}


}
