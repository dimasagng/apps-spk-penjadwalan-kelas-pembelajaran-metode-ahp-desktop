package com.spk.service;

import com.spk.model.PerbandinganSubKriteria;
import java.util.Map;

public interface ServicePerbandinganSubKriteria {
    
    // idKriteria = Kriteria Induk
    void insertData(int idKriteria, int idSubKriteria1, int idSubKriteria2, double nilai);
    void updateData(PerbandinganSubKriteria model); // (Model sekarang punya idKriteria)
    void deleteData(int idKriteria); // Hapus berdasarkan Kriteria Induk
    
    Double getNilai(int idKriteria, int idSubKriteria1, int idSubKriteria2);
    Map<String, Double> getAll(int idKriteria);
}