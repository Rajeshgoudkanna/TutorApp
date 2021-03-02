
package com.example.tutorapp.Activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tutorapp.Adapters.CourseAdapter;
import com.example.tutorapp.R;
import com.example.tutorapp.model.Course;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CourseActivity extends AppCompatActivity {
    GridView grid_view;
    List<Course> a1;
    SharedPreferences sharedPreferences;
    String session;
    ProgressDialog progressDialog;
    CourseAdapter courseAdapter;
    String cat_name="";
    String user_role;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course);
        getSupportActionBar().setTitle("Courses");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        SharedPreferences sharedPreferences;
        sharedPreferences =getSharedPreferences(Utils.SHREF, Context.MODE_PRIVATE);
        session = sharedPreferences.getString("user_name", "def-val");
        user_role = sharedPreferences.getString("user_role", "def-role");

        Bundle bundle=getIntent().getExtras();
        cat_name=bundle.getString("selected");

        a1= new ArrayList<>();
        grid_view= (GridView) findViewById(R.id.gridview);
        progressDialog=new ProgressDialog(this);
        progressDialog.setTitle("Please Wait data is being Loaded");
        progressDialog.show();
        Query query=FirebaseDatabase.getInstance().getReference("Courses");

        query = query.orderByChild("Ccategory").equalTo(cat_name);

        query.addListenerForSingleValueEvent(valueEventListener);

        grid_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        });
    }

    ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            progressDialog.dismiss();
            a1.clear();
            if (dataSnapshot.exists()) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Course course = snapshot.getValue(Course.class);
                    if(user_role.equals("Tutor")){
                        if(course.getPosted_by().equals(session)){
                            a1.add(course);
                        }
                    }
                    else{
                        a1.add(course);
                    }
                }
                //Toast.makeText(getContext(), ""+a1.size(), Toast.LENGTH_SHORT).show();
                courseAdapter = new CourseAdapter( CourseActivity.this,a1,session);
                grid_view.setAdapter(courseAdapter);

            }
            else {
                Toast.makeText(CourseActivity.this, "No data Found", Toast.LENGTH_SHORT).show();
            }
        }
        @Override
        public void onCancelled(DatabaseError databaseError) {
            progressDialog.dismiss();

        }
    };
}