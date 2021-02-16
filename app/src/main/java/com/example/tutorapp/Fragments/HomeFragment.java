package com.example.tutorapp.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.tutorapp.Activities.CourseActivity;
import com.example.tutorapp.Activities.MyProfileActivity;
import com.example.tutorapp.R;
import com.example.tutorapp.login;


public class HomeFragment extends Fragment implements View.OnClickListener {

    View view;
    ImageView languages_view;
    ImageView technologies_view;
    ImageView programmingLanguages_view;
    ImageView others_view;


    public static Fragment homeFragment() {
        HomeFragment homeFragment = new HomeFragment();
        return homeFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, container, false);
        setHasOptionsMenu(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Home");
        languages_view= view.findViewById(R.id.languages);
        languages_view.setOnClickListener(this);
        technologies_view= view.findViewById(R.id.technologies);
        technologies_view.setOnClickListener(this);
        programmingLanguages_view= view.findViewById(R.id.programming);
        programmingLanguages_view.setOnClickListener(this);
        others_view= view.findViewById(R.id.Others);
        others_view.setOnClickListener(this);
        return view;

    }

    @Override
    public void onClick(View view) {

        Intent tech_intent=new Intent(this.getContext(), CourseActivity.class);
        switch(view.getId()){
            case R.id.languages:

                tech_intent.putExtra("selected","Languages");
                startActivity(tech_intent);
                break;

            case R.id.technologies:
                tech_intent.putExtra("selected", "Technologies");
                startActivity(tech_intent);
                break;
            case R.id.programming:
                tech_intent.putExtra("selected", "Programming languages");
                startActivity(tech_intent);
                break;
            case R.id.Others:
                tech_intent.putExtra("selected", "Others");
                startActivity(tech_intent);
                break;
        }

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

            default:
                break;
        }
        return false;
    }
}