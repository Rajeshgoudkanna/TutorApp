package com.example.tutorapp.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.tutorapp.R;

public class TutorViewRating extends AppCompatActivity implements View.OnClickListener{

    Button ratingBtn;
    Button uploadFilesBtn;
    String selected_item;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutor_view_rating);

        ratingBtn=findViewById(R.id.rating);
        uploadFilesBtn=findViewById(R.id.upload_pdf);
        ratingBtn.setOnClickListener(this);
        uploadFilesBtn.setOnClickListener(this);

        Bundle bundle = getIntent().getExtras();
        selected_item=bundle.getString("selectedItem");
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.rating:
                Intent reviews_intent=new Intent(this, CourseReviewsActivity.class);
                reviews_intent.putExtra("pid",selected_item);
                startActivity(reviews_intent);
                break;

            case R.id.upload_pdf:
                Intent tutor_intent=new Intent(this, TutorViewCoursesActivity.class);
                tutor_intent.putExtra("selected",selected_item);
                startActivity(tutor_intent);
                break;
        }

    }
}