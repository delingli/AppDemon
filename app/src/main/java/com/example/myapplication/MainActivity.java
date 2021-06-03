package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;


import com.ldl.messenger.MessengerEvent;
import com.ldl.messenger.observer.IObserver;
import com.ldl.messenger.observer.MessagerObserverble;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;


public class MainActivity extends AppCompatActivity implements IObserver {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MessagerObserverble.getInstance().registObserver(this);
        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SecondActivity.class);
                MainActivity.this.startActivity(intent);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MessagerObserverble.getInstance().removeObserver(this);
    }

    @Override
    public void update(MessengerEvent mes) {
//        String title = mes.data.getString("title");
        String title = getProcessName() + mes.name;
        String value = mes.data.getString("title", "");
        Toast.makeText(this, title + value, Toast.LENGTH_LONG).show();
        Log.d("MainActivity", "接收端进程是？##" + getProcessName());


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
}