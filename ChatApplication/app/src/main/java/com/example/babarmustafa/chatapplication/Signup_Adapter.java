package com.example.babarmustafa.chatapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

/**
 * Created by BabarMustafa on 10/20/2016.
 */

public class Signup_Adapter extends BaseAdapter {

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private Signup_Adapter listapater;
    DatabaseReference database = FirebaseDatabase.getInstance().getReference();

    private ArrayList<User> dataList;
    private Context context;

    public Signup_Adapter(ArrayList<User> dataList, Context context) {
        this.dataList = dataList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public User getItem(int position) {
        return dataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        // final View inflater = LayoutInflater.from(MainView.this).inflate(R.layout.for_messages,null);
        View view = inflater.inflate(R.layout.signle_user_show, null);


        TextView forname = (TextView) view.findViewById(R.id.v_username);

        final User data = dataList.get(position);

        String username = data.getName();

//mAuth.getCurrentUser().toString();

        //to still the condition after changes
        final User todoChekd = (User) getItem(position);
        forname.setText(dataList.get(position).getEmail());

        return view;
    }
}
