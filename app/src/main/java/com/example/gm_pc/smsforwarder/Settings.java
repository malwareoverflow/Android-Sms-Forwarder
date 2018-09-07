package com.example.gm_pc.smsforwarder;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

public class Settings extends AppCompatActivity {


    private SettingsDbHelper mDbHelper;
    private DrawerLayout mDrawerLayout;
    private boolean isenablestatus=false;

    Intent myIntent;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settingsview);
        mDbHelper = new SettingsDbHelper(this);
        setTitle("Settings");


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu);
        initializeDrawerlayout();
        try {

            isenablestatus= mDbHelper.getisEnablereceiverstatus();
//    Toast.makeText(Settings.this,isdeletesmsstatus==false?"delete sms is disabled" , Toast.LENGTH_SHORT).show();
//
//    Toast.makeText(Settings.this, , Toast.LENGTH_SHORT).show();

        }catch (Exception ex){

            Toast.makeText(Settings.this, ex.getMessage().toString(), Toast.LENGTH_SHORT).show();

        }


        //Switch listener

        final Switch onOffSwitch = (Switch)  findViewById(R.id.enabledisablesmsreceiverswitch);
       // final Switch isdeletesendsmsonOffSwitch = (Switch)  findViewById(R.id.isdeletesendsmsswitch);
        onOffSwitch.setChecked(isenablestatus);
        onOffSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    try{
                      //  EnableReciver(Settings.this);
                        boolean isupdated=   mDbHelper.Update("1",true,false);
                        Toast.makeText(Settings.this, isupdated==true?"Enabled receiver":"Failed to update", Toast.LENGTH_SHORT).show();

                    }
                    catch (Exception e){

                        Toast.makeText(Settings.this, e.getMessage().toString(), Toast.LENGTH_LONG).show();
                    }
                }
                else{
                   // DisableReciver(Settings.this);
                    boolean isupdate= mDbHelper.Update("1",false,false);
                    Toast.makeText(Settings.this, isupdate==true?"Disabled reciever":"Disabled failed", Toast.LENGTH_SHORT).show();


                }
                Log.v("Switch State=", ""+isChecked);
            }

        });











    }
//    private void DisableReciver(Context context)
//    {
//        try {
//
//
//    PackageManager pm  = context.getPackageManager();
//    ComponentName componentName = new ComponentName(Settings.this, IncomingSms.class);
//    pm.setComponentEnabledSetting(componentName,PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
//            PackageManager.DONT_KILL_APP);
//
//        }
//        catch (Exception ex){
//
//
//
//        }
//
//    }

//    private void EnableReciver(Context context)
//    {
//        try {
//
//
//            PackageManager pm  = context.getPackageManager();
//
//            ComponentName componentName = new ComponentName(Settings.this, IncomingSms.class);
//            pm.setComponentEnabledSetting(componentName,PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
//                    PackageManager.DONT_KILL_APP);
//        }
//
//        catch (Exception ex){
//
//
//
//        }
//
//    }
    private void initializeDrawerlayout() {
        mDrawerLayout = findViewById(R.id.drawer_layout);

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        // set item as selected to persist highlight
                        menuItem.setChecked(true);
                        // close drawer when item is tapped
                        mDrawerLayout.closeDrawers();

                        int id = menuItem.getItemId();
                        switch (id) {

                            case R.id.nav_home:
                                myIntent = new Intent(Settings.this, MainActivity.class);

                                startActivity(myIntent);

                                break;
                            case R.id.nav_insert:
                                //Do some thing here
                                // add navigation drawer item onclick method here
                                myIntent = new Intent(Settings.this, InsertDb.class);

                                startActivity(myIntent);
                                break;
                            case R.id.nav_view:
                                //Do some thing here
                                // add navigation drawer item onclick method here
                                myIntent = new Intent(Settings.this, ViewDb.class);

                                startActivity(myIntent);


                                break;
                            case R.id.nav_setting:
                                //Do some thing here
                                // add navigation drawer item onclick method here
//                                myIntent = new Intent(Settings.this, Settings.class);
//
//                                startActivity(myIntent);
//                                Toast.makeText(MainActivity.this, "Sorry we are working on it!",
//                                        Toast.LENGTH_LONG).show();

                                break;

                            // Add code here to update the UI based on the item selected
                            // For example, swap UI fragments here


                        }



                        // Add code here to update the UI based on the item selected
                        // For example, swap UI fragments here

                        return true;
                    }
                });

        mDrawerLayout.addDrawerListener(
                new DrawerLayout.DrawerListener() {
                    @Override
                    public void onDrawerSlide(View drawerView, float slideOffset) {
                        // Respond when the drawer's position changes
                    }

                    @Override
                    public void onDrawerOpened(View drawerView) {
                        // Respond when the drawer is opened
                    }

                    @Override
                    public void onDrawerClosed(View drawerView) {
                        // Respond when the drawer is closed
                    }

                    @Override
                    public void onDrawerStateChanged(int newState) {
                        // Respond when the drawer motion state changes
                    }
                }
        );


//        mAdView.setAdListener(new AdListener() {
//            @Override
//            public void onAdLoaded() {
//                // Code to be executed when an ad finishes loading.
//            }
//
//            @Override
//            public void onAdFailedToLoad(int errorCode) {
//                // Code to be executed when an ad request fails.
//            }
//
//            @Override
//            public void onAdOpened() {
//                // Code to be executed when an ad opens an overlay that
//                // covers the screen.
//            }
//
//            @Override
//            public void onAdLeftApplication() {
//                // Code to be executed when the user has left the app.
//            }
//
//            @Override
//            public void onAdClosed() {
//                // Code to be executed when when the user is about to return
//                // to the app after tapping on an ad.
//            }
//        });
    }
}
