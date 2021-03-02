package com.example.tutorapp.Activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tutorapp.R;
import com.example.tutorapp.model.Course;
import com.example.tutorapp.model.Files;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class UserCourseViewActivity extends AppCompatActivity implements View.OnClickListener {

    TextView startDate;
    TextView endDate;
    TextView courseDescription;
    TextView courseCondition;
    TextView courseCategory;
    TextView courseName;
    TextView courseID;
    ListView courseFiles;
    String id="";

    ProgressBar progressBar;

    private FirebaseDatabase database;
    StorageReference mStorageReference;
    DatabaseReference mDatabaseReference;

    List<String> fileArray=new ArrayList<String>();
    List<Files> CompletefileArray=new ArrayList<Files>();
    ArrayAdapter filesAdpater;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_course_view);

        startDate=findViewById(R.id.start_date);
        endDate=findViewById(R.id.end_date);
        courseCategory=findViewById(R.id.category_txt);
        courseCondition=findViewById(R.id.condition_txt);
        courseDescription=findViewById(R.id.txt_description);
        courseName=findViewById(R.id.course_name);
        courseID=findViewById(R.id.course_id);
        courseFiles=findViewById(R.id.files_listView);

        Bundle bundle =  getIntent().getExtras();
        if (bundle != null) {
            id =bundle.getString("selected");
        }

        database = FirebaseDatabase.getInstance();

        getdata(id);

        mStorageReference = FirebaseStorage.getInstance().getReference();
        mDatabaseReference = FirebaseDatabase.getInstance().getReference("uploads");
        progressBar = (ProgressBar) findViewById(R.id.progressbar);

        filesAdpater=new ArrayAdapter(this, R.layout.files_list_item,fileArray);

        getCourseFiles(id);

        courseFiles.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //getting the upload
                Files upload = CompletefileArray.get(i);

                //Opening the upload file in browser using the upload url
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(upload.getFile()));
                startActivity(intent);
            }
        });

    }


    private void getdata(String id) {

        DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        DatabaseReference ref = database.child("Courses");

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot singleSnapshot : dataSnapshot.getChildren()){
                    Course tutor_course = singleSnapshot.getValue(Course.class);
                    if(tutor_course.getPid().equals(id)) {
                        startDate.setText(tutor_course.getCstartDate());
                        endDate.setText(tutor_course.getCendDate());
                        courseDescription.setText(tutor_course.getCdescription());
                        courseCategory.setText(tutor_course.getCcategory());
                        courseCondition.setText(tutor_course.getCtype());
                        courseID.setText(tutor_course.getPid());
                        courseName.setText(tutor_course.getCname());
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(),databaseError.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
    }


    public void getCourseFiles(String course_id){

        DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        DatabaseReference ref = database.child("Files");

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot singleSnapshot : dataSnapshot.getChildren()){
                    Files new_file = singleSnapshot.getValue(Files.class);
                    if(new_file.getCourseID().equals(id)) {
                        fileArray.add(new_file.getFile_name());
                        CompletefileArray.add(new_file);
                    }
                }
                courseFiles.setAdapter(filesAdpater);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(),databaseError.getMessage(),Toast.LENGTH_LONG).show();
            }
        });

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

        }

    }
}