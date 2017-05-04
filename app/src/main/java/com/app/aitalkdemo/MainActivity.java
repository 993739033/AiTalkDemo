package com.app.aitalkdemo;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements HttpCallbackListener{
    private RecyclerView Rv_showtalk;
    private EditText Et_input;
    private Button Btn_send;
    private MsgAdapter msgAdapter;
    private List<Msg> msgList = new ArrayList<>();

    private MyData myData;
    private SQLiteDatabase mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Rv_showtalk = (RecyclerView) findViewById(R.id.rv_showtalk);
        Et_input = (EditText) findViewById(R.id.Et_input);
        Btn_send = (Button) findViewById(R.id.Btn_send);
        initView();
    }
    private void initView(){
        myData = new MyData(this, "Msg.db", null, 1);
        Msg[] send_msg = { new Msg(0,"yooo! 接下来聊点什么呢？","",Msg.TYPE_RECEIVE,null,getTime()),
                new Msg(0,"又是美好的一天呢！(/≥▽≤/)","",Msg.TYPE_RECEIVE,null,getTime()),
                new Msg(0," 当当当！˙ω˙ 愚蠢的人类有什么事吗？","",Msg.TYPE_RECEIVE,null,getTime())
        };
        int i= (int) (Math.random()*send_msg.length);
        Msg m = send_msg[i];
        msgList.add(m);
        saveMsgInSql(m);
        msgAdapter = new MsgAdapter(msgList);
        LinearLayoutManager linearLayout = new LinearLayoutManager(this);
        Rv_showtalk.setLayoutManager(linearLayout);
        Rv_showtalk.setAdapter(msgAdapter);
        Btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Msg send_msg = new Msg(0,null,null,Msg.TYPE_SEND,Et_input.getText().toString(),getTime());
                MyHttpUtils.SendMsg(Et_input.getText().toString(),MainActivity.this);
                saveMsgInSql(send_msg);
                Et_input.setText("");
                msgList.add(send_msg);
                msgAdapter.notifyDataSetChanged();
                Rv_showtalk.scrollToPosition(msgList.size()-1);
            }
        });
    }

    @Override
    public void onFinish(String respone) {
        try {
            JSONObject jsonObject = new JSONObject(respone);
            int code = jsonObject.getInt("code");
            String text = jsonObject.getString("text");
            String url=null;
            if (jsonObject.has("url")) {
                url = jsonObject.getString("url");
            }
            Msg msg = new Msg(code,text,url,Msg.TYPE_RECEIVE,null,getTime());
            saveMsgInSql(msg);
            receiveMsg(msg);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onError(Exception e) {

    }
    private void receiveMsg(final Msg msg){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                msgList.add(msg);
                msgAdapter.notifyDataSetChanged();
                Rv_showtalk.scrollToPosition(msgList.size()-1);
            }
        });
    }
    static long oldtime=0;
    private String getTime(){
        long currentTime=System.currentTimeMillis();
        Date date=new Date();
        int randNum= (int) (Math.random()*1000);
        SimpleDateFormat simpledate = new SimpleDateFormat("yyyy年MM月dd日  HH:mm:ss");
        String str= simpledate.format(date);
        if (currentTime-oldtime>=5*60*1000){
            oldtime=currentTime;
            str = str + ",true,"+randNum;
            return str;
        }else{
            str=str+",false,"+randNum;
        }
        return str;
    }
  private void saveMsgInSql(Msg msg){
     mDatabase=myData.getWritableDatabase();
      ContentValues cv = new ContentValues();
      cv.put(MyData.CODE, msg.getCode());
      cv.put(MyData.TEXT, msg.getText());
      cv.put(MyData.INFO, msg.getInfo());
      cv.put(MyData.DATE, msg.getDate());
      cv.put(MyData.TYPE, msg.getType());
      cv.put(MyData.URL,msg.getUrl());
      mDatabase.insert(MyData.NAME, null, cv);
//      Toast.makeText(this, "save", Toast.LENGTH_SHORT).show();
  }


}
