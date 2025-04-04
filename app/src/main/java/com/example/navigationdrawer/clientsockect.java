package com.example.navigationdrawer;

import android.Manifest;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.pm.PackageManager;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import java.util.UUID;
import android.os.Handler;

public class clientsockect implements Runnable {
    public Boolean invokeSendMSGFragment=null;
    BluetoothSocket socket;
    Handler handler;
    private sendMSG sendThread;
    FragmentActivity x;
    private readMsg recvThread;
    public sendMSG getSendThread(){
        return sendThread;
    }
    public readMsg getRecvThread(){
        return recvThread;
    }

    public clientsockect(BluetoothSocket socket,Handler handler,FragmentActivity x) {
        this.socket = socket;
        this.handler=handler;
        this.x=x;
    }
    public Boolean getmyfield(){
        return invokeSendMSGFragment;
    }

    @Override
    public void run() {
        //try to connect to the server

        try {

            socket.connect();
            invokeSendMSGFragment=Boolean.valueOf(true);
            sendMSG sendmsgthread=new sendMSG(socket);
            sendThread=sendmsgthread;
            new Thread(sendmsgthread).start();

            readMsg readingMsgThread=new readMsg(socket,handler,x);
            recvThread=readingMsgThread;
            new Thread(readingMsgThread).start();


        //connection success

        }catch (Exception e){
            invokeSendMSGFragment=Boolean.valueOf(false);
            e.printStackTrace();
            try{
                socket.close();
            }catch (Exception x){
                x.printStackTrace();
            }
        }



    }

}
