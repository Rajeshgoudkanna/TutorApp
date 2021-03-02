package com.example.tutorapp.Activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.tutorapp.Adapters.CourseReviewsAdapter;
import com.example.tutorapp.Adapters.CourseReviewsAdapter;
import com.example.tutorapp.R;
import com.example.tutorapp.model.Rating;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CourseReviewsActivity extends AppCompatActivity {


    ListView list_view;
    List<Rating> a1;
    Float rate = 0f;
    RatingBar ratingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reviews);

        getSupportActionBar().setTitle("Course Reviews");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        a1= new ArrayList<>();
        list_view=(ListView)findViewById(R.id.list_view);
        ratingBar = findViewById(R.id.tv_rating);
        final DatabaseReference RootRef;

        RootRef = FirebaseDatabase.getInstance().getReference();

        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child("Course Reviews").child(getIntent().getStringExtra("pid")).exists()) {
                    Query query = FirebaseDatabase.getInstance().getReference("Course Reviews").child(getIntent().getStringExtra("pid"))
                            .orderByChild("pid")
                            .equalTo(getIntent().getStringExtra("pid"));

                    query.addListenerForSingleValueEvent(valueEventListener);
                }
                else{
                    Toast.makeText(CourseReviewsActivity.this, "No Data found..", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
    ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            a1.clear();
            if (dataSnapshot.exists()) {
                int count =0;
                Float rateChange = 0f;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Rating reviews = snapshot.getValue(Rating.class);
                    a1.add(reviews);
                    rateChange +=Float.parseFloat(reviews.getRating());
                    count+=1;
                }
                rate = rateChange/count;
                ratingBar.setRating(rate);
                CourseReviewsAdapter courseReviewsAdapter= new CourseReviewsAdapter(a1, CourseReviewsActivity.this);
                list_view.setAdapter(courseReviewsAdapter);

            }
            else {
                Toast.makeText(CourseReviewsActivity.this, "No data Found", Toast.LENGTH_SHORT).show();
            }
        }
        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };
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
