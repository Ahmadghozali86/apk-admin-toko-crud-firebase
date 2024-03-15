package com.example.adminstore_kelompo_7;

public class DaftarMenu {
    public DaftarMenu(String gambarMenu, String namaMenu, Integer hargaMenu, String deskripsiMenu, String jenisMakanan) {
        this.gambarMenu = gambarMenu;
        this.namaMenu = namaMenu;
        this.hargaMenu = hargaMenu;
        this.deskripsiMenu = deskripsiMenu;
        this.jenisMakanan = jenisMakanan;
    }
    public String gambarMenu;
    public String namaMenu;
    public Integer hargaMenu;
    public String deskripsiMenu;
    public String jenisMakanan;
    private String key;

    public int getJumlahPesanan() {
        return jumlahPesanan;
    }

    public void setJumlahPesanan(int jumlahPesanan) {
        this.jumlahPesanan = jumlahPesanan;
    }

    private int jumlahPesanan;



    public int getJumlah() {
        return jumlah;
    }

    public void setJumlah(int jumlah) {
        this.jumlah = jumlah;
    }

    private int jumlah;
    public String getKey() {
        return key;
    }
    public void setKey(String key) {
        this.key = key;
    }

    public DaftarMenu(){

    }


    public String getGambarMenu() {
        return gambarMenu;
    }

    public void setGambarMenu(String gambarMenu) {
        this.gambarMenu = gambarMenu;
    }

    public String getNamaMenu() {
        return namaMenu;
    }

    public void setNamaMenu(String namaMenu) {
        this.namaMenu = namaMenu;
    }

    public Integer getHargaMenu() {
        return hargaMenu;
    }

    public void setHargaMenu(Integer hargaMenu) {
        this.hargaMenu = hargaMenu;
    }

    public String getDeskripsiMenu() {
        return deskripsiMenu;
    }

    public void setDeskripsiMenu(String deskripsiMenu) {
        this.deskripsiMenu = deskripsiMenu;
    }

    public String getJenisMakanan() {
        return jenisMakanan;
    }

    public void setJenisMakanan(String jenisMakanan) {
        this.jenisMakanan = jenisMakanan;
    }
}
