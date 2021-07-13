package com.example.koticcourier.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.koticcourier.Components.Utils;
import com.example.koticcourier.Database.DatabaseHelper;
import com.example.koticcourier.R;
public class LoginActivity extends BaseActivity {

    private Button courierLoginBTN;
    private Button userLoginBTN;
    private EditText cellNumberET;
    private Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        DatabaseHelper db = new DatabaseHelper(mContext);
        db.createDummyDatas();
        initialize();
    }

    public void doLogin(String type){
        String phoneStr = cellNumberET.getText().toString();

        if (phoneStr.isEmpty() || !Utils.isValidPhone(phoneStr)) {
            Toast.makeText(mContext,getString ( R.string.cell_style_hint ),Toast.LENGTH_LONG).show();
            return;
        }

        DatabaseHelper db = new DatabaseHelper(mContext);
        String result = db.isExist(phoneStr);

        if(result.equals(type)){
            db.login(phoneStr,type);
            move(result);

        }else if(result.equals("none")){
            long registeredId = db.register(phoneStr,type);
            Toast.makeText(mContext,getString(R.string.registered_success),Toast.LENGTH_SHORT ).show ();

            if(registeredId > 0){
                db.login(phoneStr,type);
                move(type);
            }
            else{
                move(result);
            }

        }else {
            Toast.makeText(mContext,getString(R.string.wrong_type),Toast.LENGTH_LONG).show();
        }

        db.close();
    }
    public void initialize(){
        courierLoginBTN = (Button)findViewById( R.id.courierLoginBTN );
        userLoginBTN = (Button) findViewById( R.id.userLoginBTN );
        cellNumberET = (EditText) findViewById( R.id.cellLoginET );
        courierLoginBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doLogin("Courier");
            }
        });
        userLoginBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doLogin("User");
            }
        });
    }

    public void move(String role){
        if(role.equals("Courier")){
            intent = new Intent(mContext,CourierMainActivity.class);
        }else if(role.equals("User")) {
            intent = new Intent(mContext, UserMainActivity.class);
        }

        if (intent != null){
            startActivity(intent);
            finish();
        }else{
            Toast.makeText(mContext,getString(R.string.fail),Toast.LENGTH_LONG).show();
        }
    }
}