package com.app.aitalkdemo;

import android.database.sqlite.SQLiteDatabase;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;


/**
 * Created by 知らないのセカイ on 2017/5/2.
 */

public class MsgAdapter extends RecyclerView.Adapter<MsgAdapter.ViewHolder>  {
     List<Msg> msgList;

    public MsgAdapter(List<Msg> msgList) {
        this.msgList = msgList;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout layout_left,layout_right;
        TextView Tv_this, Tv_other,Tv_date;
        Button Btn_this,Btn_other;
        public ViewHolder(View itemView) {
            super(itemView);
            layout_left = (LinearLayout) itemView.findViewById(R.id.left_layout);
            layout_right = (LinearLayout) itemView.findViewById(R.id.right_layout);
            Tv_this = (TextView) itemView.findViewById(R.id.Other_Tv);//搞反了
            Tv_other = (TextView) itemView.findViewById(R.id.This_Tv);
            Btn_this = (Button) itemView.findViewById(R.id.This_Btn);
            Btn_other = (Button) itemView.findViewById(R.id.Other_Btn);
            Tv_date = (TextView) itemView.findViewById(R.id.Date_Tv);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.talk_item,parent,false);
        myData = new MyData(parent.getContext(), "Msg.db", null, 1);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
         final Msg  msg = msgList.get(position);
        String date[]=null;
        if (msg.getDate()==null){
            date= new String[]{"false", "false","false"};
        }
        else {
           date = msg.getDate().split(",");
        }

        if (date[1].equals("true")&&holder.Tv_date.getVisibility()==View.GONE){
            holder.Tv_date.setVisibility(View.VISIBLE);
            holder.Tv_date.setText(date[0]);
        }else {
            holder.Tv_date.setVisibility(View.GONE);
        }
        if (msg.getType() == Msg.TYPE_RECEIVE) {
            holder.layout_right.setVisibility(View.GONE);
            holder.layout_left.setVisibility(View.VISIBLE);
            holder.Tv_this.setText(msg.getText() + "\n" + (msg.getUrl() == null ? "" : msg.getUrl()));
            holder.Btn_other.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    msgList.remove(msg);
                    deleteMsgInSql(msg);
                    notifyDataSetChanged();
                }
            });
        } else if (msg.getType() == Msg.TYPE_SEND) {
            holder.layout_right.setVisibility(View.VISIBLE);
            holder.layout_left.setVisibility(View.GONE);
            holder.Tv_other.setText(msg.getInfo()+"\n");
            holder.Btn_this.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    msgList.remove(msg);
                    deleteMsgInSql(msg);
                    notifyDataSetChanged();
                }
            });

        }else{
            //Todo type null
            Log.w("msg","wei shu chu log" );
        }
    }

    @Override
    public int getItemCount() {
        return msgList.size();
    }

    /**
     * 用于获取表对象 定义一个删除数据库的方法
     */
    private MyData myData;
    private SQLiteDatabase mDatabase;
    private void deleteMsgInSql(Msg msg){
        mDatabase=myData.getWritableDatabase();
        mDatabase.delete(MyData.NAME,"date=?",new String[]{msg.getDate()});
    }


}
