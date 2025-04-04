package com.example.navigationdrawer;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link secondfragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class secondfragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    clientsockect clientsockectthread;
    BluetoothSocket socket;
    private String mParam2;
    ProgressBar connecting;
    Handler handler;
    ListView pairedDeviceConnect;
    //ProgressBar connecting;
    private passThisfragment mycontext;
    UUID uuid = UUID.fromString("101fdbf9-a927-4fdd-a303-b8fa4ac0d380");


    public secondfragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment secondfragment.
     */
    // TODO: Rename and change types and number of parameters
    public static secondfragment newInstance(String param1, String param2) {
        secondfragment fragment = new secondfragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    public  interface passThisfragment {
        public void recvThisFragment(thirdfragment chartfragment);

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_secondfragment, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
//find views in the layout
        pairedDeviceConnect = view.findViewById(R.id.pairedevicelist);
        connecting=view.findViewById(R.id.connect);
        connecting.setVisibility(View.INVISIBLE);






    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
         handler=new Handler();


        ArrayList<String> pairedDevices = new ArrayList<>();
        ArrayList<String> MacAddr = new ArrayList<>();

        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.

        }
        Set<BluetoothDevice> devices = bluetoothAdapter.getBondedDevices();
        for (BluetoothDevice device : devices) {
            pairedDevices.add(device.getName());
            MacAddr.add(device.getAddress());
        }
        ArrayAdapter adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, pairedDevices);
        pairedDeviceConnect.setAdapter(adapter);
        pairedDeviceConnect.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                connecting.setVisibility(View.VISIBLE);

                //Toast.makeText(getActivity(), "you clicked" + pairedDevices.get(position), Toast.LENGTH_LONG).show();
                BluetoothDevice connectCDevice = bluetoothAdapter.getRemoteDevice(MacAddr.get(position));
                try {
                    if (ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.

                    }
                     socket = connectCDevice.createRfcommSocketToServiceRecord(uuid);
                     clientsockectthread=new clientsockect(socket,handler,getActivity());
                    Thread clientthread=new Thread(clientsockectthread);
                    clientthread.start();

                    Boolean invoke=clientsockectthread.getmyfield();

                    Toast.makeText(getActivity(), "connecting to server\n please wait " +
                            "!", Toast.LENGTH_LONG).show();
                    while(invoke==null){
                        invoke=clientsockectthread.getmyfield();
                        if(invoke!=null){
                            connecting.setVisibility(View.INVISIBLE);
                            break;
                        }
                    }

                    if(invoke){
                        //invoke fragment for receiving msg

                        FragmentTransaction transaction=getActivity().getSupportFragmentManager().beginTransaction();
                        thirdfragment mychartfragment=thirdfragment.newInstance("xyz","xyz", clientsockectthread.getSendThread(),clientsockectthread.getRecvThread());
                        mycontext.recvThisFragment(mychartfragment);
                        transaction.replace(R.id.flContent,mychartfragment);
                        //fire event in the mainativity
                        transaction.commit();
                    }else{
                        //toast on error in connecting to the server socket
                        Toast.makeText(getActivity(),"Error connecting to server",Toast.LENGTH_LONG).show();
                    }


               }catch (Exception e){
                   Toast.makeText(getActivity(),"failed to create socket",Toast.LENGTH_LONG).show();
               }



            }
        });

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if(context instanceof passThisfragment){
            mycontext=(passThisfragment)context;
        }
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    public BluetoothSocket returnSocket(){
        return socket;
    }
}