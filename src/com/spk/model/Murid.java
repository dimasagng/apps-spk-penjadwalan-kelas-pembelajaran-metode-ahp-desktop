package com.spk.model;

public class Murid {
    
    private int idMurid;  
    private String namaMurid; 
    private String kodeMurid;
    private String jumlahMurid;
    private String jenisProgram;
    private String pengajar;
    private String preferensiWaktu;
    private String mediaPembelajaran;

    public int getIdMurid() { 
        return idMurid;
    }

    public void setIdMurid(int idMurid) { 
        this.idMurid = idMurid;
    }

    public String getKodeMurid() {
        return kodeMurid;
    }

    public void setKodeMurid(String kodeMurid) {
        this.kodeMurid = kodeMurid;
    }

    public String getNamaMurid() { 
        return namaMurid;
    }

    public void setNamaMurid(String namaMurid) {
        this.namaMurid = namaMurid;
    }

    public String getJumlahMurid() {
        return jumlahMurid;
    }

    public void setJumlahMurid(String jumlahMurid) {
        this.jumlahMurid = jumlahMurid;
    }

    public String getJenisProgram() {
        return jenisProgram;
    }

    public void setJenisProgram(String jenisProgram) {
        this.jenisProgram = jenisProgram;
    }
    
    public String getPengajar() {
        return pengajar;
    }

    public void setPengajar(String pengajar) {
        this.pengajar = pengajar;
    }

    public String getPreferensiWaktu() {
        return preferensiWaktu;
    }

    public void setPreferensiWaktu(String preferensiWaktu) {
        this.preferensiWaktu = preferensiWaktu;
    }

    public String getMediaPembelajaran() {
        return mediaPembelajaran;
    }

    public void setMediaPembelajaran(String mediaPembelajaran) {
        this.mediaPembelajaran = mediaPembelajaran;
    }
}