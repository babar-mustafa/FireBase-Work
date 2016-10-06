package com.example.babarmustafa.userauthentication;

/**
 * Created by Babar Mustafa on 9/30/2016.
 */

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

/**
 * Created by imran on 9/26/2016.
 */

public class ToDoAdapter extends BaseAdapter {


    private ToDoAdapter listAdapter;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    ;
    DatabaseReference database = FirebaseDatabase.getInstance().getReference();

    private List<Data> dataList;
    private Context context;

    public ToDoAdapter(List<Data> dataList, Context context) {
        this.dataList = dataList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public Object getItem(int position) {
        return dataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.singleitem, null);

        TextView foename = (TextView) view.findViewById(R.id.nameview);
        TextView foecity = (TextView) view.findViewById(R.id.cityview);
        final CheckBox focheck = (CheckBox) view.findViewById(R.id.checkview);
        focheck.setChecked(false);

        final Data data = dataList.get(position);

        String nam = data.getName();
        String cit = data.getCity();
        boolean ch = data.isCheckb();


        //to still the condition after changes
        final Data todoChekd = (Data) getItem(position);
        focheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, final boolean itemcheck) {
                database.child("Data").child(data.getUid()).child(data.getPushId()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        if (focheck.isChecked()) {

                            database.child("Data").child(data.getUid()).child(data.getPushId()).child("checkb").setValue(true);
                            todoChekd.setCheckb(true);
                            // focheck.setChecked(true);
                            // listAdapter.notifyDataSetChanged();
                        } else {

                            database.child("Data").child(data.getUid()).child(data.getPushId()).child("checkb").setValue(false);
                            todoChekd.setCheckb(false);
                            // focheck.setChecked(false);
                            //listAdapter.notifyDataSetChanged();

                        }
                        //refresh the adapter

                        // notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }
        });


        foename.setText(nam);
        foecity.setText(cit);
        focheck.setChecked(ch);
        return view;
    }
}

