package com.example.tutorapp.Fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import com.example.tutorapp.Activities.ChangePasswordActivity;
import com.example.tutorapp.Activities.MyProfileActivity;
import com.example.tutorapp.Adapters.SearchAdapter;
import com.example.tutorapp.R;
import com.example.tutorapp.login;
import com.example.tutorapp.model.Course;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class searchFragment extends Fragment {

    GridView gridview;
    List<Course> a1;
    ProgressDialog progressDialog;
    SearchAdapter searchAdapter;
    EditText et_search;
    View view;
    public static searchFragment searchFragment() {
        searchFragment fragment = new searchFragment();
        return fragment;
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        view = inflater.inflate(R.layout.fragment_search, container, false);
        setHasOptionsMenu(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Search");
        progressDialog=new ProgressDialog(getContext());


        gridview=(GridView)view.findViewById(R.id.gridview);
        a1= new ArrayList<>();
        Query query = FirebaseDatabase.getInstance().getReference("Courses");
        query.addListenerForSingleValueEvent(valueEventListener);

        et_search = (EditText) view.findViewById(R.id.et_search);
        et_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub
                String text = et_search.getText().toString().toLowerCase(Locale.getDefault());
                searchAdapter.filter(text);
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
                // TODO Auto-generated method stub
            }
        });




        return view;
    }

    ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            progressDialog.dismiss();
            a1.clear();
            if (dataSnapshot.exists()) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Course homeDataPojo = snapshot.getValue(Course.class);
                    a1.add(homeDataPojo);
                }
                searchAdapter = new SearchAdapter(a1, getActivity());
                gridview.setAdapter(searchAdapter);

            } else {
                Toast.makeText(getContext(), "No data Found", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
            progressDialog.dismiss();

        }
    };

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