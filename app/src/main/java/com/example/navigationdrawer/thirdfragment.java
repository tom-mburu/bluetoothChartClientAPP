package com.example.navigationdrawer;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link thirdfragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class thirdfragment extends Fragment  {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private static readMsg receivingthread;
    private static sendMSG sendingThread;
    private String mParam2;
    LinearLayout mainLayout;


    public thirdfragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment thirdfragment.
     */
    // TODO: Rename and change types and number of parameters
    public static thirdfragment newInstance(String param1, String param2,sendMSG SendingThread,readMsg RecvThread) {
        sendingThread = SendingThread;
        receivingthread=RecvThread;
        thirdfragment fragment = new thirdfragment();
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
        return inflater.inflate(R.layout.fragment_thirdfragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
         mainLayout=view.findViewById(R.id.recvmsg);
        ImageButton sendBtn=view.findViewById(R.id.sendBTN);
        EditText message=view.findViewById(R.id.msg);

        //for sending message

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //get message from edittext
                String recvMessage=message.getText().toString();
                if(recvMessage.isEmpty()!=true){
                    //call thread to send message
                    sendingThread.SendingMSG(recvMessage);
                    message.setText("");//clear edit text for easier typing in next input
                    LinearLayout sendLayout=new LinearLayout(getActivity());
                    sendLayout.setOrientation(LinearLayout.VERTICAL);
                    sendLayout.setGravity(Gravity.RIGHT);
                    sendLayout.setPadding(10,20,60,20);
                    TextView tv=new TextView(getActivity());
                    tv.setMinHeight(30);
                    tv.setMinWidth(50);
                    tv.setText(recvMessage);
                    tv.setPadding(10,10,10,10);
                    tv.setBackgroundResource(R.drawable.sendmsgshape);
                    TextView time= new TextView(getActivity());
                    Date date= new Date();
                    SimpleDateFormat format=new SimpleDateFormat("dd/mm/yyyy  hh:mm");
                    String formattedDate=format.format(date);
                    time.setText(formattedDate);
                    time.setTextColor(Color.parseColor("#ff7700"));
                    time.setGravity(Gravity.RIGHT);
                    time.setPadding(5,5,5,5);
                    sendLayout.addView(tv);
                    sendLayout.addView(time);
                    mainLayout.addView(sendLayout);

                }

            }
        });

    }
    public void getMessage(String msg){
        //invoked when mainActivity receives a message from recvmessage thread

        if(msg.isEmpty()!=true){
            //we have received a message,thus add it in the layout
            TextView recv=new TextView(getActivity());
            recv.setText(msg);
            recv.setMinWidth(50);
            recv.setMinHeight(30);
            recv.setPadding(10,10,10,10);
            recv.setBackgroundResource(R.drawable.recevedmageshape);

            LinearLayout recvlayout=new LinearLayout(getActivity());
            recvlayout.setOrientation(LinearLayout.VERTICAL);
            recvlayout.setPadding(10,10,10,10);
            recvlayout.setGravity(Gravity.LEFT);
            TextView time= new TextView(getActivity());
            Date date= new Date();
            SimpleDateFormat format=new SimpleDateFormat("dd/mm/yyyy  hh:mm");
            String formattedDate=format.format(date);
            time.setText(formattedDate);
            time.setTextColor(Color.parseColor("#009000"));
            time.setGravity(Gravity.RIGHT);
            time.setPadding(5,5,5,5);
            recvlayout.addView(recv);
            recvlayout.addView(time);
            mainLayout.addView(recvlayout);
    }


    }
}