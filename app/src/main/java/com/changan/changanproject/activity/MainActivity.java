package com.changan.changanproject.activity;

import android.app.Service;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.app.FragmentTabHost;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.changan.changanproject.R;
import com.changan.changanproject.constant.Constant;
import com.changan.changanproject.fragment.DataFragment;
import com.changan.changanproject.fragment.ErrorFragment;
import com.changan.changanproject.fragment.FigureFragment;
import com.changan.changanproject.listener.AnalysisServiceListener;
import com.changan.changanproject.listener.DataServiceListener;
import com.changan.changanproject.listener.ErrorServiceListener;
import com.changan.changanproject.listener.FigureServiceListener;
import com.changan.changanproject.listener.IPostListener;
import com.changan.changanproject.service.MyService;
import com.changan.changanproject.view.ExportAlertDialog;
import com.changan.changanproject.view.SettingDialog;
import com.changan.changanproject.view.WaitDialog;
import com.changan.changanproject.util.AppManager;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;

import cn.zlg.www.ErrData;
import cn.zlg.www.ItemData;

/**
 * @author yangyu
 *  功能描述：自定义TabHost
 */
public class MainActivity extends BaseActivity {
    private static String TAG = "MainActivity";
    Constant constant;
    //定义FragmentTabHost对象
    private FragmentTabHost mTabHost;
    //定义一个布局
    private LayoutInflater layoutInflater;
    //定义数组来存放Fragment界面
    private Class fragmentArray[] = {FigureFragment.class,ErrorFragment.class,DataFragment.class};
    //定义数组来存放按钮图片
    private int mImageViewArray[] = {R.drawable.tab_figure_btn,R.drawable.tab_error_btn, R.drawable.tab_analysis_btn};
    //Tab选项卡的文字
    private String mTextviewArray[] = {"车辆数据", "汽车诊断", "数据分析"};

    private static FigureServiceListener fsListener;
    private static ErrorServiceListener esListener;
    private static AnalysisServiceListener asListener;
    private static DataServiceListener dsListener;
    ExportAlertDialog mExportAlertDialog;//写入数据dialog
    WaitDialog waitDialog;

    IPostListener callback;
    Intent startServiceIntent;//启动服务Intent
    private MyService.MyBinder binder;
    public static Toolbar tb;
    Drawer drawer;//抽屉menu


    /*
    * 加载JNI函数库
    * */
    static {
        System.loadLibrary("CANWifi");
    }

    @Override
    protected View getContentView() {
        return inflateView(R.layout.activity_main);
    }

    @Override
    protected void setContentViewAfter(View contentView) {
        //保持屏幕唤醒
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        constant = new Constant(this);//初始化常量
        initView();
        initSerViceToActivity();
    }

    //UI更新操作
    Handler UiHandler = new Handler(){

        @Override
        public void handleMessage(Message msg) {
            String message = msg.obj.toString();
            switch (msg.what){
                case 0:
                    Toast.makeText(MainActivity.this,message,Toast.LENGTH_SHORT).show();
                    break;
                case 1:
                    if(message.equals("connect")){
                        tb.getMenu().findItem(R.id.connectWifi).setIcon(R.mipmap.status_connect);
                    }else{
                        tb.getMenu().findItem(R.id.connectWifi).setIcon(R.mipmap.status_unconnect);
                    }
                    break;
            }
        }
    };


