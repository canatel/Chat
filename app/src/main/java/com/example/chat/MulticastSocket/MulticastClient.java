package com.example.chat.MulticastSocket;

import android.os.AsyncTask;

import com.example.chat.MemberData;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.SocketException;
import java.net.UnknownHostException;

public class MulticastClient extends Thread {

    private DatagramSocket datagramSocket;
    private InetAddress group;
    private byte[] buf = new byte[5124];
    private int portNumber;
    private String ipAddress;
    private boolean receivingMessages = true;
    private MulticastClientInterface caller;
    private MemberData data;
    private MulticastSocket socket;


    public MulticastClient(int portNumber, String ipAddress, MulticastClientInterface caller, MemberData data) {
        this.portNumber = portNumber;
        this.ipAddress = ipAddress;
        this.caller = caller;
        this.data = data;
        this.start();
    }

    private boolean initializeClient() {
        try {
            datagramSocket = new DatagramSocket();
            group = InetAddress.getByName(ipAddress);
            socket = new MulticastSocket(portNumber);
            socket.joinGroup(group);
            return true;
        } catch (SocketException e) {
            caller.ErrorFromSocketManager(e);
        } catch (UnknownHostException e) {
            caller.ErrorFromSocketManager(e);
        } catch (IOException e) {
            caller.ErrorFromSocketManager(e);
        }
        return false;
    }

    public void sendMessage(final String message, final String userName, final String userColor) {
        try {
            new AsyncTask<Void, Void, Void>() {

                @Override
                protected Void doInBackground(Void... voids) {
                    try {
                        buf = message.getBytes();
                        DatagramPacket mensaje
                                = new DatagramPacket(buf, buf.length, group, portNumber);
                        datagramSocket.send(mensaje);
                        buf = userName.getBytes();
                        DatagramPacket userName
                                = new DatagramPacket(buf, buf.length, group, portNumber);
                        datagramSocket.send(userName);
                        buf = userColor.getBytes();
                        DatagramPacket userColor
                                = new DatagramPacket(buf, buf.length, group, portNumber);
                        datagramSocket.send(userColor);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return null;
                }
            }.execute();
        } catch (Exception error) {
            caller.ErrorFromSocketManager(error);
        }
    }

    @Override
    public void run() {
        initializeClient();
       /* if (initializeClient()) {
            while (receivingMessages) {
                if (socket != null) {
                    try {
                        int cont = 0;
                        DatagramPacket message = new DatagramPacket(buf, buf.length);
                        socket.receive(message);
                        String messageReceived = new String(
                                message.getData(), 0, message.getLength());
                        cont++;
                        DatagramPacket userName = new DatagramPacket(buf, buf.length);
                        socket.receive(userName);
                        String userNameReceived = new String(
                                userName.getData(), 0, userName.getLength());
                        cont++;
                        DatagramPacket userColor = new DatagramPacket(buf, buf.length);
                        socket.receive(userColor);
                        String userColorReceived = new String(
                                userColor.getData(), 0, userColor.getLength());
                        cont++;
                        if (cont == 3) {
                            caller.MessageHasBeenReceived(messageReceived, userNameReceived, userColorReceived);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (NullPointerException e) {
                    }
                }
            }
        }*/
    }
}
