package com.example.koticcourier.Activities;

import androidx.appcompat.widget.Toolbar;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.koticcourier.Adapters.CouriersAdapter;
import com.example.koticcourier.CurrentUser.CUser;
import com.example.koticcourier.Database.DatabaseHelper;
import com.example.koticcourier.Models.User;
import com.example.koticcourier.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class UserCouriersActivity extends BaseActivity {

    ListView courierLV;
    CouriersAdapter couriersAdapter;
    List<User> onlineCouriers;
    FloatingActionButton fab;
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_user_carriers);
        initialize();
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseHelper db = new DatabaseHelper (mContext);
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
        DatabaseHelper db = new DatabaseHelper(getApplicationContext());
        onlineCouriers = db.getOnlineCouriers();
        fab = findViewById(R.id.fab);
        toolbar= findViewById(R.id.toolbar1);
        courierLV = findViewById(R.id.couriersLV);
        couriersAdapter = new CouriersAdapter(mContext, onlineCouriers);
        courierLV.setAdapter(couriersAdapter);
        courierLV.setOnItemClickListener( new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                final AlertDialog dialogBuilder1 = new AlertDialog.Builder(UserCouriersActivity.this).create();
                final LayoutInflater inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                final View dialogView = inflater.inflate(R.layout.dialog_create_order,null);
                dialogBuilder1.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

                Button sendOrder = dialogView.findViewById(R.id.btnSendOrder);
                Button dissmissOrder = dialogView.findViewById(R.id.btnDissmisOrder);

                TextView currentText = dialogView.findViewById(R.id.orderET);


                sendOrder.setOnClickListener ( new View.OnClickListener () {
                    @Override
                    public void onClick(View v) {
                        DatabaseHelper db = new DatabaseHelper(getApplicationContext());
                        db.createOrder(currentText.getText().toString(),onlineCouriers.get(position).getID());
                        dialogBuilder1.dismiss();
                    }
                } );
                dissmissOrder.setOnClickListener(new View.OnClickListener (){
                    @Override
                    public void onClick(View v) {
                        dialogBuilder1.dismiss ();
                    }
                } );
                dialogBuilder1.setView(dialogView);
                dialogBuilder1.show();
                Toast.makeText(mContext,onlineCouriers.get(position).getPhoneNumber(),Toast.LENGTH_LONG).show ();
            }
        } );

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_orders,menu);

        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        Intent idIntent=getIntent();
        String id = idIntent.getStringExtra("id");

        switch (item.getItemId ()) {
            case R.id.action_orders_list:
                Intent goIntent = new Intent(mContext,UserMainActivity.class);
                goIntent.putExtra("id",id);
                startActivity (goIntent);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}