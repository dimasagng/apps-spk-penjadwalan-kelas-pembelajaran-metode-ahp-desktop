package com.spk.service;

public interface ServicePerbandinganAlternatif {

    void deleteByKriteria(int idKriteria);
    void insertData(int idKriteria, int idMurid1, int idMurid2, double nilai);
    double getNilai(int idKriteria, int idMurid1, int idMurid2);

    void deleteBobotByKriteria(int idKriteria);
    void insertBobot(int idKriteria, int idMurid, double bobot);
    
}