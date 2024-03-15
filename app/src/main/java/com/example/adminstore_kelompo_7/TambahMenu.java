package com.example.adminstore_kelompo_7;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.DateFormat;
import java.util.Calendar;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;

public class TambahMenu extends AppCompatActivity {

    ImageView gmbMenu;
    EditText nama, dekripsi, harga;
    Spinner jenis;
    Button addMenu;
    ActivityResultLauncher<Intent> imagePickLauncher;
    String imageURL;
    Uri uri;
    String Jenis_Makanan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_menu);

        gmbMenu = findViewById(R.id.gambarMenu);
        nama = findViewById(R.id.namamenu);
        harga = findViewById(R.id.hargamenu);
        dekripsi = findViewById(R.id.deskripsimenu);
        addMenu = findViewById(R.id.btn_simpan);
        jenis = findViewById(R.id.jenis_menu);

        ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK){
                            Intent data = result.getData();
                            uri = data.getData();
                            gmbMenu.setImageURI(uri);
                        }else {
                            Toast.makeText(TambahMenu.this, "Tidak Ada Gambar Dipilih", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );
        gmbMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent photoPicker = new Intent(Intent.ACTION_PICK);
                photoPicker.setType("image/*");
                activityResultLauncher.launch(photoPicker);
            }
        });
        addMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveData();
            }
        });
        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this,
                R.array.jenis_makanan, android.R.layout.simple_spinner_item);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        jenis.setAdapter(adapter1);

        // Menangani seleksi spinner
        jenis.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long l) {
                Jenis_Makanan = adapterView.getItemAtPosition(pos).toString();
                Toast.makeText(adapterView.getContext(), Jenis_Makanan, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    public void saveData(){

        String jenismakanan = jenis.getSelectedItem().toString();
        StorageReference storageReference; // Deklarasikan di luar blok if-else

        if (jenismakanan.equals("Makanan")) {
            storageReference = FirebaseStorage.getInstance().getReference().child("Makanan").child(uri.getLastPathSegment());
        } else {
            storageReference = FirebaseStorage.getInstance().getReference().child("Minuman").child(uri.getLastPathSegment());
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(TambahMenu.this);
        builder.setCancelable(false);
        builder.setView(R.layout.progress_layout);
        AlertDialog dialog = builder.create();
        dialog.show();

        storageReference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                uriTask.addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()) {
                            Uri urlImage = task.getResult();
                            if (urlImage != null) {
                                imageURL = urlImage.toString();
                                uploadMenu();
                                dialog.dismiss();
                            }
                        } else {
                            // Handle failure to get download URL
                            dialog.dismiss();
                        }
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                dialog.dismiss();
            }
        });
    }
    public void uploadMenu(){

        String Nama_Menu = nama.getText().toString();
        String Harga_Menu_string = harga.getText().toString(); // Mengambil data sebagai string terlebih dahulu
        Integer Harga_Menu = Integer.parseInt(Harga_Menu_string); // Konversi string ke integer
        String Deskripsi_Menus = dekripsi.getText().toString();
        String Jenis_Makanan = jenis.getSelectedItem().toString();

        DaftarMenu daftarMenu = new DaftarMenu(imageURL, Nama_Menu, Harga_Menu, Deskripsi_Menus, Jenis_Makanan);

        String currentDate = DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());


        if (Jenis_Makanan.equals("Makanan")) {
            FirebaseDatabase.getInstance().getReference("Makanan").child(currentDate)
                    .setValue(daftarMenu).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                Toast.makeText(TambahMenu.this, "Saved", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(TambahMenu.this, e.getMessage().toString(), Toast.LENGTH_SHORT).show();

                        }
                    });
        } else {
            FirebaseDatabase.getInstance().getReference("Minuman").child(currentDate)
                    .setValue(daftarMenu).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                Toast.makeText(TambahMenu.this, "Saved", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(TambahMenu.this, e.getMessage().toString(), Toast.LENGTH_SHORT).show();

                        }
                    });
        }
    }
}