package com.example.gm_pc.smsforwarder;



import android.content.Context;
import android.content.Intent;

import android.os.Bundle;

import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import java.util.Calendar;
import android.support.v7.widget.Toolbar;
public class InsertDb extends AppCompatActivity {



    EditText editFor;
    EditText editRec;
    EditText smsLimit;
    EditText updateId;
    Spinner tocountrycode,frmcountrycode;
    boolean isactiveCheck;
    SmsDbHelper mDbHelper ;
    Button inserbtn;
    Boolean isupdate=false;
String tempTonumber;
String tempFromnumber;
 //create a list of items for the spinner.
 private      String[] countrycodes = new String[]{"+92", "+1", "+33","+49","+44","+90","+91","+64",
"+81","+20","+964","+86"

 };

    private DrawerLayout mDrawerLayout;
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
        setContentView(R.layout.activity_insertdb);
        //where i am wanting to create the database and tables
        mDbHelper = new SmsDbHelper(this);
        // open to read and write

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu);


        initializeDrawerlayout();
        getextraParameters();
        SpinnerInitialize();


        updateId = (EditText) findViewById(R.id.updateId);
        updateId.setVisibility(View.GONE);
        inserbtn = findViewById(R.id.inserttablebtn);


        inserbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


try {

    editFor   = (EditText)findViewById(R.id.editForward);
    editRec   = (EditText)findViewById(R.id.editReceive);
    smsLimit   = (EditText)findViewById(R.id.smslimit);
    tocountrycode   = (Spinner)findViewById(R.id.tonumbercountrycode);
    frmcountrycode=(Spinner)findViewById(R.id.fromnumbercountrycode) ;
    isactiveCheck= ((CheckBox) findViewById(R.id.isactivecheckBox)).isChecked();
    EditText updateId = (EditText)findViewById(R.id.updateId);
    boolean success=false;
    if (isupdate){
        String upId= updateId.getText().toString();
        if(!editRec.getText().toString().equals(editFor.getText().toString()))
        {
            //check only duplicate if forward or to or both changes
            if(!tempTonumber.equals(editFor.getText().toString()) || !tempFromnumber.equals(editRec.getText().toString()) )
            {
                if(!mDbHelper.checkifsameexist( tocountrycode.getSelectedItem()+editFor.getText().toString(),frmcountrycode.getSelectedItem()+editRec.getText().toString()))
                {
                    success = mDbHelper.Update(updateId.getText().toString(), frmcountrycode.getSelectedItem()+editRec.getText().toString(),tocountrycode.getSelectedItem()+ editFor.getText().toString(), Calendar.getInstance().getTime().toString(), isactiveCheck, smsLimit.getText().toString());
                }
                else{
                    Toast.makeText(InsertDb.this, "Record already exist", Toast.LENGTH_SHORT).show();

                }


            }

            // no numbers alter case just updating limit or active status dont check
            else{
                success = mDbHelper.Update(updateId.getText().toString(), frmcountrycode.getSelectedItem()+editRec.getText().toString(),tocountrycode.getSelectedItem()+ editFor.getText().toString(), Calendar.getInstance().getTime().toString(), isactiveCheck, smsLimit.getText().toString());


            }

        }
        else{

            Toast.makeText(InsertDb.this, "Forward and Backward must not be same", Toast.LENGTH_SHORT).show();

        }


    }
    else {

        if(!editRec.getText().toString().equals(editFor.getText().toString()))
        {
if(!mDbHelper.checkifsameexist(tocountrycode.getSelectedItem()+editFor.getText().toString(),frmcountrycode.getSelectedItem()+editRec.getText().toString())) {
    success = mDbHelper.Insert(frmcountrycode.getSelectedItem()+editRec.getText().toString(),tocountrycode.getSelectedItem()+ editFor.getText().toString(), Calendar.getInstance().getTime().toString(), isactiveCheck, smsLimit.getText().toString());
}
else{
    Toast.makeText(InsertDb.this, "Record already exist", Toast.LENGTH_SHORT).show();


}

        }
        else{
            Toast.makeText(InsertDb.this, "Forward and Backward must not be same", Toast.LENGTH_SHORT).show();

        }
    }
    Toast.makeText(InsertDb.this, success==true?"Success":"Failure", Toast.LENGTH_SHORT).show();



    //mDbHelper.ReadWhere();
}
catch (Exception e)
{

    Toast.makeText(InsertDb.this, e.getMessage().toString(), Toast.LENGTH_SHORT).show();

}
            }
        });

    }

    private void SpinnerInitialize() {
        //get the spinner from the xml.
        try{
            Spinner dropdownto = findViewById(R.id.tonumbercountrycode);

//create an adapter to describe how the items are displayed, adapters are used in several places in android.
//There are multiple variations of this, but this is the basic variant.
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, countrycodes);
//set the spinners adapter to the previously created one.

            dropdownto.setAdapter(adapter);
            dropdownto.setSelection(0,true);

            Spinner dropdownfrom = findViewById(R.id.fromnumbercountrycode);

//create an adapter to describe how the items are displayed, adapters are used in several places in android.
//There are multiple variations of this, but this is the basic variant.
            // ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, countrycodes);
//set the spinners adapter to the previously created one.

            dropdownfrom.setAdapter(adapter);
            dropdownfrom.setSelection(0,true);
        }
        catch (Exception ex){
            Toast.makeText(InsertDb.this, ex.getMessage().toString(), Toast.LENGTH_SHORT).show();

        }


    }

    private void getextraParameters()
    {

        try {
            // Get input extra user account data from UserAccountListViewActivity activity.
            Intent intent = getIntent();

            if (intent.hasExtra(SmsContract.SmsEntry._ID)) {


                String val = intent.getStringExtra(SmsContract.SmsEntry._ID);

                EditText editText = (EditText) findViewById(R.id.updateId);
                editText.setText(intent.getStringExtra(SmsContract.SmsEntry._ID), TextView.BufferType.EDITABLE);
            }
            if (intent.hasExtra(SmsContract.SmsEntry.COLUMN_NAME_FROMNUMBER)) {


                EditText editText = (EditText) findViewById(R.id.editReceive);
                editText.setText(intent.getStringExtra(SmsContract.SmsEntry.COLUMN_NAME_FROMNUMBER), TextView.BufferType.EDITABLE);
                 tempFromnumber=editText.getText().toString();
            }
            if (intent.hasExtra(SmsContract.SmsEntry.COLUMN_NAME_TONUMBER)) {
                EditText editText = (EditText) findViewById(R.id.editForward);
                editText.setText(intent.getStringExtra(SmsContract.SmsEntry.COLUMN_NAME_TONUMBER), TextView.BufferType.EDITABLE);
tempTonumber=editText.getText().toString();
            }
            if (intent.hasExtra(SmsContract.SmsEntry.COLUMN_NAME_ISACTIVE)) {
                CheckBox checkBox = (CheckBox) findViewById(R.id.isactivecheckBox);

                checkBox.setChecked(Boolean.parseBoolean(intent.getStringExtra(SmsContract.SmsEntry.COLUMN_NAME_ISACTIVE)));
            }
            if (intent.hasExtra(SmsContract.SmsEntry.COLUMN_NAME_SMSLIMIT)) {
                EditText editText = (EditText) findViewById(R.id.smslimit);
                editText.setText(intent.getStringExtra(SmsContract.SmsEntry.COLUMN_NAME_SMSLIMIT), TextView.BufferType.EDITABLE);


            }

            if (intent.hasExtra("ButtonName")) {
                Button btn = (Button) findViewById(R.id.inserttablebtn);
                btn.setText(intent.getStringExtra("ButtonName"), TextView.BufferType.EDITABLE);
                isupdate = true;

            }
            //   private final int userId   = intent.getIntExtra(SmsContract.SmsEntry._ID, -1);

        }
        catch (Exception ex)
        {
            Toast.makeText(InsertDb.this, ex.getMessage().toString(), Toast.LENGTH_SHORT).show();

        }

        // Can not edit existed user name.
    }
    // Start this activity from other class.

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
                                //Do some thing here
                                // add navigation drawer item onclick method here
                                //Do some thing here
                                // add navigation drawer item onclick method here

                                 myIntent = new Intent(InsertDb.this, MainActivity.class);

                                startActivity(myIntent);
                                break;
                            case R.id.nav_insert:
                                //Do some thing here
                                // add navigation drawer item onclick method here

                                break;
                            case R.id.nav_view:

                                myIntent = new Intent(InsertDb.this, ViewDb.class);

                                startActivity(myIntent);
                                break;
                            case R.id.nav_setting:
myIntent = new Intent( InsertDb.this,Settings.class);
startActivity(myIntent);
                                //                                Toast.makeText(InsertDb.this, "Sorry we are working on it!",
//                                        Toast.LENGTH_LONG).show();
                                //Do some thing here
                                // add navigation drawer item onclick method here
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
    }


}
