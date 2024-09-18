package com.management.task;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class RegisterActivity extends AppCompatActivity {

    Button regBtn;
    TextView loginTv;
    EditText emailEdt,passEdt;
    String email,pass;
    Context context;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        context=RegisterActivity.this;
        getWindow().setStatusBarColor(getResources().getColor(R.color.walktitle));
        mAuth=FirebaseAuth.getInstance();
        regBtn=findViewById(R.id.registerBtn);
        loginTv=findViewById(R.id.loginTv);
        emailEdt=findViewById(R.id.emailEdt);
        passEdt=findViewById(R.id.passwordEdt);


        regBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                email=emailEdt.getText().toString();
                pass=passEdt.getText().toString();
                if(TextUtils.isEmpty(email)) {
                    Toast.makeText(context, "Enter Email Id", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(pass)) {
                    Toast.makeText(context, "Enter password", Toast.LENGTH_SHORT).show();
                    return;
                }
                createUser();
            }
        });
        loginTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(RegisterActivity.this,LoginActivity.class);
                startActivity(i);
                //finish();

            }
        });





    }
    void createUser()
    {
        mAuth.createUserWithEmailAndPassword(email, pass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            //Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(RegisterActivity.this, "Authentication Success.",
                                    Toast.LENGTH_SHORT).show();
                            Intent i=new Intent(RegisterActivity.this,LoginActivity.class);
                                    startActivity(i);
                            //finish();
                            //updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            //Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                    Toast.makeText(RegisterActivity.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                            //updateUI(null);
                        }
                    }
                });
    }
}