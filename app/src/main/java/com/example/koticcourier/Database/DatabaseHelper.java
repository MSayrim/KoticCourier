package com.example.koticcourier.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.koticcourier.Components.DateUtils;
import com.example.koticcourier.CurrentUser.CUser;
import com.example.koticcourier.Enums.Status;
import com.example.koticcourier.Models.Order;
import com.example.koticcourier.Models.User;
import com.example.koticcourier.R;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "kcourier.db";
    private static final int DATABASE_VERSION = 1;
    private Context _context;
    //Kullanıcı Tablosu oluşturuldu (Sqlite kodu).
    private static final String TABLE_USERS_CREATE =
            "CREATE TABLE Users (ID INTEGER PRIMARY KEY AUTOINCREMENT, Cell TEXT, Role TEXT , IsOnline INTEGER)";
    //Sipariş Tablosu oluşuturuldu (Sqlite kodu).
    private static final String TABLE_Order_CREATE =
            "CREATE TABLE Orders (ID INTEGER PRIMARY KEY AUTOINCREMENT, OrderDetail TEXT, RequestDate TEXT ,Status INTEGER,UserID INTEGER,CourierID INTEGER, FOREIGN  KEY(UserID) REFERENCES Users(UserID),FOREIGN  KEY(CourierID) REFERENCES Users(UserID)) ";

    public DatabaseHelper(Context context) {
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
        _context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //Tablolar oluşturuldu.
        db.execSQL(TABLE_USERS_CREATE);
        db.execSQL(TABLE_Order_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + "Users");
        db.execSQL("DROP TABLE IF EXISTS " + "Orders");
        onCreate(db);
    }


    //Yeni sipariş oluşturma sipariş içeriği ve seçilen kurye ID si alıyor
    public String createOrder(String orderDetail,int courierID){
        String respond;
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("OrderDetail",orderDetail.trim());
        cv.put("RequestDate", DateUtils.now());
        cv.put("Status",Status.OnConfirm.getNumericType());
        cv.put("CourierID",courierID);
        cv.put("UserID", CUser.CurrentUser().getUserID());

        long result = db.insert("Orders",null,cv);
        if(result>-1){
            Log.i("DatabaseHelper","Order registered");
            respond = _context.getString(R.string.success);
        }else{
            Log.i("DatabaseHelper","Order not registered");
            respond = _context.getString(R.string.fail);
        }

        db.close();
        return respond;
    }


    //Kayıt olma işlemi tıklanan butona göre rol geliyor buraya
    public long register(String cell,String role){

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put("Cell",cell.trim());
        cv.put("Role",role.trim());
        cv.put("IsOnline",1);
        long result ;

        result = db.insert("Users", null,cv);
        if (result > 0) {
            Log.i("DatabaseHelper", "User registered");
        } else {
            Log.i("DatabaseHelper", "User not registered");
        }

        db.close ();

        return result;
    }


    //Kayıt olunmak istenen telefon numarası kontrolü
    public String isExist(String cell){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor rslt = db.rawQuery("SELECT * FROM Users where Cell ="+cell,null);
        rslt.moveToFirst();

        if (rslt.getCount()>0){
            db.close();
            return rslt.getString(rslt.getColumnIndex ("Role"));
        }else {
            db.close();
            return "none";
        }
    }


    //Giriş yapma işlemi burada "CUser" sınıfında singleton olarak kullanıcı bilgileri tutuluyor
    public int login(String cell,String role){

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor rslt = db.rawQuery("SELECT * FROM Users where Cell ="+cell, null);
        int count = rslt.getCount();
        if(count>0) {
            rslt.moveToFirst();
            int temp =rslt.getInt(rslt.getColumnIndex ("ID"));
            String tempRole = rslt.getString(rslt.getColumnIndex("Role"));
            if(tempRole.equals(role)) {
                CUser.CurrentUser().setUserID(temp);
                CUser.CurrentUser().setUserPhone(cell);
                CUser.CurrentUser().setRole(tempRole);
                setOnline(String.valueOf(temp));
                db.close ();
                return 2;
            }else {
                db.close ();
                return 1;
            }

        }
        db.close ();
        return 0;
    }


    //Kullanıcıların görebilmesi için çevrimiçi kuryeleri getiriyor
    public List<User> getOnlineCouriers(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor rslt = db.rawQuery("SELECT * FROM Users where IsOnline = 1 AND Role ='Courier'",null);
        List<User> onlineCouriers = new ArrayList<>();

        rslt.moveToFirst();
        for (int i = 0; i<rslt.getCount();i++){
            User tempUser = new User("Courier",true);
            tempUser.setPhoneNumber(rslt.getString(rslt.getColumnIndex("Cell")));
            tempUser.setID(rslt.getInt(rslt.getColumnIndex("ID")));
            onlineCouriers.add(tempUser);
            rslt.moveToNext();
        }

        db.close ();
        return  onlineCouriers;
    }


    //Giriş yapan kullanıcının rolüne ve idsine göre sipariş listesi getiriyor
    public List<Order> getOrders(){
        List<Order> orders = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor rslt;
        if(CUser.CurrentUser().getRole().equals("Courier")) {
            rslt = db.rawQuery("SELECT * FROM Orders where CourierID ="+ CUser.CurrentUser().getUserID(),null );
        }else {
            rslt = db.rawQuery("SELECT * FROM Orders where UserID ="+ CUser.CurrentUser().getUserID(),null );
        }

        //imleçi başa çekiyor.Defaultta Cursor(imleç) işlemden sonra bir sonraki boş satıra geçiyor.
        rslt.moveToFirst ();
        for (int i = 0; i<rslt.getCount();i++){
            Order tempOrder = new Order(rslt.getString(rslt.getColumnIndex("OrderDetail")),Status.forCode(rslt.getInt(rslt.getColumnIndex("Status"))),rslt.getInt(rslt.getColumnIndex("UserID")),rslt.getInt(rslt.getColumnIndex("CourierID")));
            tempOrder.setID(rslt.getInt(rslt.getColumnIndex("ID")));
            tempOrder.setRequestDate(rslt.getString(rslt.getColumnIndex("RequestDate")));
            orders.add(tempOrder);
            rslt.moveToNext();
        }
        db.close ();
        return orders;
    }

    //id ye göre telefon numarası getiriyor. siparişlerde sadece userID ve courierID tutulduğu için User tablosundan telefon numaralarını getiriyor
    public String getCellByID(int id){
        SQLiteDatabase db = this.getWritableDatabase ();
        Cursor rslt = db.rawQuery ( "SELECT * FROM Users where ID ="+id, null);
        rslt.moveToFirst ();
        db.close ();
        return rslt.getString ( rslt.getColumnIndex ( "Cell" ) );
    }
    //Sipariş durumunu güncelliyor. Tabloda Enum tutulmadığı için enumun sayısal değerini alıyor.
    public int changeStatusOrder(int status,int id){
        SQLiteDatabase db = this.getWritableDatabase ();
        ContentValues cv = new ContentValues();
        cv.put("Status",status);
        String temp = String.valueOf(id);
        int row = db.update("Orders",cv,"ID = ?",new String[]{temp});
        db.close ();
        return row;
    }


    //login işleminde kullanıcıları online duruma getiriyor
    public void setOnline(String id){
        SQLiteDatabase db = this.getWritableDatabase ();
        Cursor rslt = db.rawQuery("SELECT * FROM Users where ID ="+id, null);
        rslt.moveToFirst ();
        int isOnline = rslt.getInt ( rslt.getColumnIndex ( "IsOnline" ) );

        ContentValues cv = new ContentValues();
        cv.put("IsOnline",1);
        db.update ( "Users",cv,"ID = ?",new String[]{id});
        db.close ();
    }
    //login işleminde kullanıcıları offline duruma getiriyor
    public void setOffline(String id){
        SQLiteDatabase db = this.getWritableDatabase ();
        Cursor rslt = db.rawQuery("SELECT * FROM Users where ID ="+id, null);
        rslt.moveToFirst ();
        int isOnline = rslt.getInt ( rslt.getColumnIndex ( "IsOnline" ) );

        ContentValues cv = new ContentValues();
        cv.put("IsOnline",0);
        db.update ( "Users",cv,"ID = ?",new String[]{id});
        db.close ();
    }


    //Uygulama kurulunca kurye testinin direk apk üstünden yapılabilmesi için kullanıcı ve kurye ekliyor.
    public void createDummyDatas(){
        if(!isExist("5554443322").equals("none")){
            return;
        }
        register("5554443322","User");
        register("5554443355","Courier");


    }

}


