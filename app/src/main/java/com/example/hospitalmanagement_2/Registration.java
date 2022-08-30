package com.example.hospitalmanagement_2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Registration extends AppCompatActivity {
    TextView registerqn;
    EditText name,age, gender, email,password,id,phone;
    Button reg;
    private FirebaseAuth mAuth;
    private DatabaseReference userDatabaseRef;
    private ProgressBar loader;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        registerqn= findViewById(R.id.loginPageQuestion);
        registerqn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i= new Intent(Registration.this,MainActivity.class);
                startActivity(i);
            }
        });

        name= findViewById(R.id.registrationFullName);
        age= findViewById(R.id.age);
        gender= findViewById(R.id.gender);
        email= findViewById(R.id.loginEmail);
        password= findViewById(R.id.editTextTextPassword);
        id= findViewById(R.id.registrationIDNumber);
        phone= findViewById(R.id.registrationPhoneNumber);


        mAuth=FirebaseAuth.getInstance();

        reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String sname=name.getText().toString().trim();
                final String sage=age.getText().toString().trim();
                final String sgen=gender.getText().toString().trim();
                final String semail=email.getText().toString().trim();
                final String spassword=password.getText().toString().trim();
                final String sid=id.getText().toString().trim();
                final String sphone=phone.getText().toString().trim();

                if(TextUtils.isEmpty(sname)||TextUtils.isEmpty(sage)||TextUtils.isEmpty(sgen)||TextUtils.isEmpty(semail)||TextUtils.isEmpty(spassword)||TextUtils.isEmpty(sid)||TextUtils.isEmpty(sphone)){
                    Toast.makeText(getApplicationContext(),"enter all the details",Toast.LENGTH_LONG).show();
                }
                else {

                    loader.setVisibility(View.VISIBLE);
                    mAuth.createUserWithEmailAndPassword(semail, spassword)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        user u = new user(sname, sage, semail, sgen, sid, sphone);
                                        FirebaseDatabase.getInstance().getReference("users")
                                                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                                .setValue(u).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {
                                                            Toast.makeText(getApplicationContext(), "details are successfully filled! ", Toast.LENGTH_LONG).show();
                                                        } else {
                                                            Toast.makeText(getApplicationContext(), "failed to register try again ", Toast.LENGTH_LONG).show();
                                                        }
                                                        loader.setVisibility(View.GONE);
                                                    }
                                                });
                                    } else {
                                        Toast.makeText(getApplicationContext(), "failed to register try again ", Toast.LENGTH_LONG).show();
                                        loader.setVisibility(View.GONE);


                                    }
                                }
                            });
                }
            }
        });

    }
}

