package com.example.koticcourier.Activities;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.example.koticcourier.Adapters.CourierOrdersAdapter;
import com.example.koticcourier.CurrentUser.CUser;
import com.example.koticcourier.Database.DatabaseHelper;
import com.example.koticcourier.Models.Order;
import com.example.koticcourier.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;
public class CourierMainActivity extends BaseActivity {

    ExpandableListView orderLV;
    CourierOrdersAdapter courierOrdersAdapter;
    ArrayList<Order> orderList = new ArrayList<Order>();

    FloatingActionButton fab;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_courier_main);
        initialize();

        fab.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseHelper db = new DatabaseHelper(mContext);
                db.setOffline (String.valueOf(CUser.CurrentUser().getUserID()));

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
    }
    public void initialize(){
        orderLV = findViewById(R.id.ordersELV);
        fab = findViewById(R.id.fab);
        DatabaseHelper db = new DatabaseHelper(mContext);
        ArrayList<Order> loadOrders = new ArrayList<>();
        List<Order> orderList =db.getOrders();
        for (Order var: orderList) {
            loadOrders.add(var);
        }
        if (loadOrders.size() > 0){
            courierOrdersAdapter = new CourierOrdersAdapter(loadOrders);
            orderLV.setAdapter(courierOrdersAdapter);
        }else{
            Toast.makeText(mContext,getString(R.string.no_current_order),Toast.LENGTH_LONG).show();
        }
    }



}