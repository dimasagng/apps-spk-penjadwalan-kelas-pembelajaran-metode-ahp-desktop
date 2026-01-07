package com.spk.dao;

import com.spk.config.Koneksi;
import com.spk.model.Murid;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import com.spk.service.ServiceMurid;

public class MuridDAO implements ServiceMurid {

    private final Connection conn;

    public MuridDAO() {
        conn = Koneksi.getConnection();
    }

    @Override
    public void insertData(Murid model) {
        PreparedStatement st = null;

        try {
            String sql = "INSERT INTO murid(kode_murid, nama_murid, jumlah_murid, jenis_program, pengajar, preferensi_waktu, media_pembelajaran) VALUES (?,?,?,?,?,?,?)";
            st = conn.prepareStatement(sql);

            st.setString(1, model.getKodeMurid());
            st.setString(2, model.getNamaMurid());
            st.setString(3, model.getJumlahMurid());
            st.setString(4, model.getJenisProgram());
            st.setString(5, model.getPengajar());
            st.setString(6, model.getPreferensiWaktu());
            st.setString(7, model.getMediaPembelajaran());

            st.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeStatement(st);
        }
    }

    @Override
    public void updateData(Murid model) {
        PreparedStatement st = null;

        try {
            String sql = "UPDATE murid SET nama_murid=?, jumlah_murid=?, jenis_program=?, pengajar=?, preferensi_waktu=?, media_pembelajaran=? WHERE id_murid=?";
            st = conn.prepareStatement(sql);

            st.setString(1, model.getNamaMurid());
            st.setString(2, model.getJumlahMurid());
            st.setString(3, model.getJenisProgram());
            st.setString(4, model.getPengajar());
            st.setString(5, model.getPreferensiWaktu());
            st.setString(6, model.getMediaPembelajaran());
            st.setInt(7, model.getIdMurid());

            st.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeStatement(st);
        }
    }

    @Override
    public void deleteData(Murid model) {
                PreparedStatement st = null;

        try {
            String sql = "DELETE FROM murid WHERE id_murid=?";
            st = conn.prepareStatement(sql);

            st.setInt(1, model.getIdMurid());

            st.executeUpdate();
            st.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Murid> getData() {
        PreparedStatement st = null;
        ResultSet rs = null;
        List<Murid> list = new ArrayList<>();

        try {
            String sql = "SELECT * FROM murid";
            st = conn.prepareStatement(sql);
            rs = st.executeQuery();

            while (rs.next()) {
                Murid modelMurid = new Murid();
                modelMurid.setIdMurid(rs.getInt("id_murid"));
                modelMurid.setKodeMurid(rs.getString("kode_murid"));
                modelMurid.setNamaMurid(rs.getString("nama_murid"));
                modelMurid.setJumlahMurid(rs.getString("jumlah_murid"));
                modelMurid.setJenisProgram(rs.getString("jenis_program"));
                modelMurid.setPengajar(rs.getString("pengajar"));
                modelMurid.setPreferensiWaktu(rs.getString("preferensi_waktu"));
                modelMurid.setMediaPembelajaran(rs.getString("media_pembelajaran"));
                
                list.add(modelMurid);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResultSet(rs);
            closeStatement(st);
        }
        return list;
    }

    @Override
    public List<Murid> searchData(String keyword) {
        PreparedStatement st = null;
        ResultSet rs = null;
        List<Murid> list = new ArrayList<>();

        try {
            String sql = "SELECT * FROM murid "
                       + "WHERE kode_murid LIKE ? OR nama_murid LIKE ? OR pengajar LIKE ? OR jenis_program LIKE ?";
            st = conn.prepareStatement(sql);
            st.setString(1, "%" + keyword + "%");
            st.setString(2, "%" + keyword + "%");
            st.setString(3, "%" + keyword + "%");
            st.setString(4, "%" + keyword + "%");
            rs = st.executeQuery();

            while (rs.next()) {
                Murid modelMurid = new Murid();
                modelMurid.setIdMurid(rs.getInt("id_murid"));
                modelMurid.setKodeMurid(rs.getString("kode_murid"));
                modelMurid.setNamaMurid(rs.getString("nama_murid"));
                modelMurid.setJumlahMurid(rs.getString("jumlah_murid"));
                modelMurid.setJenisProgram(rs.getString("jenis_program"));
                modelMurid.setPengajar(rs.getString("pengajar"));
                modelMurid.setPreferensiWaktu(rs.getString("preferensi_waktu"));
                modelMurid.setMediaPembelajaran(rs.getString("media_pembelajaran"));
                
                list.add(modelMurid);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResultSet(rs);
            closeStatement(st);
        }
        return list;
    }

    @Override
    public boolean validasiNamaMurid(Murid model) {
        PreparedStatement st = null;
        ResultSet rs = null;
        boolean valid = false;

        try {
            String sql = "SELECT nama_murid FROM murid WHERE nama_murid LIKE BINARY ?";
            st = conn.prepareStatement(sql);
            st.setString(1, model.getNamaMurid());
            rs = st.executeQuery();

            valid = !rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResultSet(rs);
            closeStatement(st);
        }
        return valid;
    }
    
    @Override
    public String generateKodeMurid() {
        PreparedStatement st = null;
        ResultSet rs = null;
        String newKode = "RA0001";

        try {
            String sql = "SELECT kode_murid FROM murid ORDER BY kode_murid DESC LIMIT 1";
            st = conn.prepareStatement(sql);
            rs = st.executeQuery();

            if (rs.next()) {
                String lastKode = rs.getString("kode_murid");

                String numPart = lastKode.substring(2); 
                int num = Integer.parseInt(numPart);
                num++;
                
                newKode = "RA" + String.format("%04d", num);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResultSet(rs);
            closeStatement(st);
        }
        return newKode;
    }


    private void closeStatement(PreparedStatement st) {
        if (st != null) {
            try {
                st.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void closeResultSet(ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}