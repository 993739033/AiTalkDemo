package com.app.aitalkdemo;

import android.content.Context;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by 知らないのセカイ on 2017/5/5.
 */

public class left_layout extends LinearLayout {
    private Button tucao,Btn_tucao;
    private TextView Tv_xiao;
    private EditText tucao_content;
    private LinearLayout content_input;

    public left_layout(Context context) {
        super(context);
    }

    public left_layout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
//       initview(context);
    }

    private void initview(Context context) {
        LayoutInflater.from(context).inflate(R.layout.left_layout, this);
        Btn_tucao = (Button) findViewById(R.id.Btn_tucao);
        tucao = (Button) findViewById(R.id.tucao_first);
        Tv_xiao = (TextView) findViewById(R.id.xiao);
        tucao_content = (EditText) findViewById(R.id.Et_input);
        content_input = (LinearLayout) findViewById(R.id.tucao_input);
        tucao.setOnClickListener(new OnClickListener() {
            int i=0;
            @Override
            public void onClick(View v) {
                if (Tv_xiao.getVisibility() == GONE) {
                    Tv_xiao.setVisibility(VISIBLE);
                }else{
                    i++;
                    if (i == 5 && content_input.getVisibility() == GONE) {
                        content_input.setVisibility(VISIBLE);
                    }

                }
            }
        });

        Btn_tucao.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                String content=tucao_content.getText().toString();
                if (TextUtils.isEmpty(content)){
                    if(content=="我就是巴大仙"||content=="我就是巴鑫辉"||content=="我就是老巴"){
                        Toast.makeText(getContext(), "大仙好！ 大仙再见！", Toast.LENGTH_SHORT).show();
                        System.exit(0);
                    }

                    Toast.makeText(getContext(), "兄弟！想修仙何不问问 巴大仙", Toast.LENGTH_SHORT).show();
                }
            }
        });




    }

    public left_layout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

    }
}
