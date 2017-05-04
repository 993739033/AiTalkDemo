package com.app.aitalkdemo;

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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements HttpCallbackListener{
    private RecyclerView Rv_showtalk;
    private EditText Et_input;
    private Button Btn_send;
    private MsgAdapter msgAdapter;
    private List<Msg> msgList = new ArrayList<>();

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
        Msg send_msg = new Msg(0,"sfsdfsadfsdfsdfsdfjaskldfjlaksdjfksldjflsadjflksdjf","sdfsdsdfsadfasdfsdfsdffs",Msg.TYPE_RECEIVE,null);
        msgList.add(send_msg);
        msgAdapter = new MsgAdapter(msgList);
        LinearLayoutManager linearLayout = new LinearLayoutManager(this);
        Rv_showtalk.setLayoutManager(linearLayout);
        Rv_showtalk.setAdapter(msgAdapter);
        Btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Msg send_msg = new Msg(0,null,null,Msg.TYPE_SEND,Et_input.getText().toString());
                MyHttpUtils.SendMsg(Et_input.getText().toString(),MainActivity.this);
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
            Msg msg = new Msg(code,text,url,Msg.TYPE_RECEIVE,null);
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


}
