package com.example.gm_pc.smsforwarder;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteCursor;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class ViewDb  extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;

    private ListView userAccountListView = null;

    private TextView userAccountListEmptyTextView = null;

    private SimpleCursorAdapter listViewDataAdapter = null;

    // Store user checked account DTO.
    private List<SmsDTO> userCheckedItemList = new ArrayList<SmsDTO>();
    SmsDbHelper mDbHelper;
    Intent myIntent;
    private  Integer selectedId=0;
    private final String fromColumnArr[] = {SmsContract.SmsEntry._ID, SmsContract.SmsEntry.COLUMN_NAME_TONUMBER, SmsContract.SmsEntry.COLUMN_NAME_FROMNUMBER, SmsContract.SmsEntry.COLUMN_NAME_CREATED_ON,SmsContract.SmsEntry.COLUMN_NAME_ISACTIVE,SmsContract.SmsEntry.COLUMN_NAME_SMSLIMIT};
    private final int toViewIdArr[] = {R.id.sms_list_item_id, R.id.sms_list_item_tonumber, R.id.sms_list_item_fromnumber, R.id.sms_list_item_datetime,R.id.sms_list_item_isactive,R.id.sms_list_item_smslimit};
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_smstable_list_view);

        setTitle("View");

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu);


        initializeDrawerlayout();

        //LIST VIEW START

        userAccountListView = (ListView) findViewById(R.id.sms_table_list);
        userAccountListEmptyTextView = (TextView) findViewById(R.id.user_account_list_empty_text_view);
        userAccountListView.setEmptyView(userAccountListEmptyTextView);

        // Get SQLite database query cursor.
        mDbHelper = new SmsDbHelper(this);
        Cursor cursor = mDbHelper.getAllAccountCursor();

