package com.example.koticcourier.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.koticcourier.Database.DatabaseHelper;
import com.example.koticcourier.Enums.Status;
import com.example.koticcourier.Models.Order;
import com.example.koticcourier.R;

import java.util.ArrayList;

public class CourierOrdersAdapter extends BaseExpandableListAdapter {

    private ArrayList<Order> mOrders;

    public CourierOrdersAdapter(ArrayList<Order> orders){
        this.mOrders=orders;
    }

    @Override
    public int getGroupCount() {
        return mOrders.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return 1;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return mOrders.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return mOrders.get(groupPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return 0;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(parent.getContext()).inflate (android.R.layout.simple_expandable_list_item_1,parent,false );
        TextView groupName = convertView.findViewById(android.R.id.text1);

        DatabaseHelper db = new DatabaseHelper(parent.getContext());
        groupName.setText (mOrders.get(groupPosition).getStatus().getTitle());

        switch (mOrders.get(groupPosition).getStatus()){
            case Complete:convertView.setBackgroundColor(parent.getContext().getResources().getColor(R.color.complete));
                return convertView;
            case OnProgress:convertView.setBackgroundColor(parent.getContext().getResources().getColor(R.color.onProgress));
                return convertView;
            case OnConfirm:convertView.setBackgroundColor(parent.getContext().getResources().getColor(R.color.onConfirm));
                return convertView;
            case Success:convertView.setBackgroundColor(parent.getContext().getResources().getColor(R.color.success));
                return convertView;
            case Canceled:convertView.setBackgroundColor(parent.getContext().getResources().getColor(R.color.canceled));
                return convertView;
            case Decline:convertView.setBackgroundColor(parent.getContext().getResources().getColor(R.color.declined));
                return convertView;
            default:
                return convertView;
        }

    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

        convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.content_order,parent,false );
        DatabaseHelper db = new DatabaseHelper(parent.getContext());

        TextView orderTV =(TextView) convertView.findViewById(R.id.orderDetailTV );
        TextView cellTV =(TextView) convertView.findViewById(R.id.userCellTV );
        TextView courierOrderDateTV =(TextView) convertView.findViewById(R.id.courierOrderDateTV );
        orderTV.setText(mOrders.get(groupPosition).getOrderDetail());
        cellTV.setText(db.getCellByID(mOrders.get(groupPosition).getUserID()));
        courierOrderDateTV.setText(mOrders.get(groupPosition).getRequestDate());

        Button acceptBTN = convertView.findViewById(R.id.acceptOrderBTN);
        Button declineBTN = convertView.findViewById(R.id.declineOrderBTN);
        Button completeBTN = convertView.findViewById(R.id.completeOrderBTN);
        acceptBTN.setVisibility(View.VISIBLE);
        declineBTN.setVisibility(View.VISIBLE);
        completeBTN.setVisibility(View.VISIBLE);
        switch (mOrders.get(groupPosition).getStatus()){
            case OnConfirm:
                completeBTN.setVisibility(View.GONE);
                break;
            case OnProgress:
                acceptBTN.setVisibility(View.GONE);
                break;
            case Success:
                completeBTN.setText(convertView.getResources().getText(R.string.complete_order));
                acceptBTN.setVisibility(View.GONE);
                declineBTN.setVisibility(View.GONE);
                break;
            case Complete:
            case Decline:
            case Canceled:
                acceptBTN.setVisibility(View.GONE);
                declineBTN.setVisibility(View.GONE);
                completeBTN.setVisibility(View.GONE);
                break;


        }


        acceptBTN.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                refreshOrder(parent.getContext(),groupPosition,Status.OnProgress);
            }});

        declineBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                refreshOrder(parent.getContext(),groupPosition,Status.Decline);
            }});
        completeBTN.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if (mOrders.get(groupPosition).getStatus() == Status.Success){
                    refreshOrder(parent.getContext(),groupPosition,Status.Complete);
                }else{
                    refreshOrder(parent.getContext(),groupPosition,Status.Success);
                }

            }});

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }


    private void refreshOrder(Context context,int position, Status status){
        DatabaseHelper db = new DatabaseHelper(context);
        int row = db.changeStatusOrder(status.getNumericType(),mOrders.get(position).getID());
        if (row <= 0)
                Toast.makeText(context,context.getString(R.string.fail_attempt),Toast.LENGTH_LONG).show ();
        else{
            mOrders.get(position).setStatu(status);
            notifyDataSetChanged();
        }
    }

}
