package com.code.recyclerviewpager;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.code.lib.LoopRecyclerViewPager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class MainActivity extends AppCompatActivity {

    LoopRecyclerViewPager mLoopViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mLoopViewPager = findViewById(R.id.loop_viewpager);
        LinearLayoutManager layout = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,
                false);
        mLoopViewPager.setTriggerOffset(0.15f);
        mLoopViewPager.setFlingFactor(0.25f);
        mLoopViewPager.setLayoutManager(layout);
        LoopPagerAdapter pagerAdapter = new LoopPagerAdapter(this, mLoopViewPager, setData());
        mLoopViewPager.setAdapter(pagerAdapter);
        mLoopViewPager.setHasFixedSize(true);
        mLoopViewPager.setLongClickable(true);
        mLoopViewPager.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                int childCount = mLoopViewPager.getChildCount();
                int width = mLoopViewPager.getChildAt(0).getWidth();
                int padding = (mLoopViewPager.getWidth() - width) / 2;
                for (int j = 0; j < childCount; j++) {
                    View v = recyclerView.getChildAt(j);
                    //往左 从 padding 到 -(v.getWidth()-padding) 的过程中，由大到小
                    float rate = 0;
                    if (v.getLeft() <= padding) {
                        if (v.getLeft() >= padding - v.getWidth()) {
                            rate = (padding - v.getLeft()) * 1f / v.getWidth();
                        } else {
                            rate = 1;
                        }
                        v.setScaleY(1 - rate * 0.1f);
                    } else {
                        //往右 从 padding 到 recyclerView.getWidth()-padding 的过程中，由大到小
                        if (v.getLeft() <= recyclerView.getWidth() - padding) {
                            rate = (recyclerView.getWidth() - padding - v.getLeft()) * 1f / v.getWidth();
                        }
                        v.setScaleY(0.9f + rate * 0.1f);
                    }
                }
            }
        });

        mLoopViewPager.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                if (mLoopViewPager.getChildCount() < 3) {
                    if (mLoopViewPager.getChildAt(1) != null) {
                        View v1 = mLoopViewPager.getChildAt(1);
                        v1.setScaleY(0.9f);
                    }
                } else {
                    if (mLoopViewPager.getChildAt(0) != null) {
                        View v0 = mLoopViewPager.getChildAt(0);
                        v0.setScaleY(0.9f);
                    }
                    if (mLoopViewPager.getChildAt(2) != null) {
                        View v2 = mLoopViewPager.getChildAt(2);
                        v2.setScaleY(0.9f);
                    }
                }
            }
        });

//        /
//        mLoopViewPager.setOnItemClickListener(new LoopRecyclerViewPager.OnItemClickListener() {
//            @Override
//            public void click(View view, int position) {
//
//            }
//        });

        //点击事件
        pagerAdapter.setOnItemPagerClickLis(new LoopPagerAdapter.OnPagerItemClickListener() {
            @Override
            public void onPagerItemClick(DataBean bean) {
                Toast.makeText(MainActivity.this, "点击了" + bean.getTitle(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private List<DataBean> setData() {
        List<DataBean> dataList = new ArrayList<>();
        DataBean dataBean1 = new DataBean();
        dataBean1.setTitle("Title1");
        dataBean1.setImage("https://media1.rrl360.com/product/0001/09/thumb_8568_default.jpg?x-oss-process=image/resize,h_400,w_600/format,jpg");
        DataBean dataBean2 = new DataBean();
        dataBean2.setTitle("Title2");
        dataBean2.setImage("https://media1.rrl360.com/product/0001/08/thumb_7903_default.jpeg?x-oss-process=image/resize,h_400,w_600/format,jpg");
        DataBean dataBean3 = new DataBean();
        dataBean3.setTitle("Title3");
        dataBean3.setImage("https://media1.rrl360.com/product/0002/63/thumb_162173_default.jpg?x-oss-process=image/resize,h_400,w_600/format,jpg");
        dataList.add(dataBean1);
        dataList.add(dataBean2);
        dataList.add(dataBean3);
        return dataList;
    }

}
