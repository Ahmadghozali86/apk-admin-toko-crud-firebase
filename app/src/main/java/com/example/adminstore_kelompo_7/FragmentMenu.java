package com.example.adminstore_kelompo_7;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FragmentMenu extends Fragment {

    // Deklarasi variabel
    RecyclerView recyclerView1, recyclerView2;
    List<DaftarMenu> dataList1, dataList2;
    ValueEventListener eventListener1, eventListener2;
    DatabaseReference databaseReference1, databaseReference2;
    Button buatPesanan;

    public FragmentMenu() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragmentmenu, container, false);

        // Inisialisasi RecyclerView
        recyclerView1 = view.findViewById(R.id.recyclerView1);
        recyclerView2 = view.findViewById(R.id.recyclerView2);

        // GridLayoutManager
        GridLayoutManager gridLayoutManager = new GridLayoutManager(requireContext(), 1);
        recyclerView1.setLayoutManager(gridLayoutManager);
        GridLayoutManager gridLayoutManager2 = new GridLayoutManager(requireContext(), 1);
        recyclerView2.setLayoutManager(gridLayoutManager2);

        // ProgressDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setCancelable(false);
        builder.setView(R.layout.progress_layout);
        AlertDialog dialog = builder.create();
        dialog.show();

        // Inisialisasi ArrayList
        dataList1 = new ArrayList<>();
        dataList2 = new ArrayList<>();

        // Adapter
        MyAdapter adapter1 = new MyAdapter(requireContext(), dataList1);
        recyclerView1.setAdapter(adapter1);
        MyAdapter adapter2 = new MyAdapter(requireContext(), dataList2);
        recyclerView2.setAdapter(adapter2);

        // DatabaseReference
        databaseReference1 = FirebaseDatabase.getInstance().getReference("Makanan");
        databaseReference2 = FirebaseDatabase.getInstance().getReference("Minuman");
        dialog.show();

        // ValueEventListener
        eventListener1 = databaseReference1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                dataList1.clear();
                for (DataSnapshot itemSnapshot : snapshot.getChildren()) {
                    DaftarMenu daftarMenu = itemSnapshot.getValue(DaftarMenu.class);
                    daftarMenu.setKey(itemSnapshot.getKey());
                    dataList1.add(daftarMenu);
                }
                adapter1.notifyDataSetChanged();
                dialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Makanan", "Error: Gagal mengambil data dari Firebase: " + error.getMessage());
                dialog.dismiss();
            }
        });

        eventListener2 = databaseReference2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                dataList2.clear();
                for (DataSnapshot itemSnapshot : snapshot.getChildren()) {
                    DaftarMenu daftarMenu = itemSnapshot.getValue(DaftarMenu.class);
                    daftarMenu.setKey(itemSnapshot.getKey());
                    dataList2.add(daftarMenu);
                }
                adapter2.notifyDataSetChanged();
                dialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Minuman", "Error: Gagal mengambil data dari Firebase: " + error.getMessage());
                dialog.dismiss();
            }
        });

        // Button buatPesanan click listener
        buatPesanan = view.findViewById(R.id.btn_buatpesanan);
        buatPesanan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Pembayaran.class);

                // Mengambil total harga dari adapter
                int totalHarga1 = adapter1.getTotalHarga();
                int totalHarga2 = adapter2.getTotalHarga();

                //Menambahkan total harga ke dalam intent sebagai extra dengan keys yang berbeda
                intent.putExtra("TOTAL_HARGA_1", totalHarga1);
                intent.putExtra("TOTAL_HARGA_2", totalHarga2);

                // Mulai activity dengan intent yang berisi total harga
                startActivity(intent);
            }
        });

        return view;
    }
}
