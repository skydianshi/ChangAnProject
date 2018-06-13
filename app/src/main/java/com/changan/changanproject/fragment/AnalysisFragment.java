package com.changan.changanproject.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.changan.changanproject.R;
import com.changan.changanproject.activity.MainActivity;
import com.changan.changanproject.listener.AnalysisServiceListener;
import com.changan.changanproject.view.ChartSetUtil;
import com.changan.changanproject.view.picker.CustomHeaderAndFooterPicker;
import com.changan.changanproject.view.picker.OptionPicker;

import lecho.lib.hellocharts.view.LineChartView;

public class AnalysisFragment extends Fragment {
    private LineChartView lineChart;
    private TextView titleView;
    ChartSetUtil chartSet = new ChartSetUtil();
    private String[] xData = new String[100000];//X轴的标注
    private float[] yData= new float[100000];//图表的数据

    private String[] xShowData = new String[10000];//X轴的标注
    float[] yShowData= new float[10000];//图表的数据
    private float maxFigure = 0;
    private float minFigure = 0;
    private TextView maxValueTextView;
    private TextView minValueTextView;

    private AnalysisServiceListener analysisServiceListener;
    private View view;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        initData();
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_analysis, null);
        initConnection();
        initView();
        initListener();
        return view;
    }

    //初始化数据
    public void initData(){
        for(int i = 0;i<100000;i++){
            xData[i] = i+"";
            yData[i] = (float)(-2000+Math.random()*(4000));//-100到100随机数
        }
    }

    public void initShowData(int index){
        for(int i = 0;i<10000;i++){
            xShowData[i] = xData[10000*index+i];
            yShowData[i] = yData[10000*index+i];
        }
        maxFigure = findMax(yShowData);
        minFigure = findMin(yShowData);
        maxValueTextView.setText(String.valueOf("最大值："+ maxFigure));
        minValueTextView.setText(String.valueOf("最小值： "+ minFigure));
    }

    public void initView(){
        MainActivity.tb.getMenu().findItem(R.id.changeChat).setVisible(true);
        MainActivity.tb.setTitle("数据分析");
        titleView = (TextView)view.findViewById(R.id.title);
        lineChart = (LineChartView)view.findViewById(R.id.line_chart);
        maxValueTextView = (TextView)view.findViewById(R.id.maxValue);
        minValueTextView = (TextView)view.findViewById(R.id.minValue);

        initShowData(0);
        chartSet.initLineChart(lineChart,xShowData,yShowData,"时间(s)","发动机转速");
        titleView.setText("电池总电压（V）");
    }

    public void initListener(){
        //给overflowMenu设置监听
        MainActivity.tb.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if(("1").equals(item.getTitle())){
                    CustomHeaderAndFooterPicker picker = new CustomHeaderAndFooterPicker(getActivity());
                    picker.setOffset(3);//显示的条目的偏移量，条数为（offset*2+1）
                    picker.setGravity(Gravity.CENTER);//居中
                    picker.setOnOptionPickListener(new OptionPicker.OnOptionPickListener() {
                        @Override
                        public void onOptionPicked(int position, String option) {
                            //重设曲线数据
                            initShowData(position);
                            chartSet.initLineChart(lineChart,xShowData,yShowData,"时间(s)",option);
                            titleView.setText(option);
                        }
                    });
                    picker.show();
                }
                return false;
            }
        });
    }

    //建立与Activity的连接
    public void initConnection(){
        analysisServiceListener = new AnalysisServiceListener() {
            @Override
            public void print(String s) {
                System.out.println("AnalysisFragment");
            }
        };
        MainActivity.setAnalysisListener(analysisServiceListener);
    }

    public float findMax(float[] figures){
        float max = 0;
        for(int i = 0;i<figures.length;i++){
            if(max<figures[i]){
                max = figures[i];
            }
        }
        return  max;
    }

    public float findMin(float [] figures){
        float min = 0;
        for(int i = 0; i<figures.length;i++){
            if(min>figures[i]){
                min = figures[i];
            }
        }
        return min;
    }

    @Override
    public void onPause() {
        super.onPause();
    }
}