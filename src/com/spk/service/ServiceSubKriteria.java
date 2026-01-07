package com.spk.service;

import com.spk.model.SubKriteria;
import java.util.List;

public interface ServiceSubKriteria {
    
    void insertData(SubKriteria model);
    void updateData(SubKriteria model);
    void deleteData(SubKriteria model);
    
    List<SubKriteria> getData(); // Dapat semua subkriteria (untuk FormMaster)
    List<SubKriteria> getDataByKriteria(int idKriteria); // Dapat anak dari kriteria (untuk FormPerbandingan)
    List<SubKriteria> searchData(String keyword);
    
    String generateKodeSubKriteria();
    boolean validasiNamaSubKriteria(SubKriteria model);
    
    void updateBobot(int idSubKriteria, double bobot);
}