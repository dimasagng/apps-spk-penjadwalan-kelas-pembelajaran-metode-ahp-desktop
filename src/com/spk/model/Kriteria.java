package com.spk.model;


public class Kriteria {
    
    private int idKriteria;
    private String kodeKriteria;
    private String namaKriteria;
    private double bobot;
    
    public Kriteria() {
    }
    
    public Kriteria(int idKriteria, String namaKriteria){
        this.idKriteria = idKriteria;
        this.namaKriteria = namaKriteria;
    }

    public int getIdKriteria() {
        return idKriteria;
    }

    public void setIdKriteria(int idKriteria) {
        this.idKriteria = idKriteria;
    }

    public String getKodeKriteria() {
        return kodeKriteria;
    }

    public void setKodeKriteria(String kodeKriteria) {
        this.kodeKriteria = kodeKriteria;
    }

    public String getNamaKriteria() {
        return namaKriteria;
    }

    @Override
    public String toString() {
        return namaKriteria;
    }

    public void setNamaKriteria(String namaKriteria) {
        this.namaKriteria = namaKriteria;
    }

    public double getBobot() {
        return bobot;
    }

    public void setBobot(double bobot) {
        this.bobot = bobot;
    }
    
}
