package com.spk.model;

import java.util.Map;

public class HasilAkhir {
    
    private String namaMurid;
    private Map<String, Double> nilaiKriteria;
    private double total;
    private int ranking;
    
        public HasilAkhir(String namaMurid, Map<String, Double> nilaiKriteria, double total, int ranking) {
        this.namaMurid = namaMurid;
        this.nilaiKriteria = nilaiKriteria;
        this.total = total;
        this.ranking = ranking;
    }

    public String getNamaMurid() {
        return namaMurid;
    }

    public void setNamaMurid(String namaMurid) {
        this.namaMurid = namaMurid;
    }

    public Map<String, Double> getNilaiKriteria() {
        return nilaiKriteria;
    }

    public void setNilaiKriteria(Map<String, Double> nilaiKriteria) {
        this.nilaiKriteria = nilaiKriteria;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public int getRanking() {
        return ranking;
    }

    public void setRanking(int ranking) {
        this.ranking = ranking;
    }

}
