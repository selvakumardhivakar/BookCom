package com.example.androidstudiofirebase1;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    //Firebase Authentication Object -> mAuth
    FirebaseAuth mAuth;
    TextView mlogout,mInfo;
    //Firebase Database Object -> mDatabase;
    DatabaseReference mDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //INIT
        mAuth=FirebaseAuth.getInstance();
        mDatabase= FirebaseDatabase.getInstance().getReference().child("Users");
        mlogout=(TextView)findViewById(R.id.txt_logout);
        mInfo=(TextView)findViewById(R.id.txt_info);
        mlogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                Intent intent=new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
        if(mAuth.getCurrentUser()!=null) {
            final String User_id = mAuth.getCurrentUser().getUid();
            mDatabase.child(User_id).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    final String name;
                    final String email;
                    name = dataSnapshot.child("Email").getValue().toString();
                    email = dataSnapshot.child("Name").getValue().toString();
                    String temp = "About Yourself:\n\nEmail:\n" + name + "\n" +
                            "Username:\n\t\t\t" + email;
                    mInfo.setText(temp);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        if(mAuth.getCurrentUser()==null){
            sendToLogin();
        }
    }

    private void sendToLogin() {
        Intent intent=new Intent(getApplicationContext(),LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
