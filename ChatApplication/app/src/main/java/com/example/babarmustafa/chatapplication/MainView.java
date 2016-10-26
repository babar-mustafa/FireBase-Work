package com.example.babarmustafa.chatapplication;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
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
import java.util.HashMap;

public class MainView extends AppCompatActivity {
    private ListView emailList;
    private ArrayList<User> messages;
    private Signup_Adapter listAdapter;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private User uSer;
    ArrayList<String> list ;
    User data;
    DatabaseReference database;
    String pId;
    private Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_view);

        Intent t = new Intent(MainView.this,PushService.class);
        startService(t);
list=new ArrayList<>();
        mAuth = FirebaseAuth.getInstance();
        final String login_user = mAuth.getCurrentUser().getUid();

        database = FirebaseDatabase.getInstance().getReference();
        // ...for signout
        mAuth.getCurrentUser().getDisplayName();

        emailList = (ListView) findViewById(R.id.list_view);
        messages = new ArrayList<>();
        listAdapter = new Signup_Adapter(messages,this);
        emailList.setAdapter(listAdapter);

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {

                    // User is signed in

                    Log.d("TAG", "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Intent call = new Intent(MainView.this, MainActivity.class);
                    call.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(call);
                    Log.d("TAG", "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };
        emailList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, final View view, int position, long id) {
                pId =list.get(position);
                AlertDialog.Builder builder = new AlertDialog.Builder(MainView.this);
                final View vieww = LayoutInflater.from(MainView.this).inflate(R.layout.for_messages, null);
                final EditText mytext = (EditText) vieww.findViewById(R.id.formessages);

                builder.setTitle("Notifications window");
                builder.setMessage("Write the Meeage...?");
                builder.setPositiveButton("Send", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(MainView.this, "Test tost", Toast.LENGTH_SHORT).show();



                        String UUID =  mAuth.getCurrentUser().getUid();
                        Log.d("v", "" + UUID);


                       // EditText mytext = (EditText) view.findViewById(R.id.formessages);

                        Log.d("v", "" + mytext.getText().toString());

                        NotificationMessage notificationMessage = new NotificationMessage(mytext.getText().toString(), database.child("Notifications").child(UUID).push().getKey().toString(), UUID);

/*
                        HashMap<String, String> result = new HashMap<>();
                        result.put("uid", UUID);
                        result.put("pushId", database.child("Notifications").child(UUID).push().getKey());
                        result.put("message", mytext.getText().toString());*/

                        database.child("Notifications").child(pId).child(notificationMessage.getPushId()).setValue(notificationMessage);

                    }
                });





                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(MainView.this, "cancel", Toast.LENGTH_SHORT).show();
                    }
                });

                builder.setView(vieww);
                builder.create().show();
                return true;
            }
        });

        FirebaseDatabase.getInstance().getReference().child("User Info").addChildEventListener(new ChildEventListener() {

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                // This method is called once with the initial value and again
                // whenever Data at this location is updated.
                 data = dataSnapshot.getValue(User.class);
list.add(data.getUID());
                // Log.v("DATA", "" + data.getId() + data.getName() + data.getCity());
                User email = new User(data.getUID(),data.getPassword(), data.getName(), data.getEmail(), data.getGEnder());
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

//        FirebaseDatabase.getInstance().getReference().child("User Info").addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                Log.d("datasnapshotValue",dataSnapshot.getValue()+"");
//                for(DataSnapshot data: dataSnapshot.getChildren()){
//
//                    uSer = dataSnapshot.getValue(User.class);
//
//                    Log.d("USER", "D: " + uSer.getUUID() + uSer.getName() + uSer.getEmail() + uSer.getPassword() + uSer.getGender());
//
//                    messages.add(new User(uSer.getUUID() , uSer.getName() , uSer.getEmail() , uSer.getPassword() , uSer.getGender()));
//                    listAdapter.notifyDataSetChanged();
//                }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
    }






    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    private void logOut() {
        FirebaseAuth.getInstance().signOut();
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
