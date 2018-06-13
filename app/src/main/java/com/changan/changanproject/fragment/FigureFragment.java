package com.changan.changanproject.fragment;


import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.changan.changanproject.R;
import com.changan.changanproject.activity.MainActivity;
import com.changan.changanproject.canmessage.FigureData;
import com.changan.changanproject.canmessage.figuremessage.BCUBattIFigureData;
import com.changan.changanproject.canmessage.figuremessage.BCUBattUFigureData;
import com.changan.changanproject.canmessage.figuremessage.BCUFltRnkFigureData;
import com.changan.changanproject.canmessage.figuremessage.BCUMaxBattTFigureData;
import com.changan.changanproject.canmessage.figuremessage.BCUMaxChrgPwrShoTFigureData;
import com.changan.changanproject.canmessage.figuremessage.BCUMinBattTFigureData;
import com.changan.changanproject.canmessage.figuremessage.BCUOperModFigureData;
import com.changan.changanproject.canmessage.figuremessage.BCUSOCFigureData;
import com.changan.changanproject.constant.Constant;
import com.changan.changanproject.fragment.cardfragment.CardFragmentPagerAdapter;
import com.changan.changanproject.fragment.cardfragment.ShadowTransformer;
import com.changan.changanproject.listener.FigureServiceListener;


public class FigureFragment extends Fragment {
    private static String TAG = "FigureFragment";
    private FigureServiceListener figureServiceListener;
    private View view;

    private TextView tv_didTitle1;
    private TextView tv_didTitle2;
    private TextView tv_didTitle3;
    private TextView tv_didTitle4;
    private TextView tv_didTitle5;
    private TextView tv_didTitle6;
    private TextView tv_didTitle7;
    private TextView tv_didTitle8;
    private TextView tv_didTitle9;
    private TextView tv_didTitle10;

    private TextView tv_didFigure1;
    private TextView tv_didFigure2;
    private TextView tv_didFigure3;
    private TextView tv_didFigure4;
    private TextView tv_didFigure5;
    private TextView tv_didFigure6;
    private TextView tv_didFigure7;
    private TextView tv_didFigure8;
    private TextView tv_didFigure9;
    private TextView tv_didFigure10;


    int index = 0;


    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            String reslut = msg.obj.toString();
            switch (msg.what){
                case -1:
                    break;
                case 0:
                    tv_didFigure1.setText(reslut+index++);
                    break;
                case 1:
                    tv_didFigure2.setText(reslut+index++);
                    break;
                case 2:
                    tv_didFigure3.setText(reslut+index++);
                    break;
                case 3:
                    tv_didFigure4.setText(reslut+index++);
                    break;
                case 4:
                    tv_didFigure5.setText(reslut+index++);
                    break;
                case 5:
                    tv_didFigure6.setText(reslut+index++);
                    break;
                case 6:
                    tv_didFigure7.setText(reslut+index++);
                    break;
                case 7:
                    tv_didFigure8.setText((reslut+index++));
                    break;
                case 8:
                    tv_didFigure9.setText((reslut+index++));
                    break;
                case 9:
                    tv_didFigure10.setText((reslut+index++));
                    break;
            }
        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_figure, null);
        initView();
        initConnection();
        return view;
    }

    public void initView(){
        MainActivity.tb.getMenu().findItem(R.id.changeChat).setVisible(false);
        MainActivity.tb.setTitle("车辆数据");
        tv_didTitle1 = (TextView)view.findViewById(R.id.tv_didTitle1);
        tv_didTitle2 = (TextView)view.findViewById(R.id.tv_didTitle2);
        tv_didTitle3 = (TextView)view.findViewById(R.id.tv_didTitle3);
        tv_didTitle4 = (TextView)view.findViewById(R.id.tv_didTitle4);
        tv_didTitle5 = (TextView)view.findViewById(R.id.tv_didTitle5);
        tv_didTitle6 = (TextView)view.findViewById(R.id.tv_didTitle6);
        tv_didTitle7 = (TextView)view.findViewById(R.id.tv_didTitle7);
        tv_didTitle8 = (TextView)view.findViewById(R.id.tv_didTitle8);
        tv_didTitle9 = (TextView)view.findViewById(R.id.tv_didTitle9);
        tv_didTitle10 = (TextView)view.findViewById(R.id.tv_didTitle10);

        tv_didFigure1 = (TextView)view.findViewById(R.id.tv_didFigure1);
        tv_didFigure2 = (TextView)view.findViewById(R.id.tv_didFigure2);
        tv_didFigure3 = (TextView)view.findViewById(R.id.tv_didFigure3);
        tv_didFigure4 = (TextView)view.findViewById(R.id.tv_didFigure4);
        tv_didFigure5 = (TextView)view.findViewById(R.id.tv_didFigure5);
        tv_didFigure6 = (TextView)view.findViewById(R.id.tv_didFigure6);
        tv_didFigure7 = (TextView)view.findViewById(R.id.tv_didFigure7);
        tv_didFigure8 = (TextView)view.findViewById(R.id.tv_didFigure8);
        tv_didFigure9 = (TextView)view.findViewById(R.id.tv_didFigure9);
        tv_didFigure10 = (TextView)view.findViewById(R.id.tv_didFigure10);

    }

    //建立与Activity的连接
    public void initConnection(){
        figureServiceListener = new FigureServiceListener() {
            @Override
            public void update(String s) {
                updateUI(s);
            }
        };
        MainActivity.setFigureListener(figureServiceListener);
    }


    public void updateUI(String message){
        String id = message.substring(8,12);
        int figureIndex = findIndexById(id);
        Message commandMessage = new Message();
        if(figureIndex!=-1){
            commandMessage.obj = Constant.figureDatas.get(figureIndex).getResult();
        }else{
            commandMessage.obj = "NO DATA";
        }
        commandMessage.what = figureIndex;
        handler.sendMessage(commandMessage);
    }

    @Override
    public void onResume() {
        super.onResume();
        initTitleView();
    }

    public void initTitleView(){
        tv_didTitle1.setText(Constant.figureDatas.get(0).getTitle());
        tv_didTitle2.setText(Constant.figureDatas.get(1).getTitle());
        tv_didTitle3.setText(Constant.figureDatas.get(2).getTitle());
        tv_didTitle4.setText(Constant.figureDatas.get(3).getTitle());
        tv_didTitle5.setText(Constant.figureDatas.get(4).getTitle());
        tv_didTitle6.setText(Constant.figureDatas.get(5).getTitle());
        tv_didTitle7.setText(Constant.figureDatas.get(6).getTitle());
        tv_didTitle8.setText(Constant.figureDatas.get(7).getTitle());
        tv_didTitle9.setText(Constant.figureDatas.get(8).getTitle());
        tv_didTitle10.setText(Constant.figureDatas.get(9).getTitle());
    }

    //根据传过来的message的id来判断对应于列表message中的哪一个
    public int findIndexById(String id){
        for(int i = 0; i<Constant.figureDatas.size();i++){
            if(id.equals(Constant.figureDatas.get(i).getDid())){
                return i;
            }
        }
        return -1;
    }
}