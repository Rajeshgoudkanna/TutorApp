package com.example.tutorapp.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.tutorapp.Activities.Utils;
import com.example.tutorapp.Adapters.HistoryAdapter;
import com.example.tutorapp.R;
import com.example.tutorapp.model.Course;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FavoritesFragment extends Fragment {

    GridView gridview;
    List<Course> a1;
    SharedPreferences sharedPreferences;
    String session;
    HistoryAdapter historyAdapter;
    View view;

    public static FavoritesFragment favoriteFragment() {
        FavoritesFragment fragment = new FavoritesFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        view = inflater.inflate(R.layout.fragment_history, container, false);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Favorite Courses");
        sharedPreferences =getActivity().getSharedPreferences(Utils.SHREF, Context.MODE_PRIVATE);
        session = sharedPreferences.getString("user_name", "def-val");
        a1= new ArrayList<>();
        gridview=(GridView)view.findViewById(R.id.gridview);
        Query query = FirebaseDatabase.getInstance().getReference("Favorite Courses").child(session);
        query.addListenerForSingleValueEvent(valueEventListener);
        return view;
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
                historyAdapter = new HistoryAdapter(a1,getActivity());
                gridview.setAdapter(historyAdapter);
            }
            else {
                Toast.makeText(getContext(), "No data Found", Toast.LENGTH_SHORT).show();
            }
        }
        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };

}