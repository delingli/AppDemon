package com.ldl.messenger.observer;


import com.ldl.messenger.MessengerEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * 做个简单观察者用于通知进程收到的消息
 */
public class MessagerObserverble implements IObserverable {
    private List<IObserver> mList = new ArrayList<>();
    private MessengerEvent messengerEvent;
    private static volatile MessagerObserverble mMessagerObserverble;

    public synchronized static MessagerObserverble getInstance() {
        if (mMessagerObserverble == null) {
            mMessagerObserverble = new MessagerObserverble();
        }
        return mMessagerObserverble;
    }


    @Override
    public void registObserver(IObserver observer) {
        synchronized (mList) {
            if (!mList.contains(observer)) {
                mList.add(observer);
            }
        }

    }

    @Override
    public void removeObserver(IObserver observer) {
        synchronized (mList) {
            if (!mList.isEmpty()) {
                mList.remove(observer);
            }
        }

    }

    @Override
    public void notifyObserver() {
        if (null != mList && !mList.isEmpty()) {
            for (IObserver observer : mList) {
                observer.update(messengerEvent);
            }
        }
    }

    @Override
    public void sendEvent(MessengerEvent messengerEvent) {
        this.messengerEvent = messengerEvent;
        notifyObserver();
    }
}
