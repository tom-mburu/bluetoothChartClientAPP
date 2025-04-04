package com.example.navigationdrawer;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link firstfragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class firstfragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    ListView bondedDiviceslist;
    TextView noDevicemsg;

    public firstfragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment firstfragment.
     */
    // TODO: Rename and change types and number of parameters
    public static firstfragment newInstance(String param1, String param2) {
        firstfragment fragment = new firstfragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
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
        return inflater.inflate(R.layout.fragment_firstfragment, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

         noDevicemsg=view.findViewById(R.id.alertnoDevice);
        bondedDiviceslist=view.findViewById(R.id.bondeddevices);


    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
      ArrayAdapter adapter;
        noDevicemsg.setVisibility(View.INVISIBLE);
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
        String []availableDevices=new String[devices.size()];
        if(devices.size()>0){
            //noDevicemsg.setVisibility(View.INVISIBLE);
            //we have paired devices
            int counter=0;
            for(BluetoothDevice device:devices){
                availableDevices[counter]=device.getName();
                counter++;
            }
            adapter = new ArrayAdapter((Context) this.getActivity(), android.R.layout.simple_list_item_1, availableDevices);
            bondedDiviceslist.setAdapter(adapter);

        }else{
            Toast.makeText(getActivity(), "no paired device", Toast.LENGTH_LONG).show();
            noDevicemsg.setVisibility(View.VISIBLE);

        }

    }
}