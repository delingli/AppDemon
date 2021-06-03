package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.ldl.messenger.ClientImpl;
import com.ldl.messenger.IClient;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class SecondActivity extends AppCompatActivity {

    private IClient mClient;
    private TextView textView,textView2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        mClient = new ClientImpl();
        mClient.bindService(this);
        textView = findViewById(R.id.textView);
        textView2 = findViewById(R.id.textView2);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("title", "测试啊啊啊啊...");
                mClient.sendMessage("发送xxx",bundle,true);
                textView2.setText("SecondActivity"+ "发送端进程是？##" + getProcessName());
                Log.d("SecondActivity", "发送端进程是？##" + getProcessName());
            }
        });
    }

    public static String getProcessName() {
        try {
            File file = new File("/proc/" + android.os.Process.myPid() + "/" + "cmdline");
            BufferedReader myBufferReader = new BufferedReader(new FileReader(file));
            String processName = myBufferReader.readLine().trim();
            myBufferReader.close();
            return processName;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mClient.unBindService(this);
    }
}