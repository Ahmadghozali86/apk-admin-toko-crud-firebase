package com.example.adminstore_kelompo_7;

import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    private Context context;
    private List<DaftarMenu> dataList;

    public MyAdapter(Context context, List<DaftarMenu> dataList) {
        this.context = context;
        this.dataList = dataList;
    }

    public int getTotalHarga() {
        int totalHarga = 0;
        if (dataList != null) {
            for (DaftarMenu menu : dataList) {
                totalHarga += menu.getHargaMenu() * menu.getJumlahPesanan();
            }
        }
        return totalHarga;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        DaftarMenu currentItem = dataList.get(position);

        Glide.with(context).load(currentItem.getGambarMenu()).into(holder.gambar);
        holder.nama.setText(currentItem.getNamaMenu());
        holder.harga.setText(String.valueOf(currentItem.getHargaMenu()));

        holder.jumlah.setText(String.valueOf(currentItem.getJumlahPesanan()));

        holder.jumlah.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                int pos = holder.getAdapterPosition();
                if (pos != RecyclerView.NO_POSITION) {
                    int jumlahPesan = 0;
                    try {
                        jumlahPesan = Integer.parseInt(editable.toString());
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                    if (dataList != null && pos < dataList.size()) {
                        dataList.get(pos).setJumlahPesanan(jumlahPesan);
                    }

                    // Hitung kembali total harga setiap kali nilai EditText berubah
                    int totalHarga = 0;
                    if (dataList != null) {
                        for (DaftarMenu menu : dataList) {
                            totalHarga += menu.getHargaMenu() * menu.getJumlahPesanan();
                        }
                    }

                    // Gunakan totalHarga untuk melakukan sesuatu, misalnya memperbarui tampilan
                    // atau melakukan tindakan tertentu yang dibutuhkan berdasarkan total harga yang baru.
                }
            }
        });

        holder.recCard.setOnClickListener(view -> {
            Intent intent = new Intent(context, DetailMenu.class);
            intent.putExtra("gambar", currentItem.getGambarMenu());
            intent.putExtra("nama", currentItem.getNamaMenu());
            intent.putExtra("harga", currentItem.getHargaMenu());
            intent.putExtra("deskripsi", currentItem.getDeskripsiMenu());
            intent.putExtra("jenis", currentItem.getJenisMakanan());
            intent.putExtra("key", currentItem.getKey());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return dataList != null ? dataList.size() : 0;
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView gambar;
        TextView nama, harga;
        EditText jumlah;
        CardView recCard;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            gambar = itemView.findViewById(R.id.gambarMenu);
            recCard = itemView.findViewById(R.id.recCard);
            nama = itemView.findViewById(R.id.namamenu);
            harga = itemView.findViewById(R.id.hargeMenu);
            jumlah = itemView.findViewById(R.id.jumlah);
        }
    }
}
