package com.ldl.messenger;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;

import androidx.annotation.NonNull;

public class ClientImpl implements IClient {
    private Messenger mService;
    public String TAG = ClientImpl.this.getClass().getSimpleName();
    private boolean isConnect = false;
    private ServiceConnection mConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder service) {
            //创建一个可以向服务器发送消息的Messenger对象
            mService = new Messenger(service);
            isConnect = true;
            Log.d(TAG, "connected...");
        }

        public void onServiceDisconnected(ComponentName className) {
            isConnect = false;
            mService = null;
            Log.d(TAG, "disconnected...");
        }
    };
    //接收服务器响应
    private Messenger mMessenger = new Messenger(new Handler(Looper.myLooper()) {
        @Override
        public void handleMessage(@NonNull Message serverMsg) {
            super.handleMessage(serverMsg);
            if (serverMsg.arg1 == MessagerConstant.MSG_ID_SERVER) {
                Bundle data = serverMsg.getData();
                if (null != data) {
                    String receiveServer = data.getString(MessagerConstant.MSG_CONTENT_SERVER);
                    Log.d("ClientManager", "##" + receiveServer);
                }

            }
        }
    });

    @Override
    public void bindService(Context context) {
        Intent intent = new Intent(context, MessagerService.class);
        context.bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
        Log.e(TAG, "bindService invoked !");
    }

    @Override
    public void unBindService(Context context) {
        context.unbindService(mConnection);
        Log.e(TAG, "unbindService invoked !");
    }

    @Override
    public void sendMessage(String name, Bundle bundle) {
        if (isConnect && null != bundle) {
            try {
                Message msgFromClient = Message.obtain(null, MessagerService.FROM_CLIENT);
//                Bundle bundle = new Bundle();
//                bundle.putParcelable(MessagerConstant.MSG_CONTENT_CLIENT, event);
//                bundle.putString(MessagerConstant.MSG_CONTENT_CLIENT,event.name);
//                msgFromClient.setData(bundle);
                bundle.putString("name", name);
                msgFromClient.obj = bundle;
                msgFromClient.replyTo = mMessenger;
                //往服务端发送消息
                if (isConnect) {
                    mService.send(msgFromClient);
                }
            } catch (RemoteException e) {
                e.printStackTrace();
            }

        }

    }

    @Override
    public void sendMessage(String name, Bundle bundle, boolean reply) {
        if (isConnect && null != bundle) {
            try {
                Message msgFromClient = Message.obtain(null, MessagerService.FROM_CLIENT);
//                Bundle bundle = new Bundle();
//                bundle.putParcelable(MessagerConstant.MSG_CONTENT_CLIENT, event);
                bundle.putString("name", name);
                msgFromClient.obj = bundle;
                if (reply) {
                    msgFromClient.arg1 = MessagerService.NEED_REPLY;
                }
                msgFromClient.replyTo = mMessenger;
                //往服务端发送消息
                if (isConnect) {
                    mService.send(msgFromClient);
                }
            } catch (RemoteException e) {
                e.printStackTrace();
            }

        }
    }
}