// Create a new SimpleCursorAdapter.
        listViewDataAdapter = new SimpleCursorAdapter(this, R.layout.activity_smstable_list_view_item, cursor, fromColumnArr, toViewIdArr, CursorAdapter.FLAG_AUTO_REQUERY);
        //listViewDataAdapter.notifyDataSetChanged();

        // Set simple cursor adapter to list view.
        userAccountListView.setAdapter(listViewDataAdapter);


        // When list view item is clicked.
        userAccountListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int index, long viewId) {
                // Get list view item SQLiteCursor object.
                Object clickItemObject = adapterView.getAdapter().getItem(index);
                SQLiteCursor cursor = (SQLiteCursor) clickItemObject;

                // Get row column data.
                int rowId = cursor.getInt(cursor.getColumnIndex(SmsContract.SmsEntry._ID));
                String createdon = cursor.getString(cursor.getColumnIndex(SmsContract.SmsEntry.COLUMN_NAME_CREATED_ON));
                String fromnumber = cursor.getString(cursor.getColumnIndex(SmsContract.SmsEntry.COLUMN_NAME_FROMNUMBER));
                String isactive = cursor.getString(cursor.getColumnIndex(SmsContract.SmsEntry.COLUMN_NAME_ISACTIVE));
                String smslimit = cursor.getString(cursor.getColumnIndex(SmsContract.SmsEntry.COLUMN_NAME_SMSLIMIT));
                String tonumber = cursor.getString(cursor.getColumnIndex(SmsContract.SmsEntry.COLUMN_NAME_TONUMBER));
                // Create a UserAccountDTO object to save row column data.
                SmsDTO smsDto = new SmsDTO();
                smsDto.setId(rowId);
                smsDto.setCREATED_ON(createdon);
                smsDto.setFROMNUMBER(fromnumber);
                smsDto.setISACTIVE(Boolean.parseBoolean( isactive));
                smsDto.setSMSLIMIT(smslimit);
                smsDto.setTONUMBER(tonumber);

                // Get checkbox object.
                CheckBox itemCheckbox = (CheckBox) view.findViewById(R.id.user_account_list_item_checkbox);
                boolean checkboxChecked = false;
                if (itemCheckbox.isChecked()) {
                    itemCheckbox.setChecked(false);
                    checkboxChecked = false;
                } else {
                    itemCheckbox.setChecked(true);
                    checkboxChecked = true;
                }

                // Add ( or remove ) userAccountDto from the instance variable userCheckedItemList.
                addCheckListItem(smsDto, checkboxChecked);

                // Show user select list view item id.


                //prevent more than 1 id selection
                if (userCheckedItemList != null) {
                    int size = userCheckedItemList.size();
                    //if more than 1 itemselected
                if(size>1){
                    Toast.makeText(getApplicationContext(), "Please select one at a time ", Toast.LENGTH_SHORT).show();
return;
                }
                if(size==0){
                    return;
                }
                }

                    Toast.makeText(getApplicationContext(), "Select id : " + getUserCheckedItemIds(), Toast.LENGTH_SHORT).show();
                selectedId=Integer.parseInt(getUserCheckedItemIds());
            }
        });
    }

    private String getUserCheckedItemIds() {
        StringBuffer retBuf = new StringBuffer();

        if (userCheckedItemList != null) {
            int size = userCheckedItemList.size();
            //if more than 1 itemselected

            for (int i = 0; i < size; i++) {
                SmsDTO tmpDto = userCheckedItemList.get(i);
                retBuf.append(tmpDto.getId());
                retBuf.append(" ");
            }
        }

        return retBuf.toString().trim();
    }

    /*
     *  Add user checked account dto to userCheckedItemList variable.
     *  userAccountDto : User selected account dto.
     *  add : true - add, false - remove.
     * */
    private void addCheckListItem(SmsDTO smsDTO, boolean add) {
        if (userCheckedItemList != null) {
            boolean accountExist = false;
            int existPosition = -1;

            // Loop to check whether the user account dto exist or not.
            int size = userCheckedItemList.size();
            for (int i = 0; i < size; i++) {
                SmsDTO tmpDto = userCheckedItemList.get(i);
                if (tmpDto.getId() == smsDTO.getId()) {
                    accountExist = true;
                    existPosition = i;
                    break;
                }
            }

            if (add) {
                // If not exist then add it.
                if (!accountExist) {
                    userCheckedItemList.add(smsDTO);
                }
            } else {
                // If exist then remove it.
                if (accountExist) {
                    if (existPosition != -1) {
                        userCheckedItemList.remove(existPosition);
                    }
                }
            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the action bar menu.
        MenuInflater menuInflater = getMenuInflater();
//        menuInflater.inflate(R.menu.action_bar_add_edit_delete_example, menu);
        menuInflater.inflate(R.menu.action_bar_add_edit_delete_example, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();


        if (itemId == android.R.id.home) {
            mDrawerLayout.openDrawer(GravityCompat.START);
            return true;
       }

        if (itemId == R.id.menu_edit) {
            if (userCheckedItemList != null) {
                int size = userCheckedItemList.size();
                if (size != 1) {
                    Toast.makeText(this, "Please select one row to edit.", Toast.LENGTH_SHORT).show();
                } else {
                    SmsDTO tmpDto = userCheckedItemList.get(0);
                    startActivity(getApplicationContext(), selectedId, tmpDto.getFROMNUMBER(), tmpDto.getTONUMBER(), tmpDto.getIACTIVE(),tmpDto.getSMSLIMIT());
                }
            }
        } else if (itemId == R.id.menu_delete) {
            if (userCheckedItemList != null) {
                int size = userCheckedItemList.size();

                if (size == 0) {
                    Toast.makeText(this, "Please select at least one row to delete.", Toast.LENGTH_SHORT).show();
                } else {
                    for (int i = 0; i < size; i++) {
                        SmsDTO tmpDto = userCheckedItemList.get(i);
                        mDbHelper.deleteAccount(tmpDto.getId());

                        userCheckedItemList.remove(i);

                        size = userCheckedItemList.size();
                        i--;
                    }

                    // Reload user account data from SQLite database.
                    Cursor cursor = mDbHelper.getAllAccountCursor();
                    listViewDataAdapter = new SimpleCursorAdapter(this, R.layout.activity_smstable_list_view_item, cursor, fromColumnArr, toViewIdArr, CursorAdapter.FLAG_AUTO_REQUERY);
                    //listViewDataAdapter.notifyDataSetChanged();
                    // Set new data adapter to lise view.
                    userAccountListView.setAdapter(listViewDataAdapter);
                }
            }
        }
        return super.onOptionsItemSelected(item);
    }



    //LIST VIEW END


    //where i am wanting to create the database and tables

    // open to read and write



    public static void startActivity(Context ctx, int id, String from, String to, boolean isactive,String smslimit)
    {
try {

        Intent intent = new Intent(ctx,InsertDb.class);
         intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra(SmsContract.SmsEntry._ID, String.valueOf(id));
        intent.putExtra(SmsContract.SmsEntry.COLUMN_NAME_FROMNUMBER, from);
        intent.putExtra(SmsContract.SmsEntry.COLUMN_NAME_TONUMBER, to);
        intent.putExtra(SmsContract.SmsEntry.COLUMN_NAME_ISACTIVE, isactive);
        intent.putExtra(SmsContract.SmsEntry.COLUMN_NAME_SMSLIMIT, smslimit);
        intent.putExtra("ButtonName", "Update");
        ctx.startActivity(intent);
}
catch (Exception ex){

    Toast.makeText(ctx, ex.getMessage().toString(), Toast.LENGTH_SHORT).show();
}
    }
    public void initializeDrawerlayout() {
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

                                myIntent = new Intent(ViewDb.this, MainActivity.class);

                                startActivity(myIntent);
                                break;
                            case R.id.nav_insert:
                                //Do some thing here
                                // add navigation drawer item onclick method here
                                myIntent = new Intent(ViewDb.this, InsertDb.class);

                                startActivity(myIntent);
                                break;
                            case R.id.nav_view:


                                break;
                            case R.id.nav_setting:
myIntent = new Intent(ViewDb.this,Settings.class);
startActivity(myIntent);
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




