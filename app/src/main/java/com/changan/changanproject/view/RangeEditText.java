package com.changan.changanproject.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.EditText;
import android.widget.Toast;

import com.changan.changanproject.R;


public class RangeEditText extends EditText {

	private final int DEF_MAX = 10;
	private final int DEF_MIN = 0;
	private int m_min = DEF_MIN;
	private int m_max = DEF_MAX;

	public RangeEditText(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		init();
		setCustomAttr(context, attrs);
	}

	public RangeEditText(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		init();
		setCustomAttr(context, attrs);
	}

	public RangeEditText(Context context) {
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
		// TODO Auto-generated method stub
		this.addTextChangedListener(new TextWatcherEx());
	}

	public int getMin() {
		return m_min;
	}

	public void setMin(int min) {
		this.m_min = min;
	}

	public int getMax() {
		return m_max;
	}

	public void setMax(int max) {
		this.m_max = max;
	}

	private class TextWatcherEx implements TextWatcher {

		@Override
		public void afterTextChanged(Editable s) {
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
									  int after) {
		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
								  int count) {
			if (0 == s.length()) {
				return;
			}
			int n = m_min;
			try {
				n = Integer.parseInt(s.toString());
			} catch (NumberFormatException e) {
				// TODO: handle exception
			}
			if (n < m_min || n > m_max) {
				Toast toast = Toast.makeText(getContext(),
						"Range[" + m_min + ", " + m_max + "]",
						Toast.LENGTH_SHORT);
				toast.setGravity(Gravity.CENTER, 0, 0);
				toast.show();
				String string = "" + (n < m_min ? m_min : m_max);
				setText(string);
				setSelection(string.length());
			}
		}
	}
}
