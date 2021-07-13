package com.example.koticcourier.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.koticcourier.Database.DatabaseHelper;
import com.example.koticcourier.Enums.Status;
import com.example.koticcourier.Models.Order;
import com.example.koticcourier.R;

import java.util.List;

public class OrdersAdapter extends BaseAdapter {

    private List<Order> mOrders;
    private Context mContext;

    public OrdersAdapter(Context context,List<Order> orders){
        mOrders = orders;
        mContext = context;
    }

    @Override
    public int getCount() {
        return mOrders.size ();
    }

    @Override
    public Object getItem(int position) {
        return mOrders.get ( position );
    }

    @Override
    public long getItemId(int position) {
        return mOrders.get ( position ).getID ();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from ( mContext ).inflate ( R.layout.content_user_order,parent,false );

        TextView courierCell = convertView.findViewById( R.id.orderCourierCellTV);
        TextView orderDetail = convertView.findViewById( R.id.orderListDetailTv);
        TextView orderDate = convertView.findViewById( R.id.orderDateTV);
        TextView orderStatus = convertView.findViewById( R.id.orderStatusTV);
        Button cancelBTN = convertView.findViewById( R.id.orderCancelBTN );
        DatabaseHelper db = new DatabaseHelper ( mContext );

        courierCell.setText (db.getCellByID(mOrders.get (position).getCourierID() ) );
        orderDetail.setText ( mOrders.get(position).getOrderDetail() );
        orderStatus.setText ( mOrders.get(position).getStatus().getTitle() );
        orderDate.setText ( mOrders.get(position).getRequestDate() );

        if(mOrders.get ( position ).getStatus ()==Status.OnConfirm){
            cancelBTN.setVisibility(View.VISIBLE);
        }else {
            cancelBTN.setVisibility(View.GONE);
        }

        cancelBTN.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                int row = db.changeStatusOrder(Status.Canceled.getNumericType(),mOrders.get(position).getID());
                if (row <= 0)
                    Toast.makeText(mContext,mContext.getString(R.string.fail_attempt),Toast.LENGTH_LONG).show();
                else{
                    mOrders.get(position).setStatu(Status.Canceled);
                    notifyDataSetChanged();
                }
            }
        } );
        switch (mOrders.get(position).getStatus()){
            case Complete:convertView.setBackgroundColor( mContext.getResources ().getColor ( R.color.complete ) );
                return convertView;
            case OnProgress:convertView.setBackgroundColor( mContext.getResources ().getColor ( R.color.onProgress ) );
                return convertView;
            case OnConfirm:convertView.setBackgroundColor( mContext.getResources ().getColor ( R.color.onConfirm ) );
                return convertView;
            case Success:convertView.setBackgroundColor( mContext.getResources ().getColor ( R.color.success ) );
                return convertView;
            case Canceled:convertView.setBackgroundColor( mContext.getResources ().getColor ( R.color.canceled ) );
                return convertView;
            case Decline:convertView.setBackgroundColor( mContext.getResources ().getColor ( R.color.declined ) );
                return convertView;
            default:
                return convertView;
        }

    }
}
