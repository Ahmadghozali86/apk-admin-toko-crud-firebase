package com.example.adminstore_kelompo_7;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView logoutTextView = findViewById(R.id.tv_logout);
        SharedPreferences sharedPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);


        logoutTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Menghapus status login dari SharedPreferences
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.remove("isLoggedIn");
                editor.apply();

                // Redirect ke ActivityLogin setelah logout
                Intent intent = new Intent(MainActivity.this, LoginAdmin.class);
                startActivity(intent);
                finish(); // Menutup Activity saat ini agar tidak kembali ke sini setelah logout
            }
        });
    }

    public void buatPesanan(View view) {
        Intent intent = new Intent(MainActivity.this, HomeActivi.class);
        startActivity(intent);

    }

    public void tambahMenu(View view) {
        Intent intent = new Intent(MainActivity .this, TambahMenu.class);
        startActivity(intent);
    }
}