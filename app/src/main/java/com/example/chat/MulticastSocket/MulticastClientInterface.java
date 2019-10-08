package com.example.chat.MulticastSocket;

import com.example.chat.MemberData;

public interface MulticastClientInterface {
    public void MessageHasBeenReceived(String message, String userName, String userColor);
    public void ErrorFromSocketManager(Exception error);
}
