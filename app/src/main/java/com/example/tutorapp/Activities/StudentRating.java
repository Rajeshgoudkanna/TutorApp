package com.example.tutorapp.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tutorapp.R;

public class StudentRating extends AppCompatActivity implements View.OnClickListener{

    Button ratingBtn;
    Button viewCourseBtn;
    String selected_item;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_rating);
        getSupportActionBar().setTitle("Select Option");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ratingBtn=findViewById(R.id.give_rating);
        viewCourseBtn=findViewById(R.id.view_files);
        ratingBtn.setOnClickListener(this);
        viewCourseBtn.setOnClickListener(this);

        Bundle bundle = getIntent().getExtras();
        selected_item=bundle.getString("selectedItem");
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.give_rating:
                Intent reviews_intent=new Intent(this, CourseReviewActivity.class);
                reviews_intent.putExtra("pid",selected_item);
                startActivity(reviews_intent);
                break;

            case R.id.view_files:

                Intent intent=new Intent(this, UserCourseViewActivity.class);
                intent.putExtra("selected",selected_item);
                startActivity(intent);
                break;
        }

    }
}