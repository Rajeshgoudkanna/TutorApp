package com.example.tutorapp.Activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.tutorapp.R;
import com.example.tutorapp.model.Course;
import com.example.tutorapp.model.Files;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static android.app.Activity.RESULT_OK;

public class TutorViewCoursesActivity extends AppCompatActivity implements View.OnClickListener {

    TextView startDate;
    TextView endDate;
    TextView courseDescription;
    TextView courseCondition;
    TextView courseCategory;
    TextView courseName;
    TextView courseID;
    ListView courseFiles;
    Button uploadFile;
    EditText fileName;
    String id="";

    final static int PICK_PDF_CODE = 2342;
    ProgressBar progressBar;

    private FirebaseDatabase database;
    StorageReference mStorageReference;
    DatabaseReference mDatabaseReference;

    List<String> fileArray=new ArrayList<String>();
    List<Files> CompletefileArray=new ArrayList<Files>();
    ArrayAdapter filesAdpater;
    String downloadImageUrl;



    public static TutorViewCoursesActivity viewTutorCourses() {
        TutorViewCoursesActivity fragment = new TutorViewCoursesActivity();
        return fragment;
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutor_view_courses);

        startDate=findViewById(R.id.start_date);
        endDate=findViewById(R.id.end_date);
        courseCategory=findViewById(R.id.category_txt);
        courseCondition=findViewById(R.id.condition_txt);
        courseDescription=findViewById(R.id.txt_description);
        courseName=findViewById(R.id.course_name);
        courseID=findViewById(R.id.course_id);
        courseFiles=findViewById(R.id.files_listView);
        fileName=findViewById(R.id.file_name);
        uploadFile=findViewById(R.id.buttonUploadFile);
        uploadFile.setOnClickListener(this);
        Bundle bundle = getIntent().getExtras();
        id=bundle.getString("selected");

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

    private void getPDF() {

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ContextCompat.checkSelfPermission(view.getContext(),
//                Manifest.permission.READ_EXTERNAL_STORAGE)
//                != PackageManager.PERMISSION_GRANTED) {
//            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
//                    Uri.parse("package:" + view.getContext().getPackageName()));
//            startActivity(intent);
//            return;
//        }
        //creating an intent for file chooser
        Intent intent = new Intent();
        intent.setType("application/pdf");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select File"), PICK_PDF_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //when the user choses the file
        if (requestCode == PICK_PDF_CODE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            //if a file is selected
            if(fileName.getText().toString().length()==0)
            {
                fileName.setError("Required");
            }
            else
            {
                if (data.getData() != null) {
                    //uploading the file
                    uploadFile(data.getData());
                    // Toast.makeText(getApplicationContext(), data.getData().toString(), Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(getApplicationContext(), "No file chosen", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void uploadFile(Uri data) {

        progressBar.setVisibility(View.VISIBLE);
        String file_id=String.valueOf(System.currentTimeMillis());
        StorageReference sRef = mStorageReference.child("uploads/" + file_id+ ".pdf");


        final UploadTask uploadTask = sRef.putFile(data);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e)
            {
                String message = e.toString();
                Toast.makeText(getApplicationContext(), "Error: " + message, Toast.LENGTH_SHORT).show();
            }
        })
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot)
                    {
                        Toast.makeText(getApplicationContext(), "File Uploaded", Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);

                        Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                            @Override
                            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception
                            {
                                if (!task.isSuccessful())
                                {
                                    throw task.getException();
                                }
                                downloadImageUrl = sRef.getDownloadUrl().toString();
                                return sRef.getDownloadUrl();
                            }
                        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                            @Override
                            public void onComplete(@NonNull Task<Uri> task)
                            {
                                if (task.isSuccessful())
                                {
                                    downloadImageUrl = task.getResult().toString();
                                    Files upload = new Files(id, downloadImageUrl,
                                            fileName.getText().toString());
                                    sendFile(upload);
                                }
                            }
                        });
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

    public void sendFile(Files new_file)
    {
        DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        SimpleDateFormat for_id = new SimpleDateFormat("yyMMddHHmmssSS");
        String file_id = for_id.format(new Date());
        DatabaseReference ref = database.child("Files").child(file_id);

        ref.setValue(new_file)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        getCourseFiles(id);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Write failed
                        // ...
                    }
                });

    }

    public void getCourseFiles(String course_id){

        fileArray.clear();

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
            case R.id.buttonUploadFile:
                getPDF();
                break;
        }

    }
}