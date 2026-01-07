package com.spk.dao;

import com.spk.config.Koneksi;
import com.spk.service.ServiceHasilAkhir;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HasilAkhirDAO implements ServiceHasilAkhir {

    private final Connection conn;

    public HasilAkhirDAO() {
        conn = Koneksi.getConnection();
    }

    @Override
    public Map<Integer, Double> getBobotKriteria() {
          PreparedStatement st = null;
          ResultSet rs = null;
          Map<Integer, Double> map = new HashMap<>();

          try {
              String sql = "SELECT id_kriteria, bobot FROM kriteria";
              st = conn.prepareStatement(sql);
              rs = st.executeQuery();

              while (rs.next()) {
                  int idKriteria = rs.getInt("id_kriteria");
                  double bobot = rs.getDouble("bobot");
                  map.put(idKriteria, bobot);
              }
          } catch (SQLException e) {
              e.printStackTrace();
          }

          return map;
      }

    @Override
    public double getBobotAlternatif(int idKriteria, int idMurid) {
        PreparedStatement st = null;
        ResultSet rs = null;
        double bobot = 0;

        try {
            String sql = "SELECT bobot FROM bobot_alternatif WHERE id_kriteria = ? AND id_murid = ?";
            st = conn.prepareStatement(sql);
            st.setInt(1, idKriteria);
            st.setInt(2, idMurid);
            rs = st.executeQuery();

            if (rs.next()) {
                bobot = rs.getDouble("bobot");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return bobot;
    
    }

    @Override
    public void deleteHasilAkhir() {
        PreparedStatement st = null;

        try {
            String sql = "DELETE FROM hasil_akhir";
            st = conn.prepareStatement(sql);
            st.executeUpdate();
            st.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void insertHasilAkhir(int idMurid, double total) {
        PreparedStatement st = null;

        try {
            String sql = "INSERT INTO hasil_akhir(id_murid, total) VALUES (?,?)";
            st = conn.prepareStatement(sql);

            st.setInt(1, idMurid);
            st.setDouble(2, total);

            st.executeUpdate();
            st.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}