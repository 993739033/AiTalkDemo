package com.app.aitalkdemo;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;


/**
 * Created by 知らないのセカイ on 2017/5/2.
 */

public class MsgAdapter extends RecyclerView.Adapter<MsgAdapter.ViewHolder>{
     List<Msg> msgList;

    public MsgAdapter(List<Msg> msgList) {
        this.msgList = msgList;
    }


    static class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout layout_left,layout_right;
        TextView Tv_this, Tv_other;
        public ViewHolder(View itemView) {
            super(itemView);
            layout_left = (LinearLayout) itemView.findViewById(R.id.left_layout);
            layout_right = (LinearLayout) itemView.findViewById(R.id.right_layout);
            Tv_this = (TextView) itemView.findViewById(R.id.Other_Tv);
            Tv_other = (TextView) itemView.findViewById(R.id.This_Tv);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.talk_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Msg msg = msgList.get(position);
        if (msg.getType() == Msg.TYPE_RECEIVE) {
            holder.layout_right.setVisibility(View.GONE);
            holder.layout_left.setVisibility(View.VISIBLE);
            holder.Tv_this.setText(msg.getText() + "\n" + (msg.getUrl() == null ? "" : msg.getUrl()));


        } else if (msg.getType() == Msg.TYPE_SEND) {
            holder.layout_right.setVisibility(View.VISIBLE);
            holder.layout_left.setVisibility(View.GONE);
            holder.Tv_other.setText(msg.getInfo()+"\n");

        }else{
            //Todo type null
            Log.w("msg","wei shu chu log" );
        }
    }

    @Override
    public int getItemCount() {
        return msgList.size();
    }



}
