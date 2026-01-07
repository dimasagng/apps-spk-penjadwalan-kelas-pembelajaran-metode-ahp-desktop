package com.spk.dao;

import com.spk.config.Koneksi;
import com.spk.model.PerbandinganKriteria;
import com.spk.service.ServicePerbandinganKriteria;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class PerbandinganKriteriaDAO implements ServicePerbandinganKriteria {

    private final Connection conn;

    public PerbandinganKriteriaDAO() {
        conn = Koneksi.getConnection();
    }

    @Override
    public void insertData(int idKriteria1, int idKriteria2, double nilai) {
         PreparedStatement st = null;

        try {
            String sql = "INSERT INTO perbandingan_kriteria(id_kriteria_1, id_kriteria_2, nilai) VALUES (?,?,?)";
            st = conn.prepareStatement(sql);

            st.setInt(1,idKriteria1);
            st.setInt(2, idKriteria2);
            st.setDouble(3, nilai);

            st.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public void updateData(PerbandinganKriteria model) {
        PreparedStatement st = null;
            try {
                String sql = "UPDATE perbandingan_kriteria SET nilai=? WHERE id_kriteria_1=? AND id_kriteria_2=?";
                st = conn.prepareStatement(sql);

                st.setDouble(1, model.getNilai());
                st.setInt(2, model.getIdKriteria1());
                st.setInt(3, model.getIdKriteria2());

            st.executeUpdate();
            st.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
}

    @Override
    public void deleteData() {
               PreparedStatement st = null;

        try {
            String sql = "DELETE FROM perbandingan_kriteria";
            st = conn.prepareStatement(sql);
            st.executeUpdate();
            st.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Double getNilai(int idKriteria1, int idKriteria2) {
       PreparedStatement st = null;
        ResultSet rs = null;
        try {
            String sql = "SELECT nilai FROM perbandingan_kriteria WHERE id_kriteria_1=? AND id_kriteria_2=?";
            st = conn.prepareStatement(sql);
            st.setInt(1, idKriteria1);
            st.setInt(2, idKriteria2);
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
        public Map<String, Double> getAll() {
            PreparedStatement st = null;
            ResultSet rs = null;
            Map<String, Double> matrix = new HashMap<>();
            try {
                String sql = "SELECT id, id_kriteria_1, id_kriteria_2, nilai FROM perbandingan_kriteria";
                st = conn.prepareStatement(sql);
                rs = st.executeQuery();

                while (rs.next()) {
                    int id1 = rs.getInt("id_kriteria_1");
                    int id2 = rs.getInt("id_kriteria_2");
                    double nilai = rs.getDouble("nilai");
                    matrix.put(id1 + "-" + id2, nilai);
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }

            return matrix;
        }
    }
