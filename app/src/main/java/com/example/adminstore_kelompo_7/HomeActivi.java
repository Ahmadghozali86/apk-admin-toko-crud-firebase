package com.example.adminstore_kelompo_7;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class HomeActivi extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    FragmentMenu fragmentMenu;
    FragmentPetunjuk fragmentPetunjuk;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        fragmentMenu =  new FragmentMenu();
        fragmentPetunjuk = new FragmentPetunjuk();
        bottomNavigationView = findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if(item.getItemId()==R.id.menu_listmenu){
                    getSupportFragmentManager().beginTransaction().replace(R.id.main_frame_layout,fragmentMenu).commit();
                }
                if(item.getItemId()==R.id.menu_petunjuk){
                    getSupportFragmentManager().beginTransaction().replace(R.id.main_frame_layout,fragmentPetunjuk).commit();
                }
                return true;
            }
        });
        bottomNavigationView.setSelectedItemId(R.id.menu_listmenu);
    }
}