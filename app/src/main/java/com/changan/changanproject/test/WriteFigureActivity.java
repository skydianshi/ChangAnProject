package com.changan.changanproject.test;
import java.io.FileOutputStream;
import java.io.IOException;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.os.StatFs;
import android.text.format.Formatter;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.changan.changanproject.R;

public class WriteFigureActivity extends Activity implements OnClickListener {

    private TextView tv_main_memerysize;
    private TextView tv_main_sdcard;
    private TextView tv_main_sdcardsize;

    private EditText et_main_content;

    private Button btn_main_writememery;
    private Button btn_main_writesdcard;

    private boolean sdcardMount = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_figure);

        tv_main_memerysize = (TextView) findViewById(R.id.tv_main_memerysize);
        tv_main_sdcard = (TextView) findViewById(R.id.tv_main_sdcard);
        tv_main_sdcardsize = (TextView) findViewById(R.id.tv_main_sdcardsize);
        et_main_content = (EditText) findViewById(R.id.et_main_content);
        btn_main_writememery = (Button) findViewById(R.id.btn_main_writememery);
        btn_main_writesdcard = (Button) findViewById(R.id.btn_main_writesdcard);

        btn_main_writememery.setOnClickListener(this);
        btn_main_writesdcard.setOnClickListener(this);

        //判断sdcard是否已安装
        tv_main_sdcard.setText("sdcard未安装");
        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
            sdcardMount = true;
            tv_main_sdcard.setText("sdcard已安装");
        }

        //得到sdcard和内存的大小及可用容量
        if(sdcardMount){
            tv_main_sdcardsize.setText("sdcard" + getStorgeFileSize(Environment.getExternalStorageDirectory().getPath()));
        }else {
            tv_main_sdcardsize.setText("sdcard总内存:0MB;  可用内存:0MB");
        }
        tv_main_memerysize.setText("手机" + getStorgeFileSize(Environment.getDataDirectory().getPath()));

    }

    /* 两个按钮的监听事件，将内容写入内存文件testmemeryio.txt  */
    @Override
    public void onClick(View v) {
        String text = et_main_content.getText().toString();
        boolean success = true;

        switch (v.getId()) {
            case R.id.btn_main_writesdcard: //写入sdcard
                FileOutputStream fos = null;
                try {
                    fos = new FileOutputStream(Environment.getExternalStorageDirectory().getPath() + "/m.txt");
                    fos.write(text.getBytes("utf-8"));
                    fos.flush();
                } catch (IOException e) {
                    success = false;
                    e.printStackTrace();
                } finally {
                    if(fos != null){
                        try {
                            fos.close();
                            fos = null;
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
                Toast.makeText(WriteFigureActivity.this, success == true ? "写入sdcard文件成功" : "写入sdcard文件失败", 0).show();
                break;

            case R.id.btn_main_writememery: //写入内存
                FileOutputStream openFileOutput = null;

                try {
                    //使用openFileOutput()函数,直接在/data/data/包名/files/目录下创建文件
                    openFileOutput = openFileOutput("testmemeryio.txt", Context.MODE_PRIVATE);  //私有模式写文件
                    openFileOutput.write(text.getBytes("utf-8"));
                    openFileOutput.flush();
                } catch (IOException e) {
                    success = false;
                    e.printStackTrace();
                } finally {
                    if(openFileOutput != null){
                        try {
                            openFileOutput.close();
                            openFileOutput = null;
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
                Toast.makeText(WriteFigureActivity.this, success == true ? "写入内存文件成功" : "写入内存文件失败", 0).show();
                break;

            default:
                break;
        }
    }

    /* 取得内存文件空间大小及可用大小 */
    @SuppressLint("NewApi") private String getStorgeFileSize(String path){
        String fileSizeDesc = null;

        StatFs statFs = new StatFs(path);                           //获得磁盘状态的对象
        long blockSizeLong = statFs.getBlockSizeLong();             //获得磁盘一个扇区的大小
        long blockCountLong = statFs.getBlockCountLong();           //获得磁盘空间总的扇区数
        long availableBlocksLong = statFs.getAvailableBlocksLong(); //获得磁盘空间总的可用扇区数
        fileSizeDesc = "总内存:" + Formatter.formatFileSize(WriteFigureActivity.this, blockSizeLong*blockCountLong)
                + "; 可用内存:" + Formatter.formatFileSize(WriteFigureActivity.this, blockSizeLong*availableBlocksLong);

        return fileSizeDesc;
    }
}