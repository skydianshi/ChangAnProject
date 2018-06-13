package com.changan.changanproject.fragment.cardfragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.changan.changanproject.R;
import com.changan.changanproject.canmessage.FigureData;
import com.changan.changanproject.canmessage.figuremessage.BCUBattIFigureData;
import com.changan.changanproject.canmessage.figuremessage.BCUBattUFigureData;
import com.changan.changanproject.canmessage.figuremessage.BCUCalDataVersFigureData;
import com.changan.changanproject.canmessage.figuremessage.BCUChrgErrInfoFigureData;
import com.changan.changanproject.canmessage.figuremessage.BCUChrgIReqFigureData;
import com.changan.changanproject.canmessage.figuremessage.BCUChrgModFigureData;
import com.changan.changanproject.canmessage.figuremessage.BCUChrgStsFigureData;
import com.changan.changanproject.canmessage.figuremessage.BCUChrgUReqFigureData;
import com.changan.changanproject.canmessage.figuremessage.BCUCrashStsFigureData;
import com.changan.changanproject.canmessage.figuremessage.BCUFltRnkFigureData;
import com.changan.changanproject.canmessage.figuremessage.BCUHVILStsFigureData;
import com.changan.changanproject.canmessage.figuremessage.BCUHwVersFigureData;
import com.changan.changanproject.canmessage.figuremessage.BCUInsulationHVLoadErrFigureData;
import com.changan.changanproject.canmessage.figuremessage.BCUInsulationIntrErrFigureData;
import com.changan.changanproject.canmessage.figuremessage.BCUInsulationStsFigureData;
import com.changan.changanproject.canmessage.figuremessage.BCUIsolationRFigureData;
import com.changan.changanproject.canmessage.figuremessage.BCUMaxBattTFigureData;
import com.changan.changanproject.canmessage.figuremessage.BCUMaxChrgIContnsFigureData;
import com.changan.changanproject.canmessage.figuremessage.BCUMaxChrgPwrShoTFigureData;
import com.changan.changanproject.canmessage.figuremessage.BCUMaxChrgUFigureData;
import com.changan.changanproject.canmessage.figuremessage.BCUMaxDchaIContnsFigureData;
import com.changan.changanproject.canmessage.figuremessage.BCUMaxDchaPwrShoTFigureData;
import com.changan.changanproject.canmessage.figuremessage.BCUMinBattTFigureData;
import com.changan.changanproject.canmessage.figuremessage.BCUMinDchaIUFigureData;
import com.changan.changanproject.canmessage.figuremessage.BCUOBCOperModReqFigureData;
import com.changan.changanproject.canmessage.figuremessage.BCUOperModFigureData;
import com.changan.changanproject.canmessage.figuremessage.BCUPredChrgTiFigureData;
import com.changan.changanproject.canmessage.figuremessage.BCUPwrUpAllwFigureData;
import com.changan.changanproject.canmessage.figuremessage.BCURlyWlddErrFigureData;
import com.changan.changanproject.canmessage.figuremessage.BCUSOCFigureData;
import com.changan.changanproject.canmessage.figuremessage.BCUSwVersFigureData;
import com.changan.changanproject.canmessage.figuremessage.CPStsFigureData;
import com.changan.changanproject.canmessage.figuremessage.DCDCOperModeFigureData;
import com.changan.changanproject.canmessage.figuremessage.DCDCVoltageIFigureData;
import com.changan.changanproject.canmessage.figuremessage.DCDCVoltageUFigureData;
import com.changan.changanproject.canmessage.figuremessage.OBCCPValueFigureData;
import com.changan.changanproject.canmessage.figuremessage.OBCChrgInpAcUFigureData;
import com.changan.changanproject.canmessage.figuremessage.OBCChrgStsFigureData;
import com.changan.changanproject.constant.Constant;


public class CardFragment extends Fragment {

    private CardView mCardView;
    Button addButton;
    TextView tv_title;
    TextView tv_desc;
    View view;

    private String title;
    private String desc;

