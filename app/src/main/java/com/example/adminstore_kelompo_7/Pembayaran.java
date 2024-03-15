package com.example.adminstore_kelompo_7;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class Pembayaran extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pembayaran);

        TextView harga1 = findViewById(R.id.textViewTotalHarga1);
        TextView harga2 = findViewById(R.id.textViewTotalHarga2);
        TextView hargabayar = findViewById(R.id.hargabayar);
        EditText saldoPembelian = findViewById(R.id.uangditerima);
        TextView saldoKembalian = findViewById(R.id.kembalian);
        Button hitungKembalian = findViewById(R.id.uangKembali);
        Button buatlagi = findViewById(R.id.buatLagi);
        TextView backToHome = findViewById(R.id.menu);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null){
            int totalHarga1 = bundle.getInt("TOTAL_HARGA_1");
            int totalHarga2 = bundle.getInt("TOTAL_HARGA_2");

            harga1.setText(String.valueOf(totalHarga1));
            harga2.setText(String.valueOf(totalHarga2));

            int hargajual = totalHarga1 + totalHarga2;
            hargabayar.setText(String.valueOf(hargajual));

            hitungKembalian.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String SaldoPembelian = saldoPembelian.getText().toString();
                    int uang = Integer.parseInt(SaldoPembelian);
                    int kembalian = uang-hargajual;

                    saldoKembalian.setText(String.valueOf(kembalian));

                }
            });
        }

        backToHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Pembayaran.this, MainActivity.class);
                startActivity(intent);
            }
        });
        buatlagi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Pembayaran.this, HomeActivi.class);
                startActivity(intent);
            }
        });

    }
}