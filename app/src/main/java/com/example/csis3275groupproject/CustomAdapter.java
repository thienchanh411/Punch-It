package com.example.csis3275groupproject;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.example.csis3275groupproject.DB.Employee;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

public class CustomAdapter extends BaseAdapter implements ListAdapter {
    private ArrayList<String> list;
    private final Context context;
    DatabaseReference databaseReference;

    public CustomAdapter(ArrayList<String> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int pos) {
        return list.get(pos);
    }

    @Override
    public long getItemId(int pos) {
        return pos;
        //just return 0 if your list items do not have an Id variable.
    }

    @SuppressLint("InflateParams")
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.single_list_item, null);
        }

        //Handle TextView and display string from your list
        TextView employeeName= view.findViewById(R.id.employeeName);
        employeeName.setText(list.get(position));

        //Handle buttons and add onClickListeners
        TextView editButton= view.findViewById(R.id.btnEdit);
        TextView deleteButton= view.findViewById(R.id.btnDelete);

        editButton.setOnClickListener(v -> {
            //do something

        });

        deleteButton.setOnClickListener(v -> {
//                list.remove
            //do something
            String key = list.get(position);
            databaseReference.child("Employees").child(key).removeValue();
            list.remove(position);
            notifyDataSetChanged();
        });

        return view;
    }
}