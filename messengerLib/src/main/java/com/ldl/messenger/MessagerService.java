package com.ldl.messenger;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;

import androidx.annotation.NonNull;


import com.ldl.messenger.observer.MessagerObserverble;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

/**
 * 作为服务端，日后可以扩展成中转者，进行转发两个其它非主进程的通信消息，
 */
public class MessagerService extends Service {
    public static final int FROM_CLIENT = 0x11;
    public static final int NEED_REPLY = 0x12;
    //指定loop保证handler存在轮训，
    private Messenger mMessager = new Messenger(new Handler(Looper.myLooper()) {
        @Override
        public void handleMessage(@NonNull Message msg) {
            // 来着客户端的消息先取到消息通知给注册的观察者
            if (null != msg && msg.what == FROM_CLIENT) {
                if (msg.obj instanceof Bundle) {
                    Bundle bundle = (Bundle) msg.obj;
                    String name = bundle.getString("name");
                    MessagerObserverble.getInstance().sendEvent(new MessengerEvent(name, bundle));
                }
                //需要服务端回复消息就回复，否则不管
                if (msg.arg1 == NEED_REPLY) {
                    Message replyMsg = Message.obtain();
                    replyMsg.arg1 = MessagerConstant.MSG_ID_SERVER;
                    Bundle bundle = new Bundle();
                    bundle.putString(MessagerConstant.MSG_CONTENT_SERVER, "receive ok.");
                    replyMsg.setData(bundle);
                    try {
                        msg.replyTo.send(replyMsg);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }


            } else {
                super.handleMessage(msg);
            }

        }
    });

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
    public IBinder onBind(Intent intent) {
        return mMessager.getBinder();
    }
}