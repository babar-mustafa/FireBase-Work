package com.example.babarmustafa.todofirebase;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ListView emailList;
    String key;
    private List<Data> messages;
    private ToDoAdapter listAdapter;
    EditText editText, editText2;
    Button btn;
    CheckBox chk;
    Data dat;
    DatabaseReference database;
    LinearLayout mylayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Write a message to the database
        database = FirebaseDatabase.getInstance().getReference();

        editText = (EditText) findViewById(R.id.edit_text);
        editText2 = (EditText) findViewById(R.id.edit_text2);
        btn = (Button) findViewById(R.id.button_add);
        chk = (CheckBox)findViewById(R.id.check_box1);

        emailList = (ListView) findViewById(R.id.email_List);
        messages = new ArrayList<>();

        listAdapter = new ToDoAdapter(messages, this);
        emailList.setAdapter(listAdapter);
        emailList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, final long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Delete Item!!!!!!!!!");
                builder.setMessage("want to delete some item");
                builder.setNegativeButton("Cancel", null);
                builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        database.child("Data").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                int i = 0;
                                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                    if (i == position) {
                                        DatabaseReference ref = dataSnapshot1.getRef();
                                        ref.removeValue();

                                        Toast.makeText(MainActivity.this, "Deleted", Toast.LENGTH_SHORT).show();
                                        messages.remove(position);
                                        listAdapter.notifyDataSetChanged();
                                    }
                                    i++;
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }

                        });


                    }
                });
                builder.create().show();
                return true;
            }

        });





//        final DatabaseReference myRef = database.getReference("AMessage");
//        final DatabaseReference myRef2 = database.getReference("City");

        // myRef.setValue("Hello Babar");
        // Read from the database

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SetValuetoFireBase();

            }


        });

        database.child("Data").addChildEventListener(new ChildEventListener() {

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                // This method is called once with the initial value and again
                // whenever Data at this location is updated.
                Data data = dataSnapshot.getValue(Data.class);

                Log.v("DATA", "" + data.getName());
                messages.add(new Data(data.getId(), data.getName(), data.getCity(), data.isCheckb()));
                listAdapter.notifyDataSetChanged();



            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }


            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("TAG", "Failed to read value.", error.toException());
            }
        });
    }

    private void SetValuetoFireBase() {
        //myRef.push().setValue();
        String text = editText.getText().toString();
        String city = editText2.getText().toString();
        boolean checked = chk.isChecked();
        //.setvalue send value to database and overide with new onw

        Data data = new Data(database.child("Data").push().getKey().toString(),text,city,checked);

        database.child("Data").child(data.getId()).setValue(data);
//        database.child("Data").push().setValue(city);
//        database.child("Data").push().setValue(text);
//        database.child("Data").push().setValue(checked);


        editText.setText("");
        editText2.setText("");
        chk.setChecked(false);


//        Data email = new Data(text, city, 0, checked);
//        messages.add(email);
//        listAdapter.notifyDataSetChanged();
    }
}

//
//import android.support.v7.app.AppCompatActivity;
//import android.os.Bundle;
//
//public class MainActivity extends AppCompatActivity {
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//    }
//}
