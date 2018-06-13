package com.changan.changanproject.activity;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Toast;

import com.changan.changanproject.R;
import com.changan.changanproject.constant.Constant;
import com.changan.changanproject.fragment.cardfragment.CardFragmentPagerAdapter;
import com.changan.changanproject.fragment.cardfragment.ShadowTransformer;
import com.changan.changanproject.view.MyToolbar;


public class DIDTypeActivity extends BaseActivity {

    private ViewPager mViewPager;
    private CardFragmentPagerAdapter mFragmentCardAdapter;
    private ShadowTransformer mFragmentCardShadowTransformer;
    private MyToolbar tb;

    @Override
    protected View getContentView() {
        return inflateView(R.layout.activity_didtype);
    }

    @Override
    protected void setContentViewAfter(View contentView) {
        initView();
    }

    public void initView(){
        mViewPager = (ViewPager)findViewById(R.id.viewPager);
        tb = findView(R.id.toolbar);
        tb.setTitle("数据选择 ");
        tb.setNavigationIcon(R.mipmap.backarrow);
        tb.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Constant.figureDatas.size()!=10){
                    Toast.makeText(DIDTypeActivity.this,"必须添加10个DID数据，当前添加"+Constant.figureDatas.size()+"个，请检查后进行添加或者取消",Toast.LENGTH_SHORT).show();
                    return;
                }
                finish();
            }
        });
        tb.setTitleTextColor(Color.parseColor("#FFFFFF"));
        mFragmentCardAdapter = new CardFragmentPagerAdapter(getSupportFragmentManager(), dpToPixels(2, this));
        mFragmentCardShadowTransformer = new ShadowTransformer(mViewPager, mFragmentCardAdapter);
        mFragmentCardShadowTransformer.enableScaling(true);
        mViewPager.setAdapter(mFragmentCardAdapter);
        mViewPager.setPageTransformer(false, mFragmentCardShadowTransformer);
        mViewPager.setOffscreenPageLimit(3);//viewpager每次切换的时候， 会重新创建当前界面及左右界面三个界面， 每次切换都要重新oncreate,设置3表示三个界面之间来回切换都不会重新加载
    }


    public static float dpToPixels(int dp, Context context) {
        //根据手机屏幕参数，后面的density就是屏幕的密度，类似分辨率
        return dp * (context.getResources().getDisplayMetrics().density);
    }

    @Override
    public void onBackPressed() {
        if(Constant.figureDatas.size()!=10){
            Toast.makeText(DIDTypeActivity.this,"必须添加10个DID数据，当前添加"+Constant.figureDatas.size()+"个，请检查后进行添加或者取消",Toast.LENGTH_SHORT).show();
            return;
        }
        finish();
    }
}
