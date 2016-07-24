package com.example.administrator.swiperefreshlayout;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by Linqiaogeng on 2016/7/15.
 */
public class ItemsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private List<String> mData;
    private Context context;
    private OnLoadMoreListener onLoadMoreListener;
    private int lastVisibleItem, totalItemCount;
    private boolean loading;
    private int visibleThreshold = 1;
    private static final int EMPTY_VIEW = 1;
    private static final int VIEW_PROG = 2;
    private  LayoutInflater mLayoutInflater;

    public ItemsAdapter(Context context, List<String> data, RecyclerView recyclerview) {
        this.context=context;
        this.mData=data;
        if (recyclerview.getLayoutManager() instanceof LinearLayoutManager) {
            final LinearLayoutManager linearLayoutManager= (LinearLayoutManager) recyclerview.getLayoutManager();

        recyclerview.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                totalItemCount=linearLayoutManager.getItemCount();
                lastVisibleItem=linearLayoutManager.findLastVisibleItemPosition();
                if(!loading && totalItemCount<=(lastVisibleItem+visibleThreshold))
                {
                    if(onLoadMoreListener!=null)
                        onLoadMoreListener.onLoadMore();
                }
            }
        });
        }
    }

    @Override
    public int getItemViewType(int position) {
        if(mData.size()==0)
        {
            return  EMPTY_VIEW;
        }
       return mData.get(position)!=null ? super.getItemViewType(position):VIEW_PROG;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mLayoutInflater=LayoutInflater.from(context);
        if(viewType==EMPTY_VIEW)
        {

            return new EmptyViewHolder(mLayoutInflater.inflate(R.layout.empty_view,parent,false));
        }
        else if(viewType==VIEW_PROG)
        {
            return new ProgressViewHolder(mLayoutInflater.inflate(R.layout.progressbar_item,parent,false));

        }
        else {

            return new ItemHolder(mLayoutInflater.inflate(R.layout.item,parent,false));
        }


    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof ItemHolder)
        {
            ((ItemHolder) holder).mTextView.setText(mData.get(position));
        }else if(holder instanceof ProgressViewHolder)
        {
            ((ProgressViewHolder) holder).progressBar.setIndeterminate(true);
        }

    }


    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void setLoading() {
        loading = true;
    }

    public void setLoaded() {
        loading = false;
    }
    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }

    //添加数据
    public void addItem(List<String> newDatas) {
        //mTitles.add(position, data);
        //notifyItemInserted(position);
        newDatas.addAll(mData);
        mData.removeAll(mData);
        mData.addAll(newDatas);
        notifyDataSetChanged();
    }
}
