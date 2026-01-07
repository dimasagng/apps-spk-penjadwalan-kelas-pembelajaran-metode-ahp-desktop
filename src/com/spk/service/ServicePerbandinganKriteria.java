package com.spk.service;

import com.spk.model.PerbandinganKriteria;
import java.util.Map;

public interface ServicePerbandinganKriteria {
    
    void insertData(int idKriteria1, int idKriteria2, double nilai);
    void updateData(PerbandinganKriteria model);
    void deleteData();
    
    Double getNilai(int idKriteria1, int idKriteria2);
    Map<String, Double> getAll();
    
}