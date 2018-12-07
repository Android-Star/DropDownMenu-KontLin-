package com.example.wilson.dropdownmenu.java;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.wilson.dropdownmenu.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by ggg on 2018/12/4.
 */

public class JavaActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private MenuView menuview;
    private String headers[] = {"城市", "年龄", "性别", "星座"};
    private List<View> popViews = new ArrayList<>();

    private String citys[] = {"不限", "武汉", "北京", "上海", "成都", "广州", "深圳", "重庆", "天津", "西安", "南京", "杭州"};
    private String ages[] = {"不限", "18岁以下", "18-22岁", "23-26岁", "27-35岁", "35岁以上"};
    private String sexs[] = {"不限", "男", "女"};
    private String constellations[] = {"不限", "白羊座", "金牛座", "双子座", "巨蟹座", "狮子座", "处女座", "天秤座", "天蝎座", "射手座", "摩羯座", "水瓶座", "双鱼座"};
    private int imagids[] = {R.mipmap.city, R.mipmap.age, R.mipmap.sex, R.mipmap.xz};

    private int constellationPostion = 0;

    private GirdDropDownAdapter cityAdapter;
    private ListDropDownAdapter ageAdapter, sexAdapter;
    private ConstellationAdapter constellationAdapter;
    private ListView lvCity;
    private ListView lvAge;
    private ListView lvSex;
    private GridView gv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        menuview = findViewById(R.id.menuview);

        initViews();
    }

    private void initViews() {
        lvCity = new ListView(this);
        lvCity.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        cityAdapter = new GirdDropDownAdapter(this, Arrays.asList(citys));
        lvCity.setDividerHeight(0);
        lvCity.setAdapter(cityAdapter);

        lvAge = new ListView(this);
        lvAge.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        ageAdapter = new ListDropDownAdapter(this, Arrays.asList(ages));
        lvAge.setDividerHeight(0);
        lvAge.setAdapter(ageAdapter);

        lvSex = new ListView(this);
        lvSex.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        sexAdapter = new ListDropDownAdapter(this, Arrays.asList(sexs));
        lvSex.setDividerHeight(0);
        lvSex.setAdapter(sexAdapter);


        gv = new GridView(this);
        gv.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        gv.setNumColumns(4);
        gv.setHorizontalSpacing(4);
        gv.setVerticalSpacing(4);
        gv.setPadding(10, 10, 10, 10);
        gv.setBackgroundColor(Color.WHITE);
        constellationAdapter = new ConstellationAdapter(this, Arrays.asList(constellations));
        gv.setAdapter(constellationAdapter);

        lvCity.setOnItemClickListener(this);
        lvAge.setOnItemClickListener(this);
        lvSex.setOnItemClickListener(this);
        gv.setOnItemClickListener(this);

        popViews.add(lvCity);
        popViews.add(lvAge);
        popViews.add(lvSex);
        popViews.add(gv);

        ImageView contentView = new ImageView(this);
        contentView.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT));
        contentView.setScaleType(ImageView.ScaleType.CENTER);

        menuview.setContainerView(Arrays.asList(headers), popViews, contentView);

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (parent == lvCity) {
            cityAdapter.setCheckItemPosition(position);
            menuview.setTabText(0, position == 0 ? headers[0] : citys[position]);
            menuview.setImageResource(imagids[0]);
        } else if (parent == lvAge) {
            ageAdapter.setCheckItemPosition(position);
            menuview.setTabText(1, position == 0 ? headers[1] : ages[position]);
            menuview.setImageResource(imagids[1]);
        } else if (parent == lvSex) {
            sexAdapter.setCheckItemPosition(position);
            menuview.setTabText(2, position == 0 ? headers[2] : sexs[position]);
            menuview.setImageResource(imagids[2]);
        } else if (parent == gv) {
            constellationAdapter.setCheckItemPosition(position);
            menuview.setTabText(3, position == 0 ? headers[3] : constellations[position]);
            menuview.setImageResource(imagids[3]);
        } else {
            Toast.makeText(this, "出错了！", Toast.LENGTH_SHORT).show();
        }
        menuview.closeMenu();
    }


    @Override
    public void onBackPressed() {
        if (menuview.isShowing()) {
            menuview.closeMenu();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
