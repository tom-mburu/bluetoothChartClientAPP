package com.example.navigationdrawer;

import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import androidx.fragment.app.FragmentActivity;

import java.io.DataInputStream;
import java.io.InputStream;

public class readMsg implements Runnable{
    BluetoothSocket socket;
    FragmentActivity x;
    Handler[] handler={null};

    public readMsg(BluetoothSocket socket,Handler handler,FragmentActivity x) {
        this.socket = socket;
        this.handler[0]=handler;
        this.x=x;
    }


    InputStream instream;
    String[] recvedmsg={null};
    @Override
    public void run() {
        try{
            instream=socket.getInputStream();
            DataInputStream indata=new DataInputStream(instream);
      Thread readMessage= new Thread (){
           public void run(){
            while(true){
                try{
               recvedmsg[0]=indata.readUTF();
                }catch (Exception e){
                    e.printStackTrace();
                }
                if(recvedmsg[0]!=null){
                  handler[0].post(new Runnable() {
                      @Override
                      public void run() {
                         Intent intent=new Intent();
                         intent.setAction("recvdservermsg");
                         intent.putExtra("message",recvedmsg[0]);
                         x.sendBroadcast(intent);
                      }
                  });
                }
            /*int data;
            while(true){
                data=instream.read();
                char chardata = (char)data;
                recvedmsg.append(chardata);

                if(data==-1){
                    //we reached end of the msg
                    break;
                }*/
            }}};

readMessage.start();
try{
   //block parent thread untill this thread finishes
   readMessage.join();
}catch(Exception e){
    e.printStackTrace();
}
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
