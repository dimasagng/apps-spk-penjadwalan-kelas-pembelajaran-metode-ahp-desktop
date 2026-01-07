package com.spk.model;


public class SubKriteria {
    
    private int idSubKriteria;
    private int idKriteria;
    private String kodeSubKriteria;
    private String namaSubKriteria;
    private double bobot;
    
    // Dipakai untuk join di table model
    private String namaKriteriaInduk; 

    public SubKriteria() {
    }
    
    // Konstruktor untuk ComboBox
    public SubKriteria(int idSubKriteria, String namaSubKriteria){
        this.idSubKriteria = idSubKriteria;
        this.namaSubKriteria = namaSubKriteria;
    }

    public int getIdSubKriteria() {
        return idSubKriteria;
    }

    public void setIdSubKriteria(int idSubKriteria) {
        this.idSubKriteria = idSubKriteria;
    }

    public int getIdKriteria() {
        return idKriteria;
    }

    public void setIdKriteria(int idKriteria) {
        this.idKriteria = idKriteria;
    }

    public String getKodeSubKriteria() {
        return kodeSubKriteria;
    }

    public void setKodeSubKriteria(String kodeSubKriteria) {
        this.kodeSubKriteria = kodeSubKriteria;
    }

    public String getNamaSubKriteria() {
        return namaSubKriteria;
    }

    public void setNamaSubKriteria(String namaSubKriteria) {
        this.namaSubKriteria = namaSubKriteria;
    }

    public double getBobot() {
        return bobot;
    }

    public void setBobot(double bobot) {
        this.bobot = bobot;
    }

    public String getNamaKriteriaInduk() {
        return namaKriteriaInduk;
    }

    public void setNamaKriteriaInduk(String namaKriteriaInduk) {
        this.namaKriteriaInduk = namaKriteriaInduk;
    }
    
    @Override
    public String toString() {
        // Digunakan oleh ComboBox
        return namaSubKriteria;
    }
}