    public static CardFragment newInstance(String title,String desc){
        CardFragment fragment = new CardFragment();
        Bundle bundle = new Bundle();
        bundle.putString( "title", title);
        bundle.putString( "desc", desc);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_card, container, false);
        title = getArguments().getString("title");
        desc = getArguments().getString("desc");
        initView();
        return view;
    }

    public void initView(){
        mCardView = (CardView) view.findViewById(R.id.cardView);
        mCardView.setMaxCardElevation(mCardView.getCardElevation() * CardAdapter.MAX_ELEVATION_FACTOR);
        tv_title = (TextView)view.findViewById(R.id.title);
        tv_title.setText(title);
        tv_desc = (TextView)view.findViewById(R.id.desc);
        tv_desc.setText(desc);
        //根据是否已经添加初始化button
        addButton = (Button)view.findViewById(R.id.addButton);
        addButton.setText("添加");
        for(int i = 0; i < Constant.figureDatas.size(); i++){
            if(Constant.figureDatas.get(i).getTitle().equals(title)){
                addButton.setText("取消");
            }
        }
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(addButton.getText().toString().equals("添加")){
                    FigureData figureData = findItemByTitle(title);
                    Constant.figureDatas.add(figureData);
                    addButton.setText("取消");
                }else{
                    int deleteIndex = 0;
                    for(int i = 0; i < Constant.figureDatas.size(); i++){
                        if(Constant.figureDatas.get(i).getTitle().equals(title)){
                            deleteIndex = i;
                        }
                    }
                    Constant.figureDatas.remove(deleteIndex);
                    addButton.setText("添加");
                }

            }
        });
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public FigureData findItemByTitle(String title){
        if(title.equals("电池总电流")){
            return new BCUBattIFigureData();
        }else if(title.equals("电池总电压")){
            return new BCUBattUFigureData();
        } else if(title.equals("标定数据版本")){
            return new BCUCalDataVersFigureData();
        } else if(title.equals("BCU充电过程故障")){
            return new BCUChrgErrInfoFigureData();
        } else if(title.equals("外插充电需求电流")){
            return new BCUChrgIReqFigureData();
        } else if(title.equals("电池充电模式")){
            return new BCUChrgModFigureData();
        }else if(title.equals("电池充电状态")){
            return new BCUChrgStsFigureData();
        }else if(title.equals("外插充电需求电压")){
            return new BCUChrgUReqFigureData();
        }else if(title.equals("碰撞状态")){
            return new BCUCrashStsFigureData();
        }else if(title.equals("电池故障等级")){
            return new BCUFltRnkFigureData();
        }else if(title.equals("高压互锁状态")){
            return new BCUHVILStsFigureData();
        }else if(title.equals("硬件版本")){
            return new BCUHwVersFigureData();
        }else if(title.equals("高压负载绝缘故障")){
            return new BCUInsulationHVLoadErrFigureData();
        }else if(title.equals("电池内部绝缘故障")){
            return new BCUInsulationIntrErrFigureData();
        }else if(title.equals("绝缘状态")){
            return new BCUInsulationStsFigureData();
        }else if(title.equals("绝缘电阻")){
            return new BCUIsolationRFigureData();
        }else if(title.equals("电池最高温度")){
            return new BCUMaxBattTFigureData();
        }else if(title.equals("最大持续充电电流")){
            return new BCUMaxChrgIContnsFigureData();
        }else if(title.equals("电池最大充电功率")){
            return new BCUMaxChrgPwrShoTFigureData();
        }else if(title.equals("电池最大充电电压")){
            return new BCUMaxChrgUFigureData();
        }else if(title.equals("最大持续放电电流")){
            return new BCUMaxDchaIContnsFigureData();
        }else if(title.equals("电池最大放电功率")){
            return new BCUMaxDchaPwrShoTFigureData();
        }else if(title.equals("电池最低温度")){
            return new BCUMinBattTFigureData();
        }else if(title.equals("电池最小放电电压")){
            return new BCUMinDchaIUFigureData();
        }else if(title.equals("OBC工作模式请求")){
            return new BCUOBCOperModReqFigureData();
        }else if(title.equals("BCU工作模式")){
            return new BCUOperModFigureData();
        }else if(title.equals("剩余充电时间")){
            return new BCUPredChrgTiFigureData();
        }else if(title.equals("允许重新上高压电")){
            return new BCUPwrUpAllwFigureData();
        }else if(title.equals("主继电器常闭故障")){
            return new BCURlyWlddErrFigureData();
        }else if(title.equals("电池荷电量")){
            return new BCUSOCFigureData();
        }else if(title.equals("软件版本")){
            return new BCUSwVersFigureData();
        }else if(title.equals("CP状态")){
            return new CPStsFigureData();
        }else if(title.equals("DCDC工作模式")){
            return new DCDCOperModeFigureData();
        }else if(title.equals("DCDC低压电流")){
            return new DCDCVoltageIFigureData();
        }else if(title.equals("DCDC低压电压")){
            return new DCDCVoltageUFigureData();
        }else if(title.equals("充电机输入电压")){
            return new OBCChrgInpAcUFigureData();
        }else if(title.equals("充电机工作状态")){
            return new OBCChrgStsFigureData();
        }else if(title.equals("CP占空比")){
            return new OBCCPValueFigureData();
        }
        return null;
    }
}
