package com.app.aitalkdemo;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Handler;

import static android.view.View.VISIBLE;

public class MainActivity extends AppCompatActivity implements HttpCallbackListener{
    private RecyclerView Rv_showtalk;
    private EditText Et_input;
    private Button Btn_send;
    private MsgAdapter msgAdapter;
    private List<Msg> msgList = new ArrayList<>();

    private MyData myData;
    private SQLiteDatabase mDatabase;

    private Button tucao,Btn_tucao;
    private TextView Tv_xiao;
    private EditText tucao_content;
    private LinearLayout content_input;


    private String[] keys = {"135aec50af6e4a5684e59a19ab976ed3", "c85e586f47494a02be14c6ba8aa782a1", "c009acedff614ab3abd18c7bae6d8321"
            , "0e66d7b5b77a40af854da4e2a3b28bf7", "9d599900c0064a79b2a5515e23cf4393"};
    private String key;

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
       int i= (int) (Math.random()*keys.length);
        key = keys[i];
        Btn_tucao = (Button) findViewById(R.id.Btn_tucao);
        tucao = (Button) findViewById(R.id.tucao_first);
        Tv_xiao = (TextView) findViewById(R.id.xiao);
        tucao_content = (EditText) findViewById(R.id.tucao_content);
        content_input = (LinearLayout) findViewById(R.id.tucao_input);
        tucao.setOnClickListener(new View.OnClickListener() {
            int i=0;
            @Override
            public void onClick(View v) {
                if (Tv_xiao.getVisibility() == View.GONE) {
                    Tv_xiao.setVisibility(VISIBLE);
                }else{
                    i++;
                    if (i == 5 && content_input.getVisibility() == View.GONE) {
                        content_input.setVisibility(VISIBLE);
                    }
                }
            }
        });

        Btn_tucao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content=tucao_content.getText().toString();
                if (!TextUtils.isEmpty(content)){
                    if(content.equals("我就是巴大仙")||content.equals("我就是巴鑫辉")||content.equals("我就是老巴")){
                        tucao_content.setText("");
                        Toast.makeText(MainActivity.this, "大仙好！ 大仙再见！", Toast.LENGTH_SHORT).show();
                        final android.os.Handler handler =new android.os.Handler();
                         Runnable runable=new Runnable(){
                            @Override
                            public void run() {
                                handler.removeCallbacks(this);
                                System.exit(0);
                            }
                        };

                        handler.postDelayed(runable,2000);

                    }

                    Toast.makeText(MainActivity.this, "兄弟！想修仙何不问问 巴大仙", Toast.LENGTH_SHORT).show();
                    tucao_content.setText("");
                }
            }
        });



        myData = new MyData(this, "Msg.db", null, 1);
        mDatabase=myData.getReadableDatabase();
        int showsize=getMaxCursorId() - 50>1?getMaxCursorId() - 50:0;
//        Cursor mcursor = mDatabase.query(MyData.NAME, new String[]{"id"}, "id>=?", new String[]{String.valueOf(showsize)}, null, null, null, null);
        Cursor mcursor = mDatabase.query(MyData.NAME, null, null, null, null, null, null, null);
        if ( mcursor.moveToFirst()&&mcursor!=null) {

            do {
                int code=mcursor.getInt(mcursor.getColumnIndex(MyData.CODE));
                String text=mcursor.getString(mcursor.getColumnIndex(MyData.TEXT));
                String url=mcursor.getString(mcursor.getColumnIndex(MyData.URL));
                int type=mcursor.getInt(mcursor.getColumnIndex(MyData.TYPE));
                String date=mcursor.getString(mcursor.getColumnIndex(MyData.DATE));
                String info=mcursor.getString(mcursor.getColumnIndex(MyData.INFO));
                Msg msg = new Msg(code, text, url, type, info, date);
                msgList.add(msg);

            } while (mcursor.moveToNext());
            mcursor.close();
        }
        Msg[] send_msg = { new Msg(0,"yooo! 接下来聊点什么呢？","",Msg.TYPE_RECEIVE,null,getTime()),
                new Msg(0,"又是美好的一天呢！(/≥▽≤/)","",Msg.TYPE_RECEIVE,null,getTime()),
                new Msg(0," 当当当！˙ω˙ 愚蠢的人类有什么事吗？","",Msg.TYPE_RECEIVE,null,getTime())
        };
        int k= (int) (Math.random()*send_msg.length);
        Msg m = send_msg[k];
        msgList.add(m);
        saveMsgInSql(m);
        msgAdapter = new MsgAdapter(msgList);
        LinearLayoutManager linearLayout = new LinearLayoutManager(this);
        Rv_showtalk.setLayoutManager(linearLayout);
        Rv_showtalk.setAdapter(msgAdapter);
        Rv_showtalk.scrollToPosition(msgList.size()-1);
        Btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Msg send_msg = new Msg(0,null,null,Msg.TYPE_SEND,Et_input.getText().toString(),getTime());
                MyHttpUtils.SendMsg(Et_input.getText().toString(),MainActivity.this,key);
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
  private int getMaxCursorId(){
      int max=0;
      mDatabase= myData.getReadableDatabase();
      Cursor cursor = mDatabase.query(MyData.NAME, null, null, null, null, null, null);
      if( cursor.moveToLast()){
        max = cursor.getInt(cursor.getColumnIndex("id"));
      };

      return max;
  }


}
