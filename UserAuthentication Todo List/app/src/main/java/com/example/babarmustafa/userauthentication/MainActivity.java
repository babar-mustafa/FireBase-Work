package com.example.babarmustafa.userauthentication;
//http://www.androidhive.info/2016/06/android-getting-started-firebase-simple-login-registration-auth/

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import static android.R.attr.password;

public class MainActivity extends AppCompatActivity {
    ProgressDialog progres;
    Button forsigin;
    private FirebaseAuth auth;
    EditText get_user;
    EditText get_getpass;
    TextView forsignup;

    String usernameforlogin;
    String passwordforlogin;

//

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        auth = FirebaseAuth.getInstance();
        progres = new ProgressDialog(this);
        forsignup = (TextView) findViewById(R.id.singup_button);
        forsigin = (Button) findViewById(R.id.login_button);
        get_user = (EditText) findViewById(R.id.user_name);
        get_getpass = (EditText) findViewById(R.id.user_pass);


        forsigin.setOnClickListener(new View.OnClickListener() {


            public void onClick(View v) {
                progres.setMessage("Signing in....");
                progres.show();
                if (get_user.getText().toString().length() == 0) {
                    Toast.makeText(getApplicationContext(), "You Should to wirte the UserName ", Toast.LENGTH_LONG).show();
                    get_user.setError("Enter The User Name ");
                    return;
                }

                if (get_getpass.getText().toString().length() > 6) {
                    Toast.makeText(getApplicationContext(), "Password Must be 6 digits or more than 6 ", Toast.LENGTH_LONG).show();
                    get_getpass.setError("Enter The password ");
                    return;
                } else if (get_getpass.getText().toString().length() == 0) {
                    Toast.makeText(getApplicationContext(), "You Must input The Password", Toast.LENGTH_LONG).show();
                    get_getpass.setError("Enter The password ");
                    return;
                }


                usernameforlogin = get_user.getText().toString();
                passwordforlogin = get_getpass.getText().toString();
                auth.signInWithEmailAndPassword(usernameforlogin, passwordforlogin)
                        .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {

                                if (!task.isSuccessful()) {

                                    // there was an error
                                    Toast.makeText(MainActivity.this, "UserName or Password is correct", Toast.LENGTH_SHORT).show();

                                } else {
                                    progres.dismiss();
                                    Intent call = new Intent(MainActivity.this, TodoList_Mainactivity.class);
                                    startActivity(call);
                                }


                            }
                        });


            }
        });


        forsignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent call = new Intent(MainActivity.this, SignUp_Form.class);
                startActivity(call);
            }
        });

    }

}
