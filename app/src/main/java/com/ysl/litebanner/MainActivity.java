package com.ysl.litebanner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.ysl.litebanner.adapter.TestBannerAdapter;
import com.ysl.litebanner.banner.LBannerView;
import com.ysl.litebanner.banner.OnItemClickListener;
import com.ysl.litebanner.mode.Banner;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private LBannerView lBannerView;
    private TestBannerAdapter testBannerAdapter;
    private List<Banner> bannerList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lBannerView = findViewById(R.id.lb_banner);

        bannerList.add(new Banner("http://pic2.16pic.com/00/05/01/16pic_501154_b.jpg"));
        bannerList.add(new Banner("http://pic11.nipic.com/20101117/70384_134856054703_2.jpg"));
        bannerList.add(new Banner("http://pic161.nipic.com/file/20180411/9448607_145554945000_2.jpg"));
        bannerList.add(new Banner("http://pic144.nipic.com/file/20171030/20261224_123636695000_2.jpg"));
        bannerList.add(new Banner("http://pic44.nipic.com/20140716/8716187_010828140000_2.jpg"));
        bannerList.add(new Banner("http://pic1.win4000.com/wallpaper/6/57a2ea76bcc06.jpg"));
        testBannerAdapter = new TestBannerAdapter(this);
        lBannerView.setPageDatas(bannerList,testBannerAdapter);
        lBannerView.start();
        //点击
        lBannerView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Toast.makeText(MainActivity.this,"点击：" + position,Toast.LENGTH_LONG).show();
            }
        });

    }
}
