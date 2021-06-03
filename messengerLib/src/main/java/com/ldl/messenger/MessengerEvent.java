package com.ldl.messenger;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * 发送的消息体
 */
public class MessengerEvent implements Parcelable {
    public String name;
    public Bundle data;

    public MessengerEvent() {
    }

    public String getName() {
        return name;
    }

    public MessengerEvent(String name, Bundle data) {
        this.name = name;
        this.data = data;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Bundle getData() {
        return data;
    }

    public void setData(Bundle data) {
        this.data = data;
    }

    protected MessengerEvent(Parcel in) {
        name = in.readString();
        data = in.readBundle();
    }

    public static final Creator<MessengerEvent> CREATOR = new Creator<MessengerEvent>() {
        @Override
        public MessengerEvent createFromParcel(Parcel in) {
            return new MessengerEvent(in);
        }

        @Override
        public MessengerEvent[] newArray(int size) {
            return new MessengerEvent[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeBundle(data);
    }
}
