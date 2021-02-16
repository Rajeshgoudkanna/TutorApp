package com.example.tutorapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class registration extends AppCompatActivity {

    TextView tv_name,tv_phone,tv_email,tv_user_name,tv_password;
    EditText et_name,et_phone,et_email,et_uname,et_PWD;
    Button btn_register;
    String name,phone,email,password,username;
    RadioGroup radioGroup;
    String userSelected;
    private String parentDbName = "Users";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        getSupportActionBar().setTitle("Registration");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        tv_name=(TextView)findViewById(R.id.btn_register);
        tv_phone=(TextView)findViewById(R.id.tv_phone);
        tv_email=(TextView)findViewById(R.id.tv_email);
        tv_user_name=(TextView)findViewById(R.id.tv_user_name);
        tv_password=(TextView)findViewById(R.id.tv_password);

        et_name=(EditText)findViewById(R.id.et_name);
        et_phone=(EditText)findViewById(R.id.et_phone);
        et_email=(EditText)findViewById(R.id.et_email);
        et_PWD=(EditText)findViewById(R.id.et_PWD);
        et_uname=(EditText)findViewById(R.id.et_uname);
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

        btn_register=(Button)findViewById(R.id.btn_register);
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateUserData();
            }
        });

    }
    private void validateUserData()
    {
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        String passwordPattern = "(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{6,}$";
        String phonepattern = "^(\\+\\d{1,2}\\s)?\\(?\\d{3}\\)?[\\s.-]\\d{3}[\\s.-]\\d{4}$";
        name = et_name.getText().toString();
        phone = et_phone.getText().toString().trim();
        email = et_email.getText().toString().trim();
        password = et_PWD.getText().toString().trim();
        username = et_uname.getText().toString();
        if (TextUtils.isEmpty(name))
        {
            Toast.makeText(this, "Please write your name...", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(email))
        {
            Toast.makeText(this, "Please write your Email...", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(phone))
        {
            Toast.makeText(this, "Please write your phone number...", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(username))
        {
            Toast.makeText(this, "Please Choose your Username...", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(password))
        {
            Toast.makeText(this, "Please write your password...", Toast.LENGTH_SHORT).show();
        }
        else if (userSelected.isEmpty())
        {
            Toast.makeText(this, "Please select either tutor or student...", Toast.LENGTH_SHORT).show();
        }
        else if (!email.matches(emailPattern)){
            Toast.makeText(this,"Please write valid email address",Toast.LENGTH_SHORT).show();
        }
        else if (!password.matches(passwordPattern)){
            Toast.makeText(this,"please write password of 6 chars with alteast one letter and number",Toast.LENGTH_SHORT).show();

        }
        else if(!phone.matches(phonepattern)){
            Toast.makeText(this,"please write 10 digit phone number in the 123 123 1234 format",Toast.LENGTH_SHORT).show();
        }

        else
        {
            adduserData();
        }
    }
    private void adduserData() {

        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();

        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (!(dataSnapshot.child("Users").child(userSelected).child(username).exists()))
                {
                    HashMap<String, Object> userdataMap = new HashMap<>();
                    userdataMap.put("name", name);
                    userdataMap.put("phone", phone);
                    userdataMap.put("email", email);
                    userdataMap.put("username", username);
                    userdataMap.put("password", password);
                    RootRef.child("Users").child(userSelected).child(username).updateChildren(userdataMap)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task)
                                {
                                    if (task.isSuccessful())
                                    {
                                        Toast.makeText(registration.this, "Congratulations, your account has been created.", Toast.LENGTH_SHORT).show();

                                        finish();
                                    }
                                    else
                                    {
                                        Toast.makeText(registration.this, "Network Error: Please try again after some time...", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                }
                else
                {
                    Toast.makeText(registration.this, "This " + username + " already exists.", Toast.LENGTH_SHORT).show();

                    Toast.makeText(registration.this, "Please try again using another username.", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(registration.this, registration.class);
                    startActivity(intent);
                }
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}