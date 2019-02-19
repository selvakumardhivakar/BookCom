package com.example.androidstudiofirebase1;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {


    //Firebase Authentication object -> mAuth
    FirebaseAuth mAuth;
    EditText mEmail,mPassword;
    Button mRegister,mLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //INIT
        mAuth=FirebaseAuth.getInstance();
        mEmail=(EditText)findViewById(R.id.txt_login_email);
        mPassword=(EditText)findViewById(R.id.txt_login_pwd);
        mLogin=(Button)findViewById(R.id.btn_login);
        mRegister=(Button)findViewById(R.id.btn_lgn_register);

        //Click Listeners

        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email,pwd;
                email=mEmail.getText().toString();
                pwd=mPassword.getText().toString();

                if(!TextUtils.isEmpty(email) &&
                        !TextUtils.isEmpty(pwd)){

                    loginUser(email,pwd);
                }
                else{
                    Toast.makeText(LoginActivity.this, "Enter the valid Credentials", Toast.LENGTH_SHORT).show();
                }
            }


        });
        mRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    sendToRegister();
            }
        });

    }

    private void sendToRegister() {
        Intent intent=new Intent(getApplicationContext(),RegisterActivity.class);
        startActivity(intent);
    }

    private void loginUser(String email, String pwd) {
        mAuth.signInWithEmailAndPassword(email,pwd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Intent intent=new Intent(getApplicationContext(),MainActivity.class);
                    startActivity(intent);
                    finish();
                }
                else{
                    Toast.makeText(LoginActivity.this, "Something went wrong...", Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
