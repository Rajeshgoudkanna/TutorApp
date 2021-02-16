package com.example.tutorapp.model;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.tutorapp.Activities.Utils;
import com.example.tutorapp.MainActivity;
import com.example.tutorapp.R;
import com.example.tutorapp.registration;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class login extends AppCompatActivity {

    TextView tv_username,tv_password,tv_forgetpwd,tv_newuser,tv_signup;
    EditText et_USERNAME,et_PWD;
    Button btn_login;
    RadioGroup radioGroup;
    String userSelected;
    private String parentDbName = "Users";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().setTitle("Tutor App");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        tv_username=(TextView)findViewById(R.id.tv_username);
        tv_password=(TextView)findViewById(R.id.tv_password);
        tv_forgetpwd=(TextView)findViewById(R.id.tv_forgetpwd);
        tv_newuser=(TextView)findViewById(R.id.tv_newuser);
        tv_signup=(TextView)findViewById(R.id.tv_signup);
        radioGroup = (RadioGroup) findViewById(R.id.usergroup);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checked) {
                if(checked == R.id.radioButton){
                    userSelected = "Tutor";
                }
                else if(checked == R.id.radioButton2){
                    userSelected = "Student";
                }

            }
        });

        tv_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(login.this, registration.class);
                startActivity(intent);
            }

        });


        et_USERNAME=(EditText) findViewById(R.id.et_USERNAME);
        et_PWD=(EditText)findViewById(R.id.et_PWD);

        btn_login=(Button) findViewById(R.id.btn_login);
        btn_login.setOnClickListener(view -> LoginUser());

    }
    private void LoginUser() {
        String username = et_USERNAME.getText().toString();
        String password = et_PWD.getText().toString();

        if (TextUtils.isEmpty(username))
        {
            Toast.makeText(this, "Please write your Username...", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(password))
        {
            Toast.makeText(this, "Please write your password...", Toast.LENGTH_SHORT).show();
        }
        else if(userSelected.isEmpty()){
            Toast.makeText(this, "Please select either tutor or student...", Toast.LENGTH_SHORT).show();
        }
        else {

            AllowAccessToAccount(username, password);

        }
    }
    private void AllowAccessToAccount(final String username, final String password) {

        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();

        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child("Users").child(userSelected).child(username).exists()){
                    User usersData = snapshot.child("Users").child(userSelected).child(username).getValue(User.class);
                    Log.i("username","test"+usersData.getUsername());
                    if(usersData.getUsername().equals(username)){
                        if(usersData.getPassword().equals(password)){
                            Toast.makeText(login.this, "logged in Successfully...", Toast.LENGTH_SHORT).show();
                            if(userSelected=="Student"){
                                Intent intent = new Intent(login.this, MainActivity.class);
                                SharedPreferences sharedPreferences = getSharedPreferences(Utils.SHREF, Context.MODE_PRIVATE);
                                SharedPreferences.Editor et=sharedPreferences.edit();
                                et.putString("user_name",et_USERNAME.getText().toString());
                                et.commit();
                                startActivity(intent);
                                finish();
                            }else {
                                Intent intent = new Intent(login.this, main_tutor.class);
                                SharedPreferences sharedPreferences = getSharedPreferences(Utils.SHREF, Context.MODE_PRIVATE);
                                SharedPreferences.Editor et=sharedPreferences.edit();
                                et.putString("user_name",et_USERNAME.getText().toString());
                                et.commit();
                                startActivity(intent);
                                finish();
                            }

                        }
                        else {
                            Toast.makeText(login.this, "Password is incorrect", Toast.LENGTH_SHORT).show();
                        }
                    }


                }
                else
                {
                    Toast.makeText(login.this, "Account with this " + username + " do not exists.", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}