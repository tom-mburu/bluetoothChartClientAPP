package com.example.navigationdrawer;

import android.bluetooth.BluetoothSocket;

import java.io.DataOutputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class sendMSG implements Runnable{
    BluetoothSocket socket;
    OutputStream stream;
    DataOutputStream outdata;
   public String[] message={null};
    public void SendingMSG(String msg){
      this.message[0]=msg;
    }

    public sendMSG(BluetoothSocket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {

        try{
            stream=socket.getOutputStream();
             outdata=new DataOutputStream(stream);

        }catch (Exception e){
            e.printStackTrace();
        }
      Thread handlesending=  new Thread (){
            public void run(){
                while(true){

                   if(message[0]!=null){
                        try{
                            outdata.writeUTF(message[0]);
                            //stream.write(msg.getBytes(StandardCharsets.UTF_8));
                            message[0]=null;
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                }
                }
            }
        };
        handlesending.start();
        try{
            //block main sendMSG thread untill this child thread finishes(this child thread will never quit)

        handlesending.join();
        }catch(Exception e){
            e.printStackTrace();
        }

    }
}
