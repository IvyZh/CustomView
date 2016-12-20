package com.ivy.customview;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ListView mLvView;
    private ArrayList<String> data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mLvView = (ListView) findViewById(R.id.lv_view);

        MyAdapter adapter = new MyAdapter();
        data = new ArrayList<>();
        data.add("Android 自定义View (一)");
        data.add("Android 自定义View (二) 进阶");
        data.add("新手自定义view练习实例之（一） 泡泡弹窗");


        mLvView.setAdapter(adapter);

        initListener();
    }

    private void initListener() {
        mLvView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> view, View view1, int i, long l) {
                onClickItem(i);
            }
        });
    }

    private void onClickItem(int item) {
        switch (item) {
            case 0:
            case 1:
            case 2:
                startActivity(CustomViewActivity.class);
                break;
        }
    }


    public void startActivity(Class cls) {
        startActivity(new Intent(this, cls));
    }


    class MyAdapter extends BaseAdapter {


        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public Object getItem(int i) {
            return i;
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup group) {
            View v = View.inflate(MainActivity.this, android.R.layout.simple_list_item_1, null);
            TextView tv = (TextView) v.findViewById(android.R.id.text1);
            tv.setText(data.get(i));
            return v;
        }
    }
}
