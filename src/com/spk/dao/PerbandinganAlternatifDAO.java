package com.spk.dao;

import com.spk.config.Koneksi;
import com.spk.service.ServicePerbandinganAlternatif;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PerbandinganAlternatifDAO implements ServicePerbandinganAlternatif {

    private final Connection conn;

    public PerbandinganAlternatifDAO() {
        conn = Koneksi.getConnection();
    }

    @Override
    public void deleteByKriteria(int idKriteria) {
        PreparedStatement st = null;

        try {
            String sql = "DELETE FROM perbandingan_alternatif WHERE id_kriteria=?";
            st = conn.prepareStatement(sql);
            st.setInt(1, idKriteria);
            st.executeUpdate();
            st.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
}

    @Override
    public void insertData(int idKriteria, int idMurid1, int idMurid2, double nilai) {
        PreparedStatement st = null;

        try {
            String sql = "INSERT INTO perbandingan_alternatif(id_kriteria, id_murid_1, id_murid_2, nilai) VALUES (?,?,?,?)";
            st = conn.prepareStatement(sql);

            st.setInt(1, idKriteria);
            st.setInt(2, idMurid1);
            st.setInt(3, idMurid2);
            st.setDouble(4, nilai);

            st.executeUpdate();
            st.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
}

    @Override
    public double getNilai(int idKriteria, int idMurid1, int idMurid2) {
        double nilai = 1.0;
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            String sql = "SELECT nilai FROM perbandingan_alternatif WHERE id_kriteria=? AND id_murid_1=? AND id_murid_2=?";
            st = conn.prepareStatement(sql);
            st.setInt(1, idKriteria);
            st.setInt(2, idMurid1);
            st.setInt(3, idMurid2);
            rs = st.executeQuery();

            if (rs.next()) {
                return rs.getDouble("nilai");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return nilai;
    }
    @Override
    public void deleteBobotByKriteria(int idKriteria) {
        PreparedStatement st = null;

        try {
            String sql = "DELETE FROM bobot_alternatif WHERE id_kriteria=?";
            st = conn.prepareStatement(sql);
            st.setInt(1, idKriteria);
            st.executeUpdate();
            st.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void insertBobot(int idKriteria, int idMurid, double bobot) {
        PreparedStatement st = null;

        try {
            String sql = "INSERT INTO bobot_alternatif(id_kriteria, id_murid, bobot) VALUES (?,?,?)";
            st = conn.prepareStatement(sql);

            st.setInt(1, idKriteria);
            st.setInt(2, idMurid);
            st.setDouble(3, bobot);

            st.executeUpdate();
            st.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
