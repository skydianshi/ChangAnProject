package com.changan.changanproject.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.changan.changanproject.R;


public class IpItemEditText extends EditText {

	public enum IpPart {
		PART1, PART2, PART3, PART4
	};

	private final int DEF_MAX = 255;
	private final int DEF_MIN = 1;
	private int m_min = DEF_MIN;
	private int m_max = DEF_MAX;
	private final int MAX_LEN = 3;
	private OnItemFullListener mOnItemFullListener;
	private IpPart mIpPart = IpPart.PART1;
	private boolean mKeyDown = false;

	public IpItemEditText(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		init();
		setCustomAttr(context, attrs);
	}

	public IpItemEditText(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		init();
		setCustomAttr(context, attrs);
	}

	public IpItemEditText(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		init();
	}

	private void setCustomAttr(Context context, AttributeSet attrs) {
		TypedArray ta = context
				.obtainStyledAttributes(attrs, R.styleable.range);
		int nCount = ta.getIndexCount();
		for (int i = 0; i < nCount; i++) {
			int index = ta.getIndex(i);
			switch (index) {
			case R.styleable.range_max:
				m_max = ta.getInt(index, DEF_MAX);
				break;
			case R.styleable.range_min:
				m_min = ta.getInt(index, DEF_MIN);
				break;
			case R.styleable.range_cur:
				setText("" + ta.getInt(index, DEF_MIN));
				break;
			}
		}
		ta.recycle();
	}

	private void init() {
		addTextChangedListener(new IpItemTextWatcher(this));
	}

	public void setPartFlag(IpPart ipPart) {
		mIpPart = ipPart;
	}

	public void setOnItemFullListener(OnItemFullListener onItemFullListener) {
		mOnItemFullListener = onItemFullListener;
	}

	private class IpItemTextWatcher implements TextWatcher {

		private EditText mEditText;
		private String mStrBefore = "";

		IpItemTextWatcher(EditText editText) {
			mEditText = editText;
		}

		@Override
		public void afterTextChanged(Editable arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void beforeTextChanged(CharSequence cs, int arg1, int arg2,
									  int arg3) {
			// TODO Auto-generated method stub
			mStrBefore = cs.toString();
		}

		@Override
		public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
			// TODO Auto-generated method stub
			String str = cs.toString();
			if (str.equals("")) {
				return;
			}
			int nVal = Integer.parseInt(str);
			if (nVal > m_max || nVal < m_min) {
				Toast toast = Toast.makeText(getContext(), "Range[" + m_min
						+ ", " + m_max + "]", Toast.LENGTH_SHORT);
				toast.setGravity(Gravity.CENTER, 0, 0);
				toast.show();
				mEditText.setText(mStrBefore);
				mEditText.setSelection(mEditText.getText().toString().length());
				return;
			}
			if (str.length() >= MAX_LEN && mOnItemFullListener != null) {
				mOnItemFullListener.OnItemFull(mEditText, mIpPart);
			}
		}
	}

	public interface OnItemFullListener {
		public void OnItemFull(View view, IpPart nIpPart);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		mKeyDown = true;
		return super.onKeyDown(keyCode, event);
	}
}