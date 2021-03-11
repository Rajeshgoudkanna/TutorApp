package com.example.tutorapp.Fragments;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tutorapp.Activities.ChangePasswordActivity;
import com.example.tutorapp.Activities.MyProfileActivity;
import com.example.tutorapp.Activities.Utils;
import com.example.tutorapp.R;
import com.example.tutorapp.login;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import static android.app.Activity.RESULT_OK;


public class AddFragment extends Fragment {

    EditText et_name,et_status,et_price,et_category,et_desc;
    TextView tv_end_date,tv_start_date;
    ImageView image_view;
    Button btn_submit;

    private static final int GalleryPick = 1;
    private Uri ImageUri;
    private String productRandomKey, downloadImageUrl;
    private StorageReference CourseImagesRef;
    private DatabaseReference CoursesRef;
    int mYear,mMonth,mDay;
    String DAY,MONTH,YEAR;
    String name,status,price,category,saveCurrentDate, saveCurrentTime,description,condition;
    SharedPreferences sharedPreferences;
    String session;
    Spinner spin_status,spin_category,spin_condition;

    View view;

    public static AddFragment addFragment() {
        AddFragment fragment = new AddFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        view = inflater.inflate(R.layout.fragment_add, container, false);
        setHasOptionsMenu(true);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Add Course");

        sharedPreferences =getActivity().getSharedPreferences(Utils.SHREF, Context.MODE_PRIVATE);
        session = sharedPreferences.getString("user_name", "");

        CourseImagesRef = FirebaseStorage.getInstance().getReference().child("Course Images");
        CoursesRef = FirebaseDatabase.getInstance().getReference().child("Courses");

        et_name=(EditText)view.findViewById(R.id.et_cname);

        et_desc=(EditText)view.findViewById(R.id.et_cdesc);
        et_price=(EditText)view.findViewById(R.id.et_cprice);
        spin_category=(Spinner)view.findViewById(R.id.spin_category);
        spin_condition=(Spinner)view.findViewById(R.id.spin_condition);
        image_view=(ImageView)view.findViewById(R.id.image_view);
        tv_end_date=(TextView)view.findViewById(R.id.tv_end_date);
        tv_start_date=(TextView)view.findViewById(R.id.tv_start_date);
        tv_start_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startDate();
            }
        });
        tv_end_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                endDate();
            }
        });


        image_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                OpenGallery();
            }
        });

        btn_submit=(Button)view.findViewById(R.id.btn_submit);
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ValidateProductData();

            }
        });



        return view;
    }
    private void OpenGallery()
    {
        Intent galleryIntent = new Intent();
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, GalleryPick);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==GalleryPick  &&  resultCode==RESULT_OK  &&  data!=null)
        {
            ImageUri = data.getData();
            image_view.setImageURI(ImageUri);
        }
    }
    private void startDate() {

        final Calendar c= Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay= c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog=new DatePickerDialog(getActivity(),
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                        DAY = dayOfMonth + " ";
                        MONTH = monthOfYear + 1 + "";
                        YEAR = year + "";
                        tv_start_date.setText(dayOfMonth+"-"+(monthOfYear +1)+"-"+year);
                    }
                },mYear,mMonth,mDay);
        datePickerDialog.show();
    }


    private void endDate() {

        final Calendar c= Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth= c.get(Calendar.MONTH);
        mDay=c.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog datePickerDialog= new DatePickerDialog(getActivity(),
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        DAY = dayOfMonth + " ";
                        MONTH = monthOfYear + 1 +"";
                        YEAR = year + "";

                        tv_end_date.setText(dayOfMonth + "-"+(monthOfYear + 1)+"-"+year);

                    }
                },mYear,mMonth,mDay);
        datePickerDialog.show();
    }
    private void ValidateProductData()
    {

        name=et_name.getText().toString();
//        status=spin_status.getSelectedItem().toString();
        price=et_price.getText().toString();
        condition=spin_condition.getSelectedItem().toString();
        description=et_desc.getText().toString();
        category=spin_category.getSelectedItem().toString();


        if (ImageUri == null)
        {
            Toast.makeText(getActivity(), "Course image is mandatory...", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(name))
        {
            Toast.makeText(getActivity(), "Please write Course name...", Toast.LENGTH_SHORT).show();
        }
//        else if (spin_status.getSelectedItem().toString().equals("Please Choose Status"))
//        {
//            Toast.makeText(getActivity(), "Please Choose Course Type...", Toast.LENGTH_SHORT).show();
//        }
        else if (TextUtils.isEmpty(price))
        {
            Toast.makeText(getActivity(), "Please write Course Price...", Toast.LENGTH_SHORT).show();
        }

        else if (TextUtils.isEmpty(description))
        {
            Toast.makeText(getActivity(), "Please write Course Description...", Toast.LENGTH_SHORT).show();
        }

        else if (spin_condition.getSelectedItem().toString().equals("Please Choose course type"))
        {
            Toast.makeText(getActivity(), "Please Choose Course type...", Toast.LENGTH_SHORT).show();
        }

        else if (spin_category.getSelectedItem().toString().equals("Please Choose course Category"))
        {
            Toast.makeText(getActivity(), "Please Choose Course Category...", Toast.LENGTH_SHORT).show();
        }
        else if(tv_start_date.getText().toString().equals("Course Start Date") || tv_end_date.getText().toString().equals("Course End Date"))
        {
            Toast.makeText(getActivity(), "Please choose From and To Date", Toast.LENGTH_SHORT).show();
        }
        else
        {
            StoreProductInformation();
        }
    }
    private void StoreProductInformation()
    {

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat for_id = new SimpleDateFormat("yyMMddHHmmssSS");
        saveCurrentDate = for_id.format(new Date());
/*

        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate = currentDate.format(calendar.getTime());
*/

        productRandomKey = saveCurrentDate;

        final StorageReference filePath = CourseImagesRef.child(ImageUri.getLastPathSegment() + productRandomKey + ".jpg");

        final UploadTask uploadTask = filePath.putFile(ImageUri);


        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e)
            {
                String message = e.toString();
                Toast.makeText(getActivity(), "Error: " + message, Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot)
            {
                Toast.makeText(getActivity(), "Course Image uploaded Successfully...", Toast.LENGTH_SHORT).show();

                Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception
                    {
                        if (!task.isSuccessful())
                        {
                            throw task.getException();
                        }
                        downloadImageUrl = filePath.getDownloadUrl().toString();
                        return filePath.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task)
                    {
                        if (task.isSuccessful())
                        {
                            downloadImageUrl = task.getResult().toString();

                            Toast.makeText(getActivity(), "got the Course image Url Successfully...", Toast.LENGTH_SHORT).show();

                            SaveProductInfoToDatabase();
                        }
                    }
                });
            }
        });
    }

    private void SaveProductInfoToDatabase()
    {
        HashMap<String, Object> productMap = new HashMap<>();
        productMap.put("pid", productRandomKey);
        productMap.put("Cimage", downloadImageUrl);
        productMap.put("Cname", name);
        productMap.put("Ccategory", category);
        productMap.put("Cprice", price);
        productMap.put("Cdescription", description);
        productMap.put("Ctype", condition);
//        productMap.put("Cstatus", status);
        productMap.put("posted_by", session);
        productMap.put("CstartDate",tv_start_date.getText().toString());
        productMap.put("CendDate",tv_end_date.getText().toString());

        CoursesRef.child(productRandomKey).updateChildren(productMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task)
                    {
                        if (task.isSuccessful())
                        {
                            Toast.makeText(getActivity(), "Course is added successfully..", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            String message = task.getException().toString();
                            Toast.makeText(getActivity(), "Error: " + message, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logout:
                startActivity(new Intent(getContext(), login.class));
                getActivity().finish();
                return true;
            case R.id.my_profile:
                startActivity(new Intent(getContext(), MyProfileActivity.class));
                return true;
            case R.id.change_pwd:
                startActivity(new Intent(getContext(), ChangePasswordActivity.class));
                return true;


            default:
                break;
        }
        return false;
    }

}