    /**
     * 初始化组件
     */
    private void initView(){
        //实例化布局对象
        layoutInflater = LayoutInflater.from(this);

        //实例化TabHost对象，得到TabHost
        mTabHost = (FragmentTabHost)findViewById(android.R.id.tabhost);
        mTabHost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);
        //得到fragment的个数
        int count = fragmentArray.length;
        for(int i = 0; i < count; i++){
            //为每一个Tab按钮设置图标、文字和内容
            TabHost.TabSpec tabSpec = mTabHost.newTabSpec(mTextviewArray[i]).setIndicator(getTabItemView(i));
            //将Tab按钮添加进Tab选项卡中
            mTabHost.addTab(tabSpec, fragmentArray[i], null);
            //设置Tab按钮的背景
            mTabHost.getTabWidget().getChildAt(i).setBackgroundResource(R.drawable.selector_tab_background);
        }
        tb = findView(R.id.toolbar);
        tb.setTitle(getResources().getString(R.string.app_name));
        tb.setNavigationIcon(R.mipmap.icon_menu);
        tb.setTitleTextColor(Color.parseColor("#FFFFFF"));
        tb.inflateMenu(R.menu.menu_settings);
        tb.getMenu().findItem(R.id.changeChat).setVisible(false);//设置隐藏overflowMenu
        tb.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if(("2").equals(item.getTitle())){
                    if(!Constant.is_connect){
                        if(startServiceIntent==null){
                            bindService();
                        }else {
                            binder.connectCar();
                        }
                    }else{
                        binder.unConnect();
                    }
                }
                return false;
            }
        });
        tb.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawer.openDrawer();
            }
        });

        AccountHeader headerResult = new AccountHeaderBuilder()
                .withActivity(this)
                .withHeaderBackground(R.mipmap.drawer_bg)
                .addProfiles(
                        new ProfileDrawerItem().withName("长安新能源").withEmail("changan@changan.com.cn").withIcon(getResources().getDrawable(R.mipmap.changan_icon))
                )
                .withOnAccountHeaderListener(new AccountHeader.OnAccountHeaderListener() {
                    @Override
                    public boolean onProfileChanged(View view, IProfile profile, boolean currentProfile) {
                        return false;
                    }
                })
                .build();

            drawer = new DrawerBuilder()
                    .withActivity(this)
                .withAccountHeader(headerResult)
                .addDrawerItems(
                        new PrimaryDrawerItem().withName("连接汽车").withIcon(R.mipmap.draw_connect).withIdentifier(1),
                        new PrimaryDrawerItem().withName("断开连接").withIcon(R.mipmap.draw_disconnect).withIdentifier(2),
                        new DividerDrawerItem(),
                        new PrimaryDrawerItem().withName("开始更新").withIcon(R.mipmap.draw_start).withIdentifier(3),
                        new PrimaryDrawerItem().withName("停止更新").withIcon(R.mipmap.draw_stop).withIdentifier(4),
                        new PrimaryDrawerItem().withName("写入CAN报文").withIcon(R.mipmap.draw_write).withIdentifier(5),
                        new PrimaryDrawerItem().withName("停止写入").withIcon(R.mipmap.draw_stopwrite).withIdentifier(6),
                        new PrimaryDrawerItem().withName("数据选择").withIcon(R.mipmap.draw_choose).withIdentifier(7),
                        new DividerDrawerItem(),
                        new PrimaryDrawerItem().withName("设置").withIcon(R.mipmap.setting).withIdentifier(8)
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {

                        if (drawerItem != null) {
                            switch ((int) drawerItem.getIdentifier()) {
                                case 1:
                                    if(startServiceIntent==null){
                                        bindService();
                                    }else {
                                        binder.connectCar();
                                    }
                                    break;
                                case 2:
                                    if(binder!=null){
                                        binder.unConnect();
                                    }else{
                                        Toast.makeText(MainActivity.this,"汽车未连接",Toast.LENGTH_SHORT).show();
                                    }
                                    break;
                                case 3:
                                    if(binder!=null){
                                        binder.updateFigure();
                                    } else{
                                        Toast.makeText(MainActivity.this,"汽车未连接",Toast.LENGTH_SHORT).show();
                                    }
                                    break;
                                case 4:
                                    if(binder!=null){
                                        binder.stopUpdate();
                                    }else{
                                        Toast.makeText(MainActivity.this,"汽车未连接",Toast.LENGTH_SHORT).show();
                                    }
                                    break;
                                case 5:
                                    if(binder!=null){
                                        if(Constant.is_save){
                                            Toast.makeText(MainActivity.this,"正在保存中",Toast.LENGTH_SHORT).show();
                                        }else {
                                            mExportAlertDialog = new ExportAlertDialog(MainActivity.this,R.style.dialog);
                                            mExportAlertDialog.setOnPositiveLister(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {
                                                    Constant.saveFilePath = mExportAlertDialog.mFilePath.getText().toString()+".csv";
                                                    Constant.writeIndex = 0;
                                                    binder.saveFigure();
                                                    mExportAlertDialog.dismiss();
                                                }
                                            });
                                            mExportAlertDialog.show();
                                        }
                                    } else{
                                        Toast.makeText(MainActivity.this,"汽车未连接",Toast.LENGTH_SHORT).show();
                                    }
                                    break;
                                case 6:
                                    if(binder!=null){
                                        binder.stopSave();
                                    } else{
                                        Toast.makeText(MainActivity.this,"汽车未连接",Toast.LENGTH_SHORT).show();
                                    }
                                    break;
                                case 7:
                                    Intent didIntent = new Intent(MainActivity.this,DIDTypeActivity.class);
                                    startActivity(didIntent);
                                    break;
                                case 8:
                                    SettingDialog settingDialog = new SettingDialog(MainActivity.this,R.style.dialog);
                                    settingDialog.show();
                                    break;
                            }
                        }
                        return false;
                    }
                })
                .withShowDrawerOnFirstLaunch(false)
                .withFireOnInitialOnClick(false)
                .build();
    }

    /**
     * 给Tab按钮设置图标和文字
     */
    private View getTabItemView(int index){
        View view = layoutInflater.inflate(R.layout.tab_item_view, null);

        ImageView imageView = (ImageView) view.findViewById(R.id.imageview);
        imageView.setImageResource(mImageViewArray[index]);

        TextView textView = (TextView) view.findViewById(R.id.textview);
        textView.setText(mTextviewArray[index]);

        return view;
    }


    private ServiceConnection conn = new ServiceConnection() {
        //当该activity与service连接成功时调用此方法
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            Log.d(TAG,"service connected");
            //获取service中返回的Mybind对象
            binder = (MyService.MyBinder)iBinder;
        }
        //断开连接时调用此方法
        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            Log.d(TAG,"disconnected");
        }
    };
    //建立Activity与FigureFragment之间的连接
    public static void setFigureListener(FigureServiceListener listener){
        fsListener = listener;
    }
    //建立Activity与FigureFragment之间的连接
    public static void setErrorListener(ErrorServiceListener listener){
        esListener = listener;
    }
    //建立Activity与FigureFragment之间的连接
    public static void setAnalysisListener(AnalysisServiceListener listener){
        asListener = listener;
    }
    //建立Activity与DataFragment之间的连接
    public static void setDataListener(DataServiceListener listener){
        dsListener = listener;
    }

    //发送Fragment请求
    public void sendFragmentRequest(String request){
        try {
            binder.getFragmentRequest(request);
        }catch (NullPointerException e){
            e.printStackTrace();
            Toast.makeText(MainActivity.this,"请先连接汽车再进行操作",Toast.LENGTH_SHORT).show();
            return;
        }
    }
    //通知Service发送数据
    public void sendFrames(int count ,int timeInterval, int frmInteval){
        binder.sendFrames(count,timeInterval,frmInteval);
    }



    //Service通知Activity进行相关操作
    public void initSerViceToActivity(){
        callback = new IPostListener() {
            @Override
            public void toast(String s) {
                Message message = new Message();
                message.what = 0;
                message.obj = s;
                UiHandler.sendMessage(message);
            }
            //得到07A8消息并转发给Fragment更新
            @Override
            public void figureUpdate(String message) {
                if(!message.contains("07a8")){//获取发送完之后的回应
                    esListener.getResponse(message);
                } else if(message.contains("f193")||message.contains("f195")||message.contains("f1a0")){//版本号信息单独获取且拥有较高优先级
                    esListener.getResult(message);
                } else if(message.contains("0562")){
                    fsListener.update(message);
                } else if(message.contains("037f")){
                    Log.e(TAG,"拒绝回应: "+message);
                } else {
                    esListener.getResult(message);
                }
            }
            @Override
            public void updateMenu(String connectsts){
                Message message = new Message();
                message.what = 1;
                message.obj = connectsts;
                UiHandler.sendMessage(message);
            }
            @Override
            public void showDialog(String s) {
                waitDialog = new WaitDialog(MainActivity.this,R.style.dialog);
                waitDialog.setTips(s);
                waitDialog.show();
            }
            @Override
            public void dismissDialog() {
                waitDialog.dismiss();
            }
            @Override
            public void addFrameData(ItemData itemData) {
                dsListener.addFrameData(itemData);
            }
            @Override
            public void addSendData(ItemData itemData) {
                dsListener.addSendFrame(itemData);
            }
            @Override
            public void addErrData(ErrData errData) {
                dsListener.addErrFrame(errData);
            }
        };
    }

    //绑定数据接收服务
    public void bindService(){
        startServiceIntent = new Intent(this,MyService.class);
        startService(startServiceIntent);
        bindService(startServiceIntent,conn, Service.BIND_AUTO_CREATE);
        Handler updateHandler = new Handler();
        //延迟200ms，防止service来不及启动
        updateHandler.postDelayed(getFigure,200);
    }

    Runnable getFigure = new Runnable() {
        @Override
        public void run() {
            binder.setListener(callback);//将service中的接口与这里的接口绑定起来·
            binder.connectCar();
        }
    };

    @Override
    public void onBackPressed() {
        new  AlertDialog.Builder(this)
                .setTitle("确认" )
                .setMessage("确定退出软件吗？" )
                .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        AppManager.getInstance().exitApp();
                    }
                })
                .setNegativeButton("返回" , null)
                .show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbindService(conn);
        stopService(startServiceIntent);
    }
}