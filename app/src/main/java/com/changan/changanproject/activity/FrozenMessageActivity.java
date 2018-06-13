package com.changan.changanproject.activity;

import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.changan.changanproject.R;
import com.changan.changanproject.adapter.FrozenMessageAdapter;
import com.changan.changanproject.fragment.ErrorFragment;
import com.changan.changanproject.view.MyToolbar;

public class FrozenMessageActivity extends BaseActivity {

    TextView dtcView;
    ListView fmListView;
    MyToolbar tb;


    @Override
    protected View getContentView() {
        return inflateView(R.layout.activity_frozenmessage);
    }

    @Override
    protected void setContentViewAfter(View contentView) {
       initView();
        showDtcDesc();

    }

    public void initView(){
        dtcView = findView(R.id.dtcView);
        fmListView = findView(R.id.fmListView);
        tb = findView(R.id.toolbar);
        tb.setTitle("冻结帧");
        tb.setNavigationIcon(R.mipmap.backarrow);
        tb.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        tb.setTitleTextColor(Color.parseColor("#FFFFFF"));
    }

    public void showDtcDesc(){
        Intent intent = getIntent();
        int position = intent.getIntExtra("position",-1);
        FrozenMessageAdapter fmAdapter = new FrozenMessageAdapter(FrozenMessageActivity.this,ErrorFragment.dtcMessages.get(position).getFrozenMessages());
        dtcView.setText("故障码： "+ErrorFragment.dtcMessages.get(position).getDtc());
        fmListView.setAdapter(fmAdapter);
    }


}
