package com.example.adminstore_kelompo_7;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class DetailMenu extends AppCompatActivity {

    TextView detailNamaMenu, detaiHargaMenu, detailJenisMenu, detailDeskripsiMenu;
    ImageView detailGambarMenu;

    String key = "";
    String imageUrl = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_menu);

        detailNamaMenu = findViewById(R.id.menuku);
        detailGambarMenu = findViewById(R.id.gambarku);
        detaiHargaMenu= findViewById(R.id.hargaku);
        detailJenisMenu= findViewById(R.id.jenisku);
        detailDeskripsiMenu= findViewById(R.id.deskripsiku);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null){
            detailNamaMenu.setText(bundle.getString("nama"));
            int harga = bundle.getInt("harga", 0);
            Log.d("DetailMenu", "Harga dari bundle: " + harga); // Tambahkan log ini
            detaiHargaMenu.setText(String.valueOf(harga)); // Menetapkan integer ke TextView sebagai String
            detailJenisMenu.setText(bundle.getString("jenis"));
            detailDeskripsiMenu.setText(bundle.getString("deskripsi"));
            key = bundle.getString("key");
            imageUrl = bundle.getString("gambar");
            Log.d("Detail Menu", "URL Gambar: " + imageUrl);
            Glide.with(this).load(bundle.getString("gambar")).into(detailGambarMenu);
        }
    }
    public void UpdateMenu(View view) {
        Intent intent = new Intent(DetailMenu.this, UpdateMenu.class)
                .putExtra("nama", detailNamaMenu.getText().toString())
                .putExtra("harga", detaiHargaMenu.getText().toString())
                .putExtra("jenis", detailJenisMenu.getText().toString())
                .putExtra("deskripsi", detailDeskripsiMenu.getText().toString())
                .putExtra("gambar", imageUrl)
                .putExtra("Key", key);
        startActivity(intent);
    }

    public void HapusMenu(View view) {

        String jenisMenu = detailJenisMenu.getText().toString();

        if (jenisMenu.equals("Makanan")){
            final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Makanan");
            FirebaseStorage storage = FirebaseStorage.getInstance();

            StorageReference storageReference = storage.getReferenceFromUrl(imageUrl);
            storageReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    reference.child(key).removeValue();
                    Toast.makeText(DetailMenu.this, "Delete", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(), HomeActivi.class));
                    finish();
                }
            });

        }else if(jenisMenu.equals("Minuman")) {
            final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Minuman");
            FirebaseStorage storage = FirebaseStorage.getInstance();

            StorageReference storageReference = storage.getReferenceFromUrl(imageUrl);
            storageReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    reference.child(key).removeValue();
                    Toast.makeText(DetailMenu.this, "Delete", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(), HomeActivi.class));
                    finish();
                }
            });
        }else  {
            Toast.makeText(DetailMenu.this, "Error Bang", Toast.LENGTH_SHORT).show();
        }
    }
}