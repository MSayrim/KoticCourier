package com.example.koticcourier.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.koticcourier.Models.User;
import com.example.koticcourier.R;

import java.util.List;

public class CouriersAdapter extends BaseAdapter {

    private List<User> _couriers;
    private Context _context;

    public CouriersAdapter(Context context,List<User> couriers){
        _couriers = couriers;
        _context = context;
    }

    @Override
    public int getCount() {
        return _couriers.size ();
    }

    @Override
    public Object getItem(int position) {
        return _couriers.get ( position );
    }

    @Override
    public long getItemId(int position) {
        return _couriers.get ( position ).getID ();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from ( _context ).inflate ( R.layout.content_courier,parent,false );
        TextView courierCell = convertView.findViewById( R.id.courierCellTV );
        courierCell.setText ( _couriers.get ( position ).getPhoneNumber () );

        return convertView;
    }
}
