package com.example.adminstore_kelompo_7;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
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

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class UpdateMenu extends AppCompatActivity {

    ImageView UpdategmbMenu;
    EditText Updatenama, Updatedekripsi, Updateharga;
    Spinner Updatejenis;
    String namaUpdate,jenisUpdate, deskripsiUpdate;
    Integer hargaUpdate;
    Button Update;
    ActivityResultLauncher<Intent> imagePickLauncher;
    String imageUrl;
    String key, oldImageURL;

    Uri uri;
    String Jenis_Makanan;
    //DatabaseReference databaseReference;
    //StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_menu);

        UpdategmbMenu = findViewById(R.id.updategambarMenu);
        Updatenama = findViewById(R.id.updatenamamenu);
        Updateharga = findViewById(R.id.updatehargamenu);
        Updatedekripsi = findViewById(R.id.updatedeskripsimenu);
        Update = findViewById(R.id.btn_update);
        Updatejenis = findViewById(R.id.updatejenis_menu);

        ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK){
                            Intent data = result.getData();
                            uri = data.getData();
                            UpdategmbMenu.setImageURI(uri);
                        } else {
                            Toast.makeText(UpdateMenu.this, "Tidak ada gambar dipilih", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );
        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this,
                R.array.jenis_makanan, android.R.layout.simple_spinner_item);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Updatejenis.setAdapter(adapter1);

        // Menangani seleksi spinner
        Updatejenis.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long l) {
                Jenis_Makanan = adapterView.getItemAtPosition(pos).toString();
                Toast.makeText(adapterView.getContext(), Jenis_Makanan, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            Glide.with(UpdateMenu.this).load(bundle.getString("gambar")).into(UpdategmbMenu);
            Updatenama.setText(bundle.getString("nama"));
            Updateharga.setText(bundle.getString("harga"));
            Updatedekripsi.setText(bundle.getString("deskripsi"));
            String dataJenis = bundle.getString("jenis");
            int posisi = adapter1.getPosition(dataJenis);
            Updatejenis.setSelection(posisi);
            key = bundle.getString("Key");
            oldImageURL = bundle.getString("gambar");
        }


        UpdategmbMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent photoPicker = new Intent(Intent.ACTION_PICK);
                photoPicker.setType("image/*");
                activityResultLauncher.launch(photoPicker);
            }
        });
        Update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveData();
                Intent intent = new Intent(UpdateMenu.this, HomeActivi.class);
                startActivity(intent);
            }
        });
    }

    public void saveData(){
        String jenismakanan = Updatejenis.getSelectedItem().toString();
        StorageReference storageReference;

        if (jenismakanan.equals("Makanan")) {
            storageReference = FirebaseStorage.getInstance().getReference().child("Makanan").child(uri.getLastPathSegment());
        } else {
            storageReference = FirebaseStorage.getInstance().getReference().child("Minuman").child(uri.getLastPathSegment());
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(UpdateMenu.this);
        builder.setCancelable(false);
        builder.setView(R.layout.progress_layout);
        AlertDialog dialog = builder.create();
        dialog.show();
        storageReference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                while (!uriTask.isComplete());
                Uri urlImage = uriTask.getResult();
                imageUrl = urlImage.toString();
                updateData();
                dialog.dismiss();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                dialog.dismiss();
            }
        });
    }

    public void updateData(){

        namaUpdate = Updatenama.getText().toString().trim();
        String Harga_Menu_string = Updateharga.getText().toString(); // Mengambil data sebagai string terlebih dahulu
        hargaUpdate = Integer.parseInt(Harga_Menu_string);
        jenisUpdate = Updatejenis.getSelectedItem().toString().trim();
        deskripsiUpdate = Updatedekripsi.getText().toString().trim();

        DaftarMenu UpdateDaftarMenu = new DaftarMenu(imageUrl, namaUpdate, hargaUpdate, deskripsiUpdate,jenisUpdate);

        String jenismakanan = Updatejenis.getSelectedItem().toString();
        DatabaseReference databaseReference;
        if (jenismakanan.equals("Makanan")) {
            databaseReference = FirebaseDatabase.getInstance().getReference("Makanan").child(key);
        } else {
            databaseReference = FirebaseDatabase.getInstance().getReference("Minuman").child(key);
        }

        databaseReference.setValue(UpdateDaftarMenu).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    StorageReference reference = FirebaseStorage.getInstance().getReferenceFromUrl(oldImageURL);
                    reference.delete();
                    Toast.makeText(UpdateMenu.this, "Telah di Update", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(UpdateMenu.this, e.getMessage().toString(), Toast.LENGTH_SHORT).show();
            }
        });

    }


}