package com.spk.service;

import com.spk.model.Kriteria;
import java.util.List;

public interface ServiceKriteria {
    
    void insertData(Kriteria model);
    void updateData(Kriteria model);
    void deleteData(Kriteria model);
    
    List<Kriteria> getData();
    List<Kriteria> searchData(String keyword);
    
    String generateKodeKriteria();
    boolean validasiNamaKriteria(Kriteria model);
    
    void updateBobot(int idKriteria, double bobot);
    
}