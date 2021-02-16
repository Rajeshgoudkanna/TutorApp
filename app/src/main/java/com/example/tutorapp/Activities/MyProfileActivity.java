package com.example.tutorapp.Activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.tutorapp.R;
import com.example.tutorapp.model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MyProfileActivity extends AppCompatActivity {
    Button btn_update_profile, btn_logout;
    EditText et_name, et_phone, et_pwd, et_username,et_email;
    DatabaseReference dbArtists;
    User users;
    SharedPreferences sharedPreferences;
    String session;
    RadioGroup radioGroup;
    String userSelected,user_role;
    private String parentDbName = "Users";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);

        getSupportActionBar().setTitle("My Profile");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        sharedPreferences = getSharedPreferences(Utils.SHREF, Context.MODE_PRIVATE);
        session = sharedPreferences.getString("user_name", "def-val");
        user_role = sharedPreferences.getString("user_role", "def-role");

        btn_update_profile = (Button) findViewById(R.id.btn_update_profile);

        et_name = (EditText) findViewById(R.id.et_name);
        et_phone = (EditText) findViewById(R.id.et_phone);
        et_pwd = (EditText) findViewById(R.id.et_pwd);
        et_username = (EditText) findViewById(R.id.et_username);
        et_email = (EditText) findViewById(R.id.et_email);

        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();
        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child(parentDbName).child(user_role).child(session).exists()) {
                    User usersData = snapshot.child(parentDbName).child(user_role).child(session).getValue(User.class);
                    et_name.setText(usersData.getName());
                    et_email.setText(usersData.getEmail());
                    et_phone.setText(usersData.getPhone());
                    et_pwd.setText(usersData.getPassword());
                    et_username.setText(usersData.getUsername());

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        btn_update_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(parentDbName).child(user_role).child(session);
                User users = new User(et_name.getText().toString(), et_phone.getText().toString(), et_pwd.getText().toString(), session, et_email.getText().toString());
                databaseReference.setValue(users);
                Toast.makeText(MyProfileActivity.this, "Profile Updated Succussfully", Toast.LENGTH_SHORT).show();
                finish();
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
