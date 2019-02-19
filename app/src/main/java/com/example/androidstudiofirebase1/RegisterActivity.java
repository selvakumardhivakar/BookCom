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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    //Firebase Authentication object -> mAuth
    FirebaseAuth mAuth;
    //Database object -> mDatabase;
    DatabaseReference mDatabase;
    EditText mUname, mEmail,mPassword;
    Button mRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //INIT
        mAuth=FirebaseAuth.getInstance();
        mUname=(EditText)findViewById(R.id.txt_username);
        mEmail=(EditText)findViewById(R.id.txt_email);
        mPassword=(EditText)findViewById(R.id.txt_pwd);
        mRegister=(Button)findViewById(R.id.btn_register);
        mDatabase= FirebaseDatabase.getInstance().getReference();

        //Click Listener

        mRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name,email,pwd;
                name=mUname.getText().toString();
                email=mEmail.getText().toString();
                pwd=mPassword.getText().toString();

                if(!TextUtils.isEmpty(name) &&
                        !TextUtils.isEmpty(email) &&
                            !TextUtils.isEmpty(pwd))
                {
                    registerUser(name,email,pwd);
                }
                else{
                    Toast.makeText(getApplicationContext(),"Enter the valid credentials !",Toast.LENGTH_SHORT).show();
                }
            }

            
        });


    }

    private void registerUser(final String name, final String email, final String pwd) {
        mAuth.createUserWithEmailAndPassword(email,pwd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(RegisterActivity.this, "Successfully Registered !!!", Toast.LENGTH_SHORT).show();
                    Map<String,String> map=new HashMap<String,String>();
                    map.put("Name",name);
                    map.put("Email",email);
                    map.put("Password",pwd);

                    mDatabase.child("Users").child(mAuth.getCurrentUser().getUid()).setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(RegisterActivity.this,"Registeration Successfully Done...!", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                                startActivity(intent);
                            }
                            else {
                                Toast.makeText(RegisterActivity.this, "Please Try Again !!!", Toast.LENGTH_SHORT).show();
                                mAuth.getCurrentUser().delete();
                            }
                        }
                    });
                }
                else {
                    Toast.makeText(RegisterActivity.this, "Registeration Failed !!!", Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(RegisterActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
