package com.example.administrator.swiperefreshlayout;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

/**
 * Created by Administrator on 2016/1/26.
 */
public class EmptyViewHolder extends RecyclerView.ViewHolder {
    public TextView mTextView;
    public EmptyViewHolder(View itemView) {
        super(itemView);
        mTextView = (TextView)itemView.findViewById(R.id.textView4);
    }
}
