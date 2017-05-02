package com.app.aitalkdemo;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by 知らないのセカイ on 2017/5/2.
 */

public class MyHttpUtils {
    private final static String KEY = "135aec50af6e4a5684e59a19ab976ed3";

    public static void SendMsg(final String info, final HttpCallbackListener httpCallbackListener) {


        new Thread(new Runnable() {
            HttpURLConnection connection;

            @Override
            public void run() {
                try {
                    URL url = new URL("http://www.tuling123.com/openapi/api");
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("POST");
                    connection.setReadTimeout(5000);
                    connection.setConnectTimeout(5000);
                    DataOutputStream dataOutputStream = new DataOutputStream(connection.getOutputStream());

                    dataOutputStream.writeBytes("key=" + KEY + "&" + "info=" + URLEncoder.encode(info,"utf-8"));
                    InputStream inputStream = connection.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "utf-8"));
                    StringBuffer stringBuffer = new StringBuffer();
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        stringBuffer.append(line);
                    }
                    if (httpCallbackListener!=null){
                        httpCallbackListener.onFinish(stringBuffer.toString());
                    }
                    if (dataOutputStream != null) {
                        dataOutputStream.close();
                    }
                    if (inputStream != null) {
                        inputStream.close();
                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                    if (httpCallbackListener != null) {
                        httpCallbackListener.onError(e);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    if (httpCallbackListener != null) {
                        httpCallbackListener.onError(e);
                    }
                } finally {
                    if (connection != null) {
                        connection.disconnect();
                    }
                }
            }
        }).start();

    }
}
