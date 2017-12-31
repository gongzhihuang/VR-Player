package com.example.vrplayer;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private TabLayout mtablayout;
    private ViewPager mViewPager;
    public static final String []sTitle = new String[]{"图片","视频"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mViewPager = (ViewPager) findViewById(R.id.view_pager);
        mtablayout = (TabLayout)findViewById(R.id.tab_layout);
        mtablayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        mtablayout.setupWithViewPager(mViewPager);//关联Tablayout和ViewPager

        List<Fragment> fragments = new ArrayList<>();
        fragments.add(FirstFragment.newInstance());//添加了FirstFragment
        fragments.add(SecondFragment.newInstance());//添加了SecondFragment

        MyFragmentAdapter adapter = new MyFragmentAdapter(getSupportFragmentManager(),fragments,
                Arrays.asList(sTitle));//Fragment适配器
        mViewPager.setAdapter(adapter);

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }
}
