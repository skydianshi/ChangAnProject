package com.changan.changanproject.fragment.cardfragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v7.widget.CardView;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class CardFragmentPagerAdapter extends FragmentStatePagerAdapter implements CardAdapter {

    private List<Fragment> mFragments;
    private float mBaseElevation;
    private String[] titles = new String[35];


    String desc = "发动机转速的高低，关系到单位时间内作功次数的多少或发动机有效功率的大小，即发动机的有效功率随转速的不同而改变。";


    public CardFragmentPagerAdapter(FragmentManager fm, float baseElevation) {
        super(fm);

        titles[0] = "电池总电压";
        titles[1] = "电池总电流";
        titles[2] = "电池荷电量";
        titles[3] = "电池故障等级";
        titles[4] = "BCU工作模式";
        titles[5] = "OBC工作模式请求";
        titles[6] = "允许重新上高压电";
        titles[7] = "高压互锁状态";
        titles[8] = "碰撞状态";
        titles[9] = "主继电器常闭故障";
        titles[10] = "绝缘状态";
        titles[11] = "电池内部绝缘故障";
        titles[12] = "高压负载绝缘故障";
        titles[13] = "电池最大放电功率";
        titles[14] = "电池最大充电功率";
        titles[15] = "电池最小放电电压";
        titles[16] = "最大持续放电电流";
        titles[17] = "最大持续充电电流";
        titles[18] = "电池最大充电电压";
        titles[19] = "外插充电需求电压";
        titles[20] = "外插充电需求电流";
        titles[21] = "电池充电模式";
        titles[22] = "BCU充电过程故障";
        titles[23] = "电池充电状态";
        titles[24] = "电池最高温度";
        titles[25] = "电池最低温度";
        titles[26] = "剩余充电时间";
        titles[27] = "绝缘电阻";
        titles[28] = "标定数据版本";
        titles[29] = "CP占空比";
        titles[30] = "CP状态";
        titles[31] = "充电机工作状态";
        titles[31] = "充电机输入电压";
        titles[32] = "DCDC低压电压";
        titles[33] = "DCDC低压电流";
        titles[34] = "DCDC工作模式";

        mFragments = new ArrayList<>();
        mBaseElevation = baseElevation;
        for(int i = 0;i<titles.length;i++){
            addCardFragment(CardFragment.newInstance(titles[i],desc));
        }

    }

    @Override
    public float getBaseElevation() {
        return mBaseElevation;
    }

    @Override
    public CardView getCardViewAt(int position) {
        return (CardView)mFragments.get(position).getView();
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Object fragment = super.instantiateItem(container, position);
        mFragments.set(position, (Fragment) fragment);
        return fragment;
    }

    public void addCardFragment(Fragment fragment) {
        mFragments.add(fragment);
    }

}
