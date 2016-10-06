package com.example.babarmustafa.userauthentication;


import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class TodoList_Mainactivity extends AppCompatActivity {


    private ListView emailList;
    String key;
    private List<Data> messages;
    private ToDoAdapter listAdapter;
    EditText editText, editText2;
    Button btn;
    CheckBox chk;
    Data dat;
    DatabaseReference database;
    FirebaseUser f_user;
    TextView nameview;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_list__mainactivity);
        // Write a message to the database
        database = FirebaseDatabase.getInstance().getReference();

        mAuth = FirebaseAuth.getInstance();
        final String login_user = mAuth.getCurrentUser().getUid();
        // ...for signout
        mAuth.getCurrentUser().getDisplayName();

        //this method checks
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in

                    Log.d("TAG", "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Intent call = new Intent(TodoList_Mainactivity.this, MainActivity.class);
                    call.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(call);
                    Log.d("TAG", "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };


        editText = (EditText) findViewById(R.id.edit_text);
        editText2 = (EditText) findViewById(R.id.edit_text2);

        btn = (Button) findViewById(R.id.button_add);
        chk = (CheckBox) findViewById(R.id.check_box1);

        nameview = (TextView) findViewById(R.id.to_useer);
        emailList = (ListView) findViewById(R.id.email_List);

        messages = new ArrayList<>();
        nameview.setText(mAuth.getCurrentUser().getDisplayName());
        listAdapter = new ToDoAdapter(messages, this);

        emailList.setAdapter(listAdapter);
        emailList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, final long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(TodoList_Mainactivity.this);
                builder.setTitle("Delete Item!!!!!!!!!");
                builder.setMessage("want to delete some item");
                builder.setNegativeButton("Cancel", null);


                builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {


                        database.child("Data").child(login_user).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                int i = 0;
                                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                    if (i == position) {
                                        DatabaseReference ref = dataSnapshot1.getRef();
                                        ref.removeValue();

                                        Toast.makeText(TodoList_Mainactivity.this, "Deleted", Toast.LENGTH_SHORT).show();
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

        database.child("Data").child(login_user).addChildEventListener(new ChildEventListener() {

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                // This method is called once with the initial value and again
                // whenever Data at this location is updated.
                Data data = dataSnapshot.getValue(Data.class);

                // Log.v("DATA", "" + data.getId() + data.getName() + data.getCity());
                Data email = new Data(data.getUid(), data.getPushId(), data.getName(), data.getCity(), data.isCheckb());
                messages.add(email);
                listAdapter.notifyDataSetChanged();
//

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

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    private void logOut() {
        FirebaseAuth.getInstance().signOut();
    }


    private void SetValuetoFireBase() {
        //myRef.push().setValue();
        String text = editText.getText().toString();
        String city = editText2.getText().toString();
        boolean checked = chk.isChecked();
        //  String uid = f_user.getUid().toString();

        //.setvalue send value to database and overide with new onw

        //getting the push key
        String userKey = mAuth.getCurrentUser().getUid();

        Log.v("TAG", "" + userKey);

        String pushKey = database.child("Data").child(userKey).push().getKey();

//        String key = database.child("Data").push().getKey().toString();
        Data data = new Data(userKey, pushKey, text, city, checked);

        database.child("Data").child(userKey).child(pushKey).setValue(data);


//        database.child("Data").push().setValue(city);
//        database.child("Data").push().setValue(text);
//        database.child("Data").push().setValue(checked);


        editText.setText("");
        editText2.setText("");
        chk.setChecked(false);


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            logOut();
            //Toast.makeText(TodoList_Mainactivity.this, "You Pressed The Setting", Toast.LENGTH_SHORT).show();
        }
        return true;
    }
}
