package com.example.gaoshuang20170630;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.google.gson.Gson;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.maxwin.view.XListView;

public class MainActivity extends AppCompatActivity implements XListView.IXListViewListener {
    private String urlpath = "http://qhb.2dyt.com/Bwei/news?page=1&type=5&postkey=1503d";
    private MyAdapter adapter;
    private Banner banner;
    private XListView lv;
    private List li=new ArrayList();
    private List<Data.ListBean> list = new ArrayList<>();
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Gson gson = new Gson();
            Data data = gson.fromJson(msg.obj.toString(), Data.class);
            list.addAll(data.getList());
            List imageList = new ArrayList<>();
            for (Data.ListBean listBean : list) {
                String[] split = listBean.getPic().split("\\|");
                imageList.add(split[0]);
                imageList.add(split[1]);
            }

            banner.setImages(imageList);
            banner.start();
            stopxlv();
            adapter.notifyDataSetChanged();
        }
    };


    private void stopxlv() {
        lv.stopLoadMore();
        lv.stopRefresh();
        lv.setRefreshTime("刚刚");
    }

    public void load() {
      Map map = new HashMap<>();
        map.put("page", "1");
        map.put("type", "6");
        map.put("postkey", "1503d");

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        banner = (Banner) findViewById(R.id.banner);
        lv = (XListView) findViewById(R.id.lv);

        //设置图片加载器
        banner.setImageLoader(new BannerImageLoader(this));
        //设置显示样式
        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
        //设置圆点位置
        banner.setIndicatorGravity(BannerConfig.LEFT);
        banner.setBannerAnimation(Transformer.ScaleInOut);
        initData();
        initview();
        load();
        adapter = new MyAdapter(this, list);
        lv.setAdapter(adapter);

        if (isNetworkAvailable(MainActivity.this)) {
        } else {
            Toast.makeText(MainActivity.this, "没有网络，应用无法正常使用，请进行开启数据", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent("android.settings.WIRELESS_SETTINGS");
            startActivity(intent);
        }
    }

    public boolean isNetworkAvailable(Context context) {
        if (context != null) {
            ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (manager != null) {
                NetworkInfo info = manager.getActiveNetworkInfo();
                if (info != null) {
                    return info.isAvailable();
                }
            }
        }
        return false;
    }

    private void initview() {
        lv.setPullRefreshEnable(true);
        lv.setPullLoadEnable(true);
        lv.setXListViewListener(this);
    }

    private void initData() {
        new Thread() {
            @Override
            public void run() {
                String result = UrlUtils.getUrlConnect(urlpath);
                Message msg = Message.obtain();
                msg.what = 1;
                msg.obj = result;
                handler.sendMessage(msg);
            }
        }.start();
    }

    @Override
    public void onRefresh() {
        list.clear();
        initData();
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onLoadMore() {
        initData();
        adapter.notifyDataSetChanged();
    }
}
