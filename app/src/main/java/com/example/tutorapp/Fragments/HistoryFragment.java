package com.example.tutorapp.Fragments;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.tutorapp.Activities.ChangePasswordActivity;
import com.example.tutorapp.Activities.MyProfileActivity;
import com.example.tutorapp.Activities.StudentRating;
import com.example.tutorapp.Activities.TutorViewRating;
import com.example.tutorapp.Activities.Utils;
import com.example.tutorapp.Adapters.HistoryAdapter;
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

public class HistoryFragment extends Fragment {
    GridView gridview;
    List<Course> a1;
    SharedPreferences sharedPreferences;
    String session;
    String user_role;
    HistoryAdapter historyAdapter;
    View view;

    public static HistoryFragment historyFragment() {
        HistoryFragment fragment = new HistoryFragment();
        return fragment;
    }
    ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            a1.clear();
            if (dataSnapshot.exists()) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Course homeDataPojo = snapshot.getValue(Course.class);
                    a1.add(homeDataPojo);
                }
                historyAdapter = new HistoryAdapter(a1, getActivity());
                gridview.setAdapter(historyAdapter);
            } else {
                Toast.makeText(getContext(), "No data Found", Toast.LENGTH_SHORT).show();
            }
        }
        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        view = inflater.inflate(R.layout.fragment_history, container, false);
        setHasOptionsMenu(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("History");
        sharedPreferences = getActivity().getSharedPreferences(Utils.SHREF, Context.MODE_PRIVATE);
        session = sharedPreferences.getString("user_name", "def-val");
        user_role = sharedPreferences.getString("user_role", "def-role");
        a1 = new ArrayList<>();
        gridview = (GridView) view.findViewById(R.id.gridview);
        Query query = null;
        if (user_role.equals("Student")) {
            query = FirebaseDatabase.getInstance().getReference("Enrolled Courses").child(session);
        }
        if (user_role.equals("Tutor")) {
            query = FirebaseDatabase.getInstance().getReference("Courses").orderByChild("posted_by").equalTo(session);
        }

        query.addListenerForSingleValueEvent(valueEventListener);

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {

                String selected_item = ((TextView) v.findViewById(R.id.tv_course_id)).getText().toString();

                if (user_role.equals("Student")) {
                    Intent student_intent = new Intent(getActivity(), StudentRating.class);
                    student_intent.putExtra("selectedItem", selected_item);
                    startActivity(student_intent);
                }
                if (user_role.equals("Tutor")) {

                    Intent tutor_intent = new Intent(getActivity(), TutorViewRating.class);
                    tutor_intent.putExtra("selectedItem", selected_item);
                    view.getContext().startActivity(tutor_intent);
                }
            }
        });
        return view;
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
