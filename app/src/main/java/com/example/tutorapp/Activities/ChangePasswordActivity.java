package com.example.tutorapp.Activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.tutorapp.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ChangePasswordActivity extends AppCompatActivity {
    Button btn_update;
    EditText et_change_pass;

    SharedPreferences sharedPreferences;
    String session;
    String userSelected,user_role;
    private String parentDbName = "Users";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_changepwd);

        getSupportActionBar().setTitle("Change Password");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        sharedPreferences = getSharedPreferences(Utils.SHREF, Context.MODE_PRIVATE);
        session = sharedPreferences.getString("user_name", "def-val");
        user_role = sharedPreferences.getString("user_role", "def-role");




        btn_update=(Button)findViewById(R.id.btn_update);
        et_change_pass=(EditText)findViewById(R.id.et_change_pass);

        final String passwordPattern = "(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{6,}$";


        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(et_change_pass.getText().toString()))
                {
                    Toast.makeText(ChangePasswordActivity.this, "Please write your password...", Toast.LENGTH_SHORT).show();
                }
                else if (!(et_change_pass.getText().toString()).matches(passwordPattern)){
                    Toast.makeText(ChangePasswordActivity.this,"please write password of 6 chars with alteast one letter and number", Toast.LENGTH_SHORT).show();

                }
                else {


                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(parentDbName).child(user_role).child(session).child("password");
                    databaseReference.setValue(et_change_pass.getText().toString());
                    Toast.makeText(ChangePasswordActivity.this, "Password Updated Succussfully", Toast.LENGTH_SHORT).show();
                    finish();
                }
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
