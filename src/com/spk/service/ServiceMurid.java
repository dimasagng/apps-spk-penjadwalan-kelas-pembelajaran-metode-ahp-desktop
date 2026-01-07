package com.spk.service;

import com.spk.model.Murid;
import java.util.List;

public interface ServiceMurid {
    
    void insertData(Murid model);
    void updateData(Murid model);
    void deleteData(Murid model);
    
    List<Murid> getData();
    List<Murid> searchData(String keyword);
    
    boolean validasiNamaMurid(Murid model);
    
    String generateKodeMurid();
    
}