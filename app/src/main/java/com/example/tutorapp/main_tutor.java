package com.example.tutorapp;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class main_tutor extends AppCompatActivity {


    BottomNavigationView bottomNavigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_tutor);
        bottomNavigation();
    }
    private void bottomNavigation() {
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.navigation);

        bottomNavigationView.setSelectedItemId(R.id.action_home);
        bottomNavigationView.setOnNavigationItemSelectedListener
                (new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        Fragment selectedFragment = null;


                        switch (item.getItemId()) {

                            case R.id.action_home:
                                selectedFragment = com.example.tutorapp.Fragments.HomeFragment.homeFragment();
                                break;
                            case R.id.action_add:
                                selectedFragment = com.example.tutorapp.Fragments.AddFragment.addFragment();
                                break;
                            case R.id.action_search:
                                selectedFragment = com.example.tutorapp.Fragments.searchFragment.searchFragment();
                                break;
                            case R.id.action_history:
                                selectedFragment = com.example.tutorapp.Fragments.HistoryFragment.historyFragment();
                                break;
                        }
                        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                        transaction.replace(R.id.frame_layout, selectedFragment);
                        transaction.commit();
                        return true;
                    }
                });
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout,  com.example.tutorapp.Fragments.HomeFragment.homeFragment());
        transaction.commit();
    }

}