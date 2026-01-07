package com.spk.dao;

import com.spk.config.Koneksi;
import com.spk.model.Kriteria;
import com.spk.service.ServiceKriteria;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class KriteriaDAO implements ServiceKriteria {

    private final Connection conn;

    public KriteriaDAO() {
        conn = Koneksi.getConnection();
    }

    @Override
    public void insertData(Kriteria model) {
        PreparedStatement st = null;

        try {
            String sql = "INSERT INTO kriteria(kode_kriteria, nama_kriteria) VALUES (?,?)";
            st = conn.prepareStatement(sql);

            st.setString(1, model.getKodeKriteria());
            st.setString(2, model.getNamaKriteria());

            st.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateData(Kriteria model) {
        PreparedStatement st = null;

        try {
            String sql = "UPDATE kriteria SET kode_kriteria=?, nama_kriteria=? WHERE id_kriteria=?";
            st = conn.prepareStatement(sql);

            st.setString(1, model.getKodeKriteria());
            st.setString(2, model.getNamaKriteria());
            st.setInt(3, model.getIdKriteria());

            st.executeUpdate();
            st.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteData(Kriteria model) {
                PreparedStatement st = null;

        try {
            String sql = "DELETE FROM kriteria WHERE id_kriteria=?";
            st = conn.prepareStatement(sql);

            st.setInt(1, model.getIdKriteria());

            st.executeUpdate();
            st.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Kriteria> getData() {
        PreparedStatement st = null;
        ResultSet rs = null;
        List<Kriteria> list = new ArrayList<>();

        try {
            String sql = "SELECT id_kriteria, kode_kriteria, nama_kriteria FROM kriteria";
            st = conn.prepareStatement(sql);
            rs = st.executeQuery();

            while (rs.next()) {
                Kriteria modelKriteria = new Kriteria();
                modelKriteria.setIdKriteria(rs.getInt("id_kriteria"));
                modelKriteria.setKodeKriteria(rs.getString("kode_kriteria"));
                modelKriteria.setNamaKriteria(rs.getString("nama_kriteria"));

                list.add(modelKriteria);
            }
            rs.close();
            st.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    @Override
    public List<Kriteria> searchData(String keyword) {
        PreparedStatement st = null;
        ResultSet rs = null;
        List<Kriteria> list = new ArrayList<>();

        try {
            String sql = "SELECT id_kriteria, kode_kriteria, nama_kriteria FROM kriteria "
                       + "WHERE kode_kriteria LIKE ? OR nama_kriteria LIKE ?";
            st = conn.prepareStatement(sql);
            st.setString(1, "%" + keyword + "%");
            st.setString(2, "%" + keyword + "%");
            rs = st.executeQuery();

            while (rs.next()) {
                Kriteria modelKriteria = new Kriteria();
                modelKriteria.setIdKriteria(rs.getInt("id_kriteria"));
                modelKriteria.setKodeKriteria(rs.getString("kode_kriteria"));
                modelKriteria.setNamaKriteria(rs.getString("nama_kriteria"));

                list.add(modelKriteria);
            }
            rs.close();
            st.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    @Override
    public String generateKodeKriteria() {
        PreparedStatement st = null;
        ResultSet rs = null;
        String urutan = null;

        try {
            String sql = "SELECT kode_kriteria FROM kriteria ORDER BY kode_kriteria DESC LIMIT 1";
            st = conn.prepareStatement(sql);
            rs = st.executeQuery();

            int nomor;
            if (rs.next()) {
                String lastKode = rs.getString("kode_kriteria");
                nomor = Integer.parseInt(lastKode.substring(1)) + 1;
            } else {
                nomor = 1;
            }

            urutan = "K" + nomor;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return urutan;
    }

    @Override
    public boolean validasiNamaKriteria(Kriteria model) {
        PreparedStatement st = null;
        ResultSet rs = null;
        boolean valid = false;

        try {
            String sql = "SELECT nama_kriteria FROM kriteria WHERE nama_kriteria LIKE BINARY ?";
            st = conn.prepareStatement(sql);
            st.setString(1, model.getNamaKriteria());
            rs = st.executeQuery();

            valid = !rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return valid;
    }

    @Override
    public void updateBobot(int idKriteria, double bobot) {
        PreparedStatement st = null;
        ResultSet rs = null;

        try {
            String sql = "UPDATE kriteria SET bobot=? WHERE id_kriteria=?";
            st = conn.prepareStatement(sql);

            st.setDouble(1, bobot);
            st.setInt(2, idKriteria);
            st.executeUpdate();

            st.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}