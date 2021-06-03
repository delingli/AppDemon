package com.ldl.messenger;

import android.content.Context;
import android.os.Bundle;

public interface IClient {
    void bindService(Context context);

    void unBindService(Context context);

    void sendMessage(String name,Bundle messengerEvent);

    void sendMessage(String name,Bundle bundle, boolean reply);
}
