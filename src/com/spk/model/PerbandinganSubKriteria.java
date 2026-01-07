package com.spk.model;


public class PerbandinganSubKriteria {
    
    private int id;
    private int idKriteria; // Kriteria Induk
    private int idSubKriteria1;
    private int idSubKriteria2;
    private double nilai;

    public int getId() {
        return id;
    }

    public int getIdKriteria() {
        return idKriteria;
    }

    public void setIdKriteria(int idKriteria) {
        this.idKriteria = idKriteria;
    }

    public int getIdSubKriteria1() {
        return idSubKriteria1;
    }

    public void setIdSubKriteria1(int idSubKriteria1) {
        this.idSubKriteria1 = idSubKriteria1;
    }

    public int getIdSubKriteria2() {
        return idSubKriteria2;
    }

    public void setIdSubKriteria2(int idSubKriteria2) {
        this.idSubKriteria2 = idSubKriteria2;
    }

    public double getNilai() {
        return nilai;
    }

    public void setNilai(double nilai) {
        this.nilai = nilai;
    }
}
