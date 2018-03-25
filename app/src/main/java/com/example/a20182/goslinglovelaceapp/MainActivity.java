package com.example.a20182.goslinglovelaceapp;

import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity implements ScrollViewListener{
    private ObservableScrollView mScrollView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mScrollView = (ObservableScrollView) findViewById(R.id.scroll);
        mScrollView.setScrollViewListener(this);

        TextView tv = (TextView) findViewById(R.id.tv);

        try {
            InputStream in = new InputStream() {
                @Override
                public int read() throws IOException {
                    return 0;
                }
            };
            //  拿到文件的输入流
            if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE ){
                if(this.getResources().getConfiguration().locale.toString().equals("zh_CN")){
                    in = getAssets().open( "james_gosling_zh.txt");
                }else {
                    in = getAssets().open("james_gosling.txt");
                }
            }
            else if(this.getResources().getConfiguration().orientation ==Configuration.ORIENTATION_PORTRAIT) {
                if(this.getResources().getConfiguration().locale.toString().equals("zh_CN")){
                    in = getAssets().open( "ada_lovelace_zh.txt");
                }else{
                    in = getAssets().open( "ada_lovelace.txt");
                }
            }
            //  获得内容的长度
            int size = in.available();

            byte[] buffer = new byte[size];
            //  把内存从inputstream内读取到数组上
            in.read(buffer);
            in.close();

            //  把内容复制给String
            String content = new String(buffer,"GB2312");
            tv.setText(content);

            //  初始化设计滑动距离只能这样写
            //  mScrollView.smoothScrollTo()或mScrollView.mScrollView.scrollTo()均无效!
            //  根据记录的y来回到上次离开的地方
            mScrollView.post(new Runnable() {

                @Override
                public void run() {
                    //  500为模拟值,实际上可以从轻量级数据库orSQLITE获取上次记录的值
                    mScrollView.smoothScrollTo(0, 500);
                }
            });

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    @Override
    public void onScrollChanged(ObservableScrollView scrollView, int x, int y,
                                int oldx, int oldy) {
        //  每次滑动记录y
        //  使用SharedPreferences或者SQLITE
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        switch (newConfig.orientation)
        {
            case (Configuration.ORIENTATION_LANDSCAPE):
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                break;
            case (Configuration.ORIENTATION_PORTRAIT):
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                break;
        }
    }

}
