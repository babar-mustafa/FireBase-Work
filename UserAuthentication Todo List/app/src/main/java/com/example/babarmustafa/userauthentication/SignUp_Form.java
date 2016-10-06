package com.example.babarmustafa.userauthentication;

import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class SignUp_Form extends AppCompatActivity {


    Button add_btn;
    DatabaseReference databse;
    ProgressDialog progres;
    FirebaseUser firebaseUser;
    User user;
    private FirebaseAuth mAuth;
    public HashMap<String, String> hashObj = new HashMap<>();
    private FirebaseAuth.AuthStateListener mAuthListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up__form);
        mAuth = FirebaseAuth.getInstance();
        progres = new ProgressDialog(this);
        databse = FirebaseDatabase.getInstance().getReference();
        add_btn = (Button) findViewById(R.id.saving_button);
        firebaseUser = mAuth.getCurrentUser();

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
        // ...


        add_btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                progres.setMessage("Creating a user One moment Please....");
                progres.show();
                final EditText name = (EditText) findViewById(R.id.edittext_for_name);
                final EditText email = (EditText) findViewById(R.id.edittext_for_email);
                final EditText password = (EditText) findViewById(R.id.edittext_for_pasword);


                if (name.getText().toString().length() == 0) {
                    Toast.makeText(getApplicationContext(), "Name cannot be Blank", Toast.LENGTH_LONG).show();
                    name.setError("Enter the Name");
                    return;
                } else if (email.getText().toString().length() == 0) {
                    Toast.makeText(getApplicationContext(), "Email can't be blank", Toast.LENGTH_LONG).show();
                    email.setError(" Enter The Email");
                    return;
                } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email.getText().toString()).matches()) {
                    //Validation for Invalid Email Address
                    Toast.makeText(getApplicationContext(), "Invalid Email", Toast.LENGTH_LONG).show();
                    email.setError("Invalid Email address ");
                    return;
                } else if (password.getText().toString().length() == 0) {
                    Toast.makeText(getApplicationContext(), "password can't be blank", Toast.LENGTH_LONG).show();
                    password.setError("Enter The password ");
                    return;
                } else if (password.getText().toString().length() > 60) {
                    Toast.makeText(getApplicationContext(), "Password Must be 6 digits or more than 6 ", Toast.LENGTH_LONG).show();
                    password.setError("Enter The password ");
                    return;
                }


                final String forname = name.getText().toString();
                final String foremail = email.getText().toString();
                final String forpassword = password.getText().toString();


//                databse.child("User Information").push().setValue(forname);
//                databse.child("User Information").push().setValue(foremail);
//                databse.child("User Information").push().setValue(forpassword);


                //this method create a user authentication

                mAuth.createUserWithEmailAndPassword(foremail, forpassword)
                        .addOnCompleteListener(SignUp_Form.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                Log.d("TAG", "createUserWithEmail:onComplete:" + task.isSuccessful());


                                // If sign in fails, display a message to the firebaseUser. If sign in succeeds
                                // the auth state listener will be notified and logic to handle the
                                // signed in firebaseUser can be handled in the listener.
                                if (!task.isSuccessful()) {
                                    Toast.makeText(SignUp_Form.this, "Authentication Failed",
                                            Toast.LENGTH_SHORT).show();
                                } else {

                                    String id = mAuth.getCurrentUser().getUid();

                                    user = new User(id, forname, foremail, forpassword);


                                    hashObj.put("UID", user.getId());
                                    hashObj.put("Name", user.getName());
                                    hashObj.put("Email", user.getEmail());
                                    hashObj.put("Password", user.getPass());

                                    Toast.makeText(SignUp_Form.this, "User Created Succesfully", Toast.LENGTH_SHORT).show();
                                    databse.child("User Info").child(user.getId()).setValue(hashObj);
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
