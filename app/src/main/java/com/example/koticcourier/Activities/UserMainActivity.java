package com.example.koticcourier.Activities;

import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.example.koticcourier.Adapters.OrdersAdapter;
import com.example.koticcourier.CurrentUser.CUser;
import com.example.koticcourier.Database.DatabaseHelper;
import com.example.koticcourier.Models.Order;
import com.example.koticcourier.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class UserMainActivity extends BaseActivity {

    ListView ordersLV;
    OrdersAdapter ordersAdapter;
    FloatingActionButton fab;
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_main);
        initialize();
        fab.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                DatabaseHelper db = new DatabaseHelper(mContext);
                db.setOffline(String.valueOf(CUser.CurrentUser().getUserID()));
                Toast.makeText(mContext,getString(R.string.log_out_warning),Toast.LENGTH_LONG).show();
                new Handler().postDelayed (new Runnable() {
                                               @Override
                                               public void run(){
                                                   finishAffinity();
                                               }
                                           }
                        ,2400 );
            }
        } );

        setSupportActionBar(toolbar);
    }


    public void initialize(){
        ordersLV = findViewById( R.id.userOrdersLV );
        fab = findViewById( R.id.fab);
        toolbar= findViewById( R.id.toolbar2 );
        DatabaseHelper db = new DatabaseHelper(getApplicationContext());
        ArrayList<Order> loadOrders = new ArrayList<>();
        List<Order> orderList =db.getOrders();
        for (Order var: orderList) {
            loadOrders.add(var);
        }

        ordersAdapter = new OrdersAdapter(mContext,loadOrders);
        ordersLV.setAdapter(ordersAdapter);
    }
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_couriers,menu);

        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        Intent idIntent=getIntent();
        String id = idIntent.getStringExtra("id");


        switch (item.getItemId()) {

            case R.id.action_courier_list:
                Intent goIntent1 = new Intent(mContext,UserCouriersActivity.class);
                goIntent1.putExtra ("id",id);
                startActivity(goIntent1);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}