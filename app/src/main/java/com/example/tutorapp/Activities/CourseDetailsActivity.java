package com.example.tutorapp.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.tutorapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class CourseDetailsActivity extends AppCompatActivity {

    TextView tv_name,tv_status,tv_price,tv_desc,tv_category,tv_type,tv_startDate,tv_endDate;
    ImageView image_view;
    Button btn_enroll,btn_get_reviews;
    SharedPreferences sharedPreferences;
    String session;
    String name,price,category,image,pid,posted_by,date,time,condition,description;
    private DatabaseReference CoursesRef;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_details);
        sharedPreferences =getSharedPreferences(Utils.SHREF, Context.MODE_PRIVATE);
        session = sharedPreferences.getString("user_name", "def-val");
        getSupportActionBar().setTitle("Enroll Course");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        CoursesRef = FirebaseDatabase.getInstance().getReference();
        tv_name=(TextView)findViewById(R.id.tv_name);
        tv_status=(TextView)findViewById(R.id.tv_status_value);
        tv_price=(TextView)findViewById(R.id.tv_price_value);
        tv_desc=(TextView)findViewById(R.id.tv_desc);
        tv_type=(TextView)findViewById(R.id.tv_type_value);
        tv_category = (TextView) findViewById(R.id.course_category_value);
        tv_startDate=(TextView)findViewById(R.id.tv_start_value);
        tv_endDate=(TextView)findViewById(R.id.tv_end_value);
        tv_name.setText(" :"+getIntent().getStringExtra("Cname"));
        tv_status.setText(" :"+getIntent().getStringExtra("Cstatus"));
        tv_price.setText(" :"+getIntent().getStringExtra("Cprice"));
        tv_desc.setText(getIntent().getStringExtra("Cdescription"));
        tv_type.setText(" :"+getIntent().getStringExtra("Ctype"));
        tv_category.setText(" :"+getIntent().getStringExtra("Ccategory"));
        tv_startDate.setText(getIntent().getStringExtra("CstartDate"));
        tv_endDate.setText(getIntent().getStringExtra("CendDate"));
        btn_enroll=(Button)findViewById(R.id.enroll_button);
        if (getIntent().getStringExtra("Cstatus").equals("Not Available")) {
            btn_enroll.setVisibility(View.INVISIBLE);
        }
        else  {
            btn_enroll.setVisibility(View.VISIBLE);
        }
        btn_enroll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HashMap<String, Object> courseMap = new HashMap<>();
                courseMap.put("pid", getIntent().getStringExtra("pid"));
                courseMap.put("Cimage", getIntent().getStringExtra("Cimage"));
                courseMap.put("Cname", getIntent().getStringExtra("Cname"));
                courseMap.put("Ccategory", getIntent().getStringExtra("Ccategory"));
                courseMap.put("Cprice", getIntent().getStringExtra("Cprice"));
                courseMap.put("Cdescription", getIntent().getStringExtra("Cdescription"));
                courseMap.put("Ctype", getIntent().getStringExtra("Ctype"));
                courseMap.put("Cstatus", getIntent().getStringExtra("Cstatus"));
                courseMap.put("posted_by", "session");
                courseMap.put("CstartDate",getIntent().getStringExtra("CstartDate"));
                courseMap.put("CendDate",getIntent().getStringExtra("CendDate"));
                CoursesRef.child("Enrolled Courses").child(session).child(getIntent().getStringExtra("pid")).updateChildren(courseMap)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task)
                            {
                                if (task.isSuccessful())
                                {
                                    Toast.makeText(CourseDetailsActivity.this, "Course enrolled successfully..", Toast.LENGTH_SHORT).show();
                                }
                                else
                                {
                                    String message = task.getException().toString();
                                    Toast.makeText(CourseDetailsActivity.this, "Error: " + message, Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
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
