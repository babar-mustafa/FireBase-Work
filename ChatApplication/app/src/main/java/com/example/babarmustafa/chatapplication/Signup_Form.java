package com.example.babarmustafa.chatapplication;

import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class Signup_Form extends AppCompatActivity {

    RadioGroup gender;

    Button push_data;
    DatabaseReference databse;
    ProgressDialog progres;
    FirebaseUser firebaseUser;
    private FirebaseAuth mAuth;
    User user;
    public HashMap<String, String> hashObj = new HashMap<>();
    private FirebaseAuth.AuthStateListener mAuthListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup__form);
         final RadioGroup u_sex = (RadioGroup) findViewById(R.id.radioSex);


        mAuth = FirebaseAuth.getInstance();
        databse = FirebaseDatabase.getInstance().getReference();
        firebaseUser = mAuth.getCurrentUser();
        progres = new ProgressDialog(this);

        push_data = (Button) findViewById(R.id.signup_button);

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {


                if (firebaseUser != null) {

                    // User is signed in
                    Log.d("TAG", "onAuthStateChanged:signed_in:" + firebaseUser.getUid());
                } else {
                    // User is signed out
                    Log.d("TAG", "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };
        push_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progres.setMessage("Creating a user One moment Please....");
                progres.show();
                progres.setCancelable(false);

                final EditText u_name = (EditText) findViewById(R.id.s_username);
                final EditText u_email = (EditText) findViewById(R.id.s_useremail);
                final EditText u_pass= (EditText) findViewById(R.id.s_userpass);

                if (u_name.getText().toString().length() == 0) {
                    Toast.makeText(getApplicationContext(), "Name cannot be Blank", Toast.LENGTH_LONG).show();
                    u_name.setError("Enter the Name");
                    return;
                } else if (u_email.getText().toString().length() == 0) {
                    Toast.makeText(getApplicationContext(), "Email can't be blank", Toast.LENGTH_LONG).show();
                    u_email.setError(" Enter The Email");
                    return;
                } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(u_email.getText().toString()).matches()) {
                    //Validation for Invalid Email Address
                    Toast.makeText(getApplicationContext(), "Invalid Email", Toast.LENGTH_LONG).show();
                    u_email.setError("Invalid Email address ");
                    return;
          }
// else if (u_pass.getText().toString().length() == 0) {
//                    Toast.makeText(getApplicationContext(), "password can't be blank", Toast.LENGTH_LONG).show();
//                    u_pass.setError("Enter The password ");
//                    return;
//                } else if (u_pass.getText().toString().length() > 6) {
//                    Toast.makeText(getApplicationContext(), "Password Must be 6 digits or more than 6 ", Toast.LENGTH_LONG).show();
//                    u_pass.setError("Enter The password ");
//                    return;
//                }

                int selectedId=u_sex.getCheckedRadioButtonId();
                 RadioButton radioButton=(RadioButton)findViewById(selectedId);


                final String get_name = u_name.getText().toString();
                final String get_email = u_email.getText().toString();
                final String get_pas = u_pass.getText().toString();
                final String get_gender = radioButton.getText().toString();



                mAuth.createUserWithEmailAndPassword(get_email,get_pas)
                        .addOnCompleteListener(Signup_Form.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                //if authentication failed soo....
                                if (!task.isSuccessful()) {
                                    Toast.makeText(Signup_Form.this, "Authentication Failed",
                                            Toast.LENGTH_SHORT).show();
                                }
                                else {

                                    String id = mAuth.getCurrentUser().getUid();

                                    user = new User(id, get_name, get_email, get_pas,get_gender);


                                    hashObj.put("UID", user.getUID());
                                    hashObj.put("Name", user.getName());
                                    hashObj.put("Email", user.getEmail());
                                    hashObj.put("Password", user.getPassword());
                                    hashObj.put("GEnder", user.getGEnder());

                                    Toast.makeText(Signup_Form.this, "User Created Succesfully", Toast.LENGTH_SHORT).show();
                                    databse.child("User Info").child(user.getUID()).setValue(hashObj);
                                    progres.dismiss();
                                    //to close activity
                                    finish();

                                }

                            }
                        });

            }
        });
    }
    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }
}
