package com.example.navigationdrawer;

import static androidx.core.content.ContextCompat.registerReceiver;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

public class MainActivity extends  AppCompatActivity implements secondfragment.passThisfragment{
    private DrawerLayout mDrawer;
    private BluetoothAdapter adapter;
    private thirdfragment thirdFragment;

    private Toolbar toolbar;
    private NavigationView nvDrawer;
    // Make sure to be using androidx.appcompat.app.ActionBarDrawerToggle version.
    private ActionBarDrawerToggle drawerToggle;
    private BroadcastReceiver serverMSGReceiver=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //receive the message and update the ui
            String message=intent.getStringExtra("message");
            thirdFragment.getMessage(message);

        }
    };
    @Override
    protected void onStart() {

        super.onStart();
        registerReceiver(serverMSGReceiver,new IntentFilter("recvdservermsg"));
    }
    @Override
            protected void onStop() {

        super.onStop();
        unregisterReceiver(serverMSGReceiver);
    }


    DrawerLayout drawerLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        drawerLayout = findViewById(R.id.drawer_layout);
        Toolbar toolbar = findViewById(R.id.tlbar);
        toolbar.showOverflowMenu();
        toolbar.inflateMenu(R.menu.blufeatures);
        // setSupportActionBar(toolbar);
        toolbar.setTitle(" Blue Chart App");
        //toolbar.inflateMenu(R.menu.blufeatures);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if(item.getItemId()==R.id.getdiscovered){
                    //enable discoveribility
                    Intent discoverableIntent = new
                            Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
                    discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 0);
                    startActivity(discoverableIntent);

                }
                if (item.getItemId() == R.id.turnOnbluetooth) {
                    //turn on bluuetooth
                    adapter = BluetoothAdapter.getDefaultAdapter();
               if(adapter!=null){
                    if (ActivityCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        //return TODO;
                    }
                    startActivityForResult(new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE), 1);
               }
               else {
                   Toast.makeText(MainActivity.this, "Bluetooth not supported ", Toast.LENGTH_LONG).show();
               }

            } else if (R.id.turnOffbluetooth==item.getItemId()) {
                    //turn off the bluetooth
                    adapter.disable();
                }
                return true;
            }
        });


        FrameLayout fragmentContainer=findViewById(R.id.flContent);
        NavigationView drawer=findViewById(R.id.nvView);
        ActionBarDrawerToggle toggle=new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.open,R.string.close);
        drawerLayout.setDrawerListener(toggle);

        toggle.syncState();
        FragmentManager fragmentManager=getSupportFragmentManager();
        if(savedInstanceState==null){
            toolbar.setTitle("Paired Devices");
            Fragment home=firstfragment.newInstance("xyz","xnm");
            fragmentManager.beginTransaction().replace(R.id.flContent,home).commit();
            drawer.setCheckedItem(R.id.pairedDevices);
        }
        drawer.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment launchfragment=null;
                int itemselected=item.getItemId();
                if(itemselected==R.id.pairedDevices){
                    //home menu item selected
                    toolbar.setTitle("Paired Devices");
                    launchfragment=firstfragment.newInstance("xyz","mnz");
                    fragmentManager.beginTransaction().replace(R.id.flContent,launchfragment).commit();
                }else if(itemselected==R.id.BluetoothTurnOn){
                    toolbar.setTitle("Connect Device");
                    launchfragment=secondfragment.newInstance("xyz","xyz");

                    fragmentManager.beginTransaction().replace(R.id.flContent,launchfragment).commit();
                }else if(itemselected==R.id.DiscoverDevices){
                    toolbar.setTitle("Discover Devices");
                   launchfragment=fouthfragment.newInstance("xyz","xyz");
                   fragmentManager.beginTransaction().replace(R.id.flContent,launchfragment).commit();
                } else if (itemselected==R.id.logout) {
                    toolbar.setTitle("Disconnect");
                 launchfragment=fifthfragment.newInstance("xyz","xyz");
                 fragmentManager.beginTransaction().replace(R.id.flContent,launchfragment).commit();
                }
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });


    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.blufeatures, menu);

        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }




    @Override
    public void recvThisFragment(thirdfragment chartfragment) {
       thirdFragment=chartfragment;
    }

}
