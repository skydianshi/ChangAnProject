package com.changan.changanproject.activity;

import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

import com.changan.changanproject.R;
import com.changan.changanproject.fragment.ErrorFragment;
import com.changan.changanproject.view.MyToolbar;

public class DtcDescActivity extends BaseActivity {
    private TextView dtcNumber_tv;
    private TextView dtcMeaning_tv;
    private TextView faultType_tv;
    private TextView faultAttribute_tv;
    private TextView faultExplain_tv;
    private TextView faultCauses_tv;
    private TextView correctiveAction_tv;
    private MyToolbar tb;

    @Override
    protected View getContentView() {
        return inflateView(R.layout.activity_dtc_desc);
    }

    @Override
    protected void setContentViewAfter(View contentView) {
        initView();

        Intent intent = getIntent();
        int position = intent.getIntExtra("position",-1);
        dtcNumber_tv.setText("故障码："+ErrorFragment.dtcMessages.get(position).getDtc());
        dtcMeaning_tv.setText("故障含义："+ErrorFragment.dtcMessages.get(position).getDtcMeaning());
        faultType_tv.setText(""+ErrorFragment.dtcMessages.get(position).getFaultType());
        faultAttribute_tv.setText(""+ErrorFragment.dtcMessages.get(position).getFaultsAttribute());
        faultExplain_tv.setText(""+ErrorFragment.dtcMessages.get(position).getFaultExplain());
        faultCauses_tv.setText(""+ErrorFragment.dtcMessages.get(position).getPossibleCauses());
        correctiveAction_tv.setText(""+ErrorFragment.dtcMessages.get(position).getCorrectiveAction());
    }

    public void initView(){
        dtcNumber_tv = findView(R.id.dtcView);
        dtcMeaning_tv = findView(R.id.dtcMean);
        faultType_tv = findView(R.id.faultsType);
        faultAttribute_tv = findView(R.id.faultsAttribute);
        faultExplain_tv = findView(R.id.faultExplain);
        faultCauses_tv = findView(R.id.faultCause);
        correctiveAction_tv = findView(R.id.correctiveAction);
        tb = findView(R.id.toolbar);
        tb.setTitle("故障描述");
        tb.setNavigationIcon(R.mipmap.backarrow);
        tb.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        tb.setTitleTextColor(Color.parseColor("#FFFFFF"));
    }

}
