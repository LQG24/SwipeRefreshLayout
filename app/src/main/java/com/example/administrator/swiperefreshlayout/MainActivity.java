package com.example.administrator.swiperefreshlayout;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {


    @Bind(R.id.recyclerview)
    RecyclerView recyclerview;
    @Bind(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    private List<String> mData;
    private ItemsAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        swipeRefreshLayout.setOnRefreshListener(this);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this){
            protected int getExtraLayoutSpace(RecyclerView.State state) {
                return 6000;}
        };
        //如果可以确定每个item的高度是固定的，设置这个选项可以提高性能
        recyclerview.setHasFixedSize(true);
        recyclerview.setLayoutManager(linearLayoutManager);

        mData = new ArrayList<>();

        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(true);
            }
        });

        loadData(false);



    }

    private void loadData(final boolean isMore) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                if(isMore){
                    mAdapter.setLoaded();
                    mData.remove(mData.size() - 1);
                    mAdapter.notifyItemRemoved(mData.size());
                    //                    去除空白的一条
                }
                int size=mData.size();
                for(int i=size;i<size+5;i++)
                {
                    mData.add("第"+i+"个数据");
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        setAdapter();
                        swipeRefreshLayout.setRefreshing(false);
                    }
                });

            }
        }).start();
    }

    private void setAdapter() {
        if(mAdapter==null)
        {
            mAdapter=new ItemsAdapter(this,mData,recyclerview);
            recyclerview.setAdapter(mAdapter);
            mAdapter.setOnLoadMoreListener(new OnLoadMoreListener() {
                @Override
                public void onLoadMore() {
                    mData.add(null);
                    mAdapter.notifyItemInserted(mData.size() - 1);
//                    增加空白的一条
                    loadData(true);
                    mAdapter.setLoading();
                }
            });
        }
        else {
            mAdapter.notifyDataSetChanged();
        }

    }

    @Override
    public void onRefresh() {

//        loadData(false);
        refresh();
    }

    private void refresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                final List<String> newData=new ArrayList<String>();
                int index=1;
                for(int i=0;i<5;i++)
                {
                    index=i+1;
                    newData.add("新增第"+index+"个数据");
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mAdapter.addItem(newData);
                        swipeRefreshLayout.setRefreshing(false);
                    }
                });
            }
        },1000);
    }
}
