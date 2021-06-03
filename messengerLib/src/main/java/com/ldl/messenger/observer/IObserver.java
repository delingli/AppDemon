package com.ldl.messenger.observer;


import com.ldl.messenger.MessengerEvent;

public interface IObserver {
    public void update(MessengerEvent mes);
}
