package com.example.tutorapp.Activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.tutorapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class CourseReviewActivity extends AppCompatActivity implements View.OnClickListener {


    TextView tv_name, tv_status, tv_price, tv_reviews, tv_desc, tv_condition, tv_category;
    ImageView image_view;
    Button btn_book;
    SharedPreferences sharedPreferences;
    String session, rating_num;
    RatingBar ratingBar;
    EditText et_course_review;
    Button btn_submit;
    private DatabaseReference ProductsRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.write_review);

        sharedPreferences = getSharedPreferences(Utils.SHREF, Context.MODE_PRIVATE);
        session = sharedPreferences.getString("user_name", "def-val");

        getSupportActionBar().setTitle("Course Review");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        et_course_review = (EditText) findViewById(R.id.et_course_review);
        btn_submit = (Button) findViewById(R.id.btn_submit);
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                courseReview();
                //Toast.makeText(ToolReviewActivity.this, ""+getIntent().getStringExtra("pid"), Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void courseReview() {

        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();

        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (!(dataSnapshot.child("Course Reviews").child(getIntent().getStringExtra("pid")).child(session).exists())) {
                    HashMap<String, Object> productMap = new HashMap<>();
                    productMap.put("pid", getIntent().getStringExtra("pid"));
                    productMap.put("username", session);
                    productMap.put("rating", String.valueOf(ratingBar.getRating()));
                    productMap.put("review", et_course_review.getText().toString());

                    RootRef.child("Course Reviews").child(getIntent().getStringExtra("pid")).child(session).updateChildren(productMap)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(CourseReviewActivity.this, "Thank you for the feedback.", Toast.LENGTH_SHORT).show();
                                        finish();
                                        // loadingBar.dismiss();
                                    } else {
                                        Toast.makeText(CourseReviewActivity.this, "Network Error: Please try again after some time...", Toast.LENGTH_SHORT).show();
                                    }

                                }

                            });
                } else {
                    Toast.makeText(CourseReviewActivity.this, "You have already given feedback to this Course.", Toast.LENGTH_SHORT).show();
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

    @Override
    public void onClick(View view) {

    }
}
