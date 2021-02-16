package com.example.tutorapp.Activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

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

public class UserCourseViewActivity extends Fragment implements View.OnClickListener {

    TextView startDate;
    TextView endDate;
    TextView courseDescription;
    TextView courseCondition;
    TextView courseCategory;
    TextView courseName;
    TextView courseID;
    ListView courseFiles;
    String id = "";

    ProgressBar progressBar;
    List<String> fileArray = new ArrayList<String>();
    StorageReference mStorageReference;
    DatabaseReference mDatabaseReference;
    List<Files> CompletefileArray = new ArrayList<Files>();
    private FirebaseDatabase database;
    ArrayAdapter filesAdpater;

    View view;


    public static UserCourseViewActivity viewCourses() {
        UserCourseViewActivity fragment = new UserCourseViewActivity();
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        view = inflater.inflate(R.layout.activity_user_course_view, container, false);
        startDate = view.findViewById(R.id.start_date);
        endDate = view.findViewById(R.id.end_date);
        courseCategory = view.findViewById(R.id.category_txt);
        courseCondition = view.findViewById(R.id.condition_txt);
        courseDescription = view.findViewById(R.id.txt_description);
        courseName = view.findViewById(R.id.course_name);
        courseID = view.findViewById(R.id.course_id);
        courseFiles = view.findViewById(R.id.files_listView);

        Bundle bundle = getActivity().getIntent().getExtras();
        id = bundle.getString("selected");

        database = FirebaseDatabase.getInstance();

        getdata(id);

        mStorageReference = FirebaseStorage.getInstance().getReference();
        mDatabaseReference = FirebaseDatabase.getInstance().getReference("uploads");
        progressBar = (ProgressBar) view.findViewById(R.id.progressbar);

        filesAdpater = new ArrayAdapter(view.getContext(), R.layout.files_list_item, fileArray);

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

        return view;

    }


    private void getdata(String id) {

        DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        DatabaseReference ref = database.child("Courses");

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                    Course tutor_course = singleSnapshot.getValue(Course.class);
                    if (tutor_course.getPid().equals(id)) {
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
                Toast.makeText(view.getContext(), databaseError.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }


    public void getCourseFiles(String course_id) {

        DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        DatabaseReference ref = database.child("Files");

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                    Files new_file = singleSnapshot.getValue(Files.class);
                    if (new_file.getCourseID().equals(id)) {
                        fileArray.add(new_file.getFile_name());
                        CompletefileArray.add(new_file);
                    }
                }
                courseFiles.setAdapter(filesAdpater);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(view.getContext(), databaseError.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

        }

    }
}