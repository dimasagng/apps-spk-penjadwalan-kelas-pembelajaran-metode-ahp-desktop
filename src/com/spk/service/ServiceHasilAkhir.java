package com.spk.service;

import java.util.Map;

public interface ServiceHasilAkhir {

    Map<Integer, Double> getBobotKriteria();
    double getBobotAlternatif(int idKriteria, int idMurid);

    void deleteHasilAkhir();
    void insertHasilAkhir(int idMurid, double total);
    
}