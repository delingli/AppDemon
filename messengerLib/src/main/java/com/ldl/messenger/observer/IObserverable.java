package com.ldl.messenger.observer;


import com.ldl.messenger.MessengerEvent;

public interface IObserverable {
    public void registObserver(IObserver observer);
    public void removeObserver(IObserver observer);
    public void notifyObserver();
    public void sendEvent(MessengerEvent messengerEvent);
}
