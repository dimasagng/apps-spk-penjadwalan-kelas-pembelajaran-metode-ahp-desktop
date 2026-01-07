package com.spk.dao;

import com.spk.config.Koneksi;
import com.spk.model.Kriteria;
import com.spk.model.SubKriteria;
import com.spk.service.ServiceSubKriteria;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SubKriteriaDAO implements ServiceSubKriteria {

    private final Connection conn;

    public SubKriteriaDAO() {
        conn = Koneksi.getConnection();
    }

    @Override
    public void insertData(SubKriteria model) {
        PreparedStatement st = null;
        try {
            String sql = "INSERT INTO sub_kriteria(id_kriteria, kode_sub_kriteria, nama_sub_kriteria) VALUES (?,?,?)";
            st = conn.prepareStatement(sql);
            st.setInt(1, model.getIdKriteria());
            st.setString(2, model.getKodeSubKriteria());
            st.setString(3, model.getNamaSubKriteria());
            st.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateData(SubKriteria model) {
        PreparedStatement st = null;
        try {
            String sql = "UPDATE sub_kriteria SET id_kriteria=?, kode_sub_kriteria=?, nama_sub_kriteria=? WHERE id_sub_kriteria=?";
            st = conn.prepareStatement(sql);
            st.setInt(1, model.getIdKriteria());
            st.setString(2, model.getKodeSubKriteria());
            st.setString(3, model.getNamaSubKriteria());
            st.setInt(4, model.getIdSubKriteria());
            st.executeUpdate();
            st.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteData(SubKriteria model) {
        PreparedStatement st = null;
        try {
            String sql = "DELETE FROM sub_kriteria WHERE id_sub_kriteria=?";
            st = conn.prepareStatement(sql);
            st.setInt(1, model.getIdSubKriteria());
            st.executeUpdate();
            st.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<SubKriteria> getData() {
        PreparedStatement st = null;
        ResultSet rs = null;
        List<SubKriteria> list = new ArrayList<>();
        try {
            // Mengambil data dan mengurutkan berdasarkan Kode agar rapi di tabel
            String sql = "SELECT sk.*, k.nama_kriteria "
                       + "FROM sub_kriteria sk "
                       + "JOIN kriteria k ON sk.id_kriteria = k.id_kriteria "
                       + "ORDER BY sk.kode_sub_kriteria ASC"; 
            st = conn.prepareStatement(sql);
            rs = st.executeQuery();
            while (rs.next()) {
                SubKriteria model = new SubKriteria();
                model.setIdSubKriteria(rs.getInt("id_sub_kriteria"));
                model.setIdKriteria(rs.getInt("id_kriteria"));
                model.setKodeSubKriteria(rs.getString("kode_sub_kriteria"));
                model.setNamaSubKriteria(rs.getString("nama_sub_kriteria"));
                model.setNamaKriteriaInduk(rs.getString("nama_kriteria"));
                list.add(model);
            }
            rs.close();
            st.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
    
    @Override
    public List<SubKriteria> getDataByKriteria(int idKriteria) {
        PreparedStatement st = null;
        ResultSet rs = null;
        List<SubKriteria> list = new ArrayList<>();
        try {
            String sql = "SELECT * FROM sub_kriteria WHERE id_kriteria = ?";
            st = conn.prepareStatement(sql);
            st.setInt(1, idKriteria);
            rs = st.executeQuery();
            while (rs.next()) {
                SubKriteria model = new SubKriteria();
                model.setIdSubKriteria(rs.getInt("id_sub_kriteria"));
                model.setIdKriteria(rs.getInt("id_kriteria"));
                model.setKodeSubKriteria(rs.getString("kode_sub_kriteria"));
                model.setNamaSubKriteria(rs.getString("nama_sub_kriteria"));
                list.add(model);
            }
            rs.close();
            st.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public List<SubKriteria> searchData(String keyword) {
        PreparedStatement st = null;
        ResultSet rs = null;
        List<SubKriteria> list = new ArrayList<>();
        try {
            String sql = "SELECT sk.*, k.nama_kriteria "
                       + "FROM sub_kriteria sk "
                       + "JOIN kriteria k ON sk.id_kriteria = k.id_kriteria "
                       + "WHERE sk.kode_sub_kriteria LIKE ? OR sk.nama_sub_kriteria LIKE ? OR k.nama_kriteria LIKE ?";
            st = conn.prepareStatement(sql);
            st.setString(1, "%" + keyword + "%");
            st.setString(2, "%" + keyword + "%");
            st.setString(3, "%" + keyword + "%");
            rs = st.executeQuery();
            while (rs.next()) {
                SubKriteria model = new SubKriteria();
                model.setIdSubKriteria(rs.getInt("id_sub_kriteria"));
                model.setIdKriteria(rs.getInt("id_kriteria"));
                model.setKodeSubKriteria(rs.getString("kode_sub_kriteria"));
                model.setNamaSubKriteria(rs.getString("nama_sub_kriteria"));
                model.setNamaKriteriaInduk(rs.getString("nama_kriteria"));
                list.add(model);
            }
            rs.close();
            st.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public String generateKodeSubKriteria() {
        PreparedStatement st = null;
        ResultSet rs = null;
        String kode = "S01"; // Default jika tabel kosong
        try {
           // LOGIC GLOBAL SEQUENCE:
            // Mengambil angka terbesar dari seluruh tabel sub_kriteria (tanpa where kriteria)
            // COALESCE(..., 0) menangani jika tabel kosong, maka max_kode = 0
            String sql = "SELECT COALESCE(MAX(CAST(SUBSTRING(kode_sub_kriteria, 2) AS UNSIGNED)), 0) as max_kode FROM sub_kriteria";
            
            st = conn.prepareStatement(sql);
            rs = st.executeQuery();

            if (rs.next()) {
                int lastValue = rs.getInt("max_kode");
                int nextValue = lastValue + 1;

                if (nextValue < 10) {
                    kode = "S0" + nextValue; 
                } else {
                    kode = "S" + nextValue; 
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return kode;
    }

    @Override
    public boolean validasiNamaSubKriteria(SubKriteria model) {
        PreparedStatement st = null;
        ResultSet rs = null;
        boolean valid = false;
        try {
            String sql = "SELECT nama_sub_kriteria FROM sub_kriteria WHERE nama_sub_kriteria LIKE BINARY ?";
            st = conn.prepareStatement(sql);
            st.setString(1, model.getNamaSubKriteria());
            rs = st.executeQuery();
            valid = !rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return valid;
    }

    @Override
    public void updateBobot(int idSubKriteria, double bobot) {
        PreparedStatement st = null;
        try {
            String sql = "UPDATE sub_kriteria SET bobot=? WHERE id_sub_kriteria=?";
            st = conn.prepareStatement(sql);
            st.setDouble(1, bobot);
            st.setInt(2, idSubKriteria);
            st.executeUpdate();
            st.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}