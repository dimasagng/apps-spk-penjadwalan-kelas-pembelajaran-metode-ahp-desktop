package com.spk.dao;

import com.spk.config.Koneksi;
import com.spk.model.PerbandinganSubKriteria;
import com.spk.service.ServicePerbandinganSubKriteria;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class PerbandinganSubKriteriaDAO implements ServicePerbandinganSubKriteria {

    private final Connection conn;

    public PerbandinganSubKriteriaDAO() {
        conn = Koneksi.getConnection();
    }

    @Override
    public void insertData(int idKriteria, int idSubKriteria1, int idSubKriteria2, double nilai) {
        PreparedStatement st = null;
        try {
            String sql = "INSERT INTO perbandingan_sub_kriteria(id_kriteria, id_sub_kriteria_1, id_sub_kriteria_2, nilai) VALUES (?,?,?,?)";
            st = conn.prepareStatement(sql);
            st.setInt(1, idKriteria);
            st.setInt(2, idSubKriteria1);
            st.setInt(3, idSubKriteria2);
            st.setDouble(4, nilai);
            st.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public void updateData(PerbandinganSubKriteria model) {
        PreparedStatement st = null;
        try {
            String sql = "UPDATE perbandingan_sub_kriteria SET nilai=? WHERE id_kriteria=? AND id_sub_kriteria_1=? AND id_sub_kriteria_2=?";
            st = conn.prepareStatement(sql);
            st.setDouble(1, model.getNilai());
            st.setInt(2, model.getIdKriteria());
            st.setInt(3, model.getIdSubKriteria1());
            st.setInt(4, model.getIdSubKriteria2());
            st.executeUpdate();
            st.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteData(int idKriteria) {
        PreparedStatement st = null;
        try {
            String sql = "DELETE FROM perbandingan_sub_kriteria WHERE id_kriteria=?";
            st = conn.prepareStatement(sql);
            st.setInt(1, idKriteria);
            st.executeUpdate();
            st.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Double getNilai(int idKriteria, int idSubKriteria1, int idSubKriteria2) {
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            String sql = "SELECT nilai FROM perbandingan_sub_kriteria WHERE id_kriteria=? AND id_sub_kriteria_1=? AND id_sub_kriteria_2=?";
            st = conn.prepareStatement(sql);
            st.setInt(1, idKriteria);
            st.setInt(2, idSubKriteria1);
            st.setInt(3, idSubKriteria2);
            rs = st.executeQuery();
            if (rs.next()) {
                return rs.getDouble("nilai");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

        @Override
    public Map<String, Double> getAll(int idKriteria) {
        PreparedStatement st = null;
        ResultSet rs = null;
        Map<String, Double> matrix = new HashMap<>();
        try {
            String sql = "SELECT * FROM perbandingan_sub_kriteria WHERE id_kriteria=?";
            st = conn.prepareStatement(sql);
            st.setInt(1, idKriteria);
            rs = st.executeQuery();
            while (rs.next()) {
                int id1 = rs.getInt("id_sub_kriteria_1");
                int id2 = rs.getInt("id_sub_kriteria_2");
                double nilai = rs.getDouble("nilai");
                matrix.put(id1 + "-" + id2, nilai);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return matrix;
    }
}
