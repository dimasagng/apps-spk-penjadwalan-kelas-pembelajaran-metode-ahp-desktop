package com.spk.dao;

import com.spk.config.Koneksi;
import com.spk.model.User;
import com.spk.service.ServiceUser;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.mindrot.jbcrypt.BCrypt;

public class UserDAO implements ServiceUser {

    private final Connection conn;

    public UserDAO() {
        conn = Koneksi.getConnection();
    }

    @Override
    public boolean isUserExists() {
        PreparedStatement st = null;
        ResultSet rs = null;

        try {
            String sql = "SELECT COUNT(*) FROM user";
            st = conn.prepareStatement(sql);
            rs = st.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public boolean validateUsername(User model) {
        PreparedStatement st = null;
        ResultSet rs = null;
        boolean valid = false;

        try {
            String sql = "SELECT username FROM user WHERE username LIKE BINARY ?";
            st = conn.prepareStatement(sql);
            st.setString(1, model.getUsername());
            rs = st.executeQuery();

            valid = rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return valid;
    }

    @Override
    public void insertData(User model) {
        PreparedStatement st = null;

        try {
            String sql = "INSERT INTO user (nama, email, username, password, role) VALUES (?,?,?,?,?)";
            st = conn.prepareStatement(sql);

            st.setString(1, model.getNama());
            st.setString(2, model.getEmail());
            st.setString(3, model.getUsername());
            st.setString(4, BCrypt.hashpw(model.getPassword(), BCrypt.gensalt()));
            st.setString(5, model.getRole());

            st.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public User processLogin(User model) {
        PreparedStatement st = null;
        ResultSet rs = null;
        String sql = "SELECT id_user, nama, email, username, password, role FROM user WHERE username=?";
        User modelUser = null;

        try {
            st = conn.prepareStatement(sql);
            st.setString(1, model.getUsername());
            rs = st.executeQuery();

            if (rs.next()) {
                String hashedPassword = rs.getString("password");

                if (BCrypt.checkpw(model.getPassword(), hashedPassword)) {
                    modelUser = new User();
                    modelUser.setIdUser(rs.getInt("id_user"));
                    modelUser.setNama(rs.getString("nama"));
                    modelUser.setEmail(rs.getString("email"));
                    modelUser.setUsername(rs.getString("username"));
                    modelUser.setRole(rs.getString("role"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return modelUser;
    
    }

    @Override
    public void updateData(User model) {
        PreparedStatement st = null;

        try {
            String sql = "UPDATE user SET nama=?, email=?, username=?, role=? WHERE id_user=?";
            st = conn.prepareStatement(sql);

            st.setString(1, model.getNama());
            st.setString(2, model.getEmail());
            st.setString(3, model.getUsername());
            st.setString(4, model.getRole());
            st.setInt(5, model.getIdUser());

            st.executeUpdate();
            st.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
}

    @Override
    public void deleteData(User model) {
         PreparedStatement st = null;

        try {
            String sql = "DELETE FROM user WHERE id_user=?";
            st = conn.prepareStatement(sql);

            st.setInt(1, model.getIdUser());

            st.executeUpdate();
            st.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public User getUserById(int idUser) {
        PreparedStatement st = null;
        ResultSet rs = null;
        User modelUser = null;

        try {
            String sql = "SELECT id_user, nama, email, username, role FROM user WHERE id_user=?";
            st = conn.prepareStatement(sql);
            st.setInt(1, idUser);
            rs = st.executeQuery();

            while (rs.next()) {
                modelUser = new User();
                modelUser.setIdUser(rs.getInt("id_user"));
                modelUser.setNama(rs.getString("nama"));
                modelUser.setEmail(rs.getString("email"));
                modelUser.setUsername(rs.getString("username"));
                modelUser.setRole(rs.getString("role"));
            }
            rs.close();
            st.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return modelUser;
}

    @Override
    public List<User> getData() {
         PreparedStatement st = null;
        ResultSet rs = null;
        List<User> list = new ArrayList<>();

        try {
            String sql = "SELECT id_user, nama, email, username, role FROM user";
            st = conn.prepareStatement(sql);
            rs = st.executeQuery();

            while (rs.next()) {
                User modelUser = new User();
                modelUser.setIdUser(rs.getInt("id_user"));
                modelUser.setNama(rs.getString("nama"));
                modelUser.setEmail(rs.getString("email"));
                modelUser.setUsername(rs.getString("username"));
                modelUser.setRole(rs.getString("role"));

                list.add(modelUser);
            }
            rs.close();
            st.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    @Override
    public List<User> searchData(String keyword) {
         PreparedStatement st = null;
        ResultSet rs = null;
        List<User> list = new ArrayList<>();

        try {
            String sql = "SELECT id_user, nama, email, username, role FROM user "
                    + "WHERE nama LIKE ? "
                    + "OR email LIKE ? "
                    + "OR username LIKE ? "
                    + "OR role LIKE ? ";
            st = conn.prepareStatement(sql);
            st.setString(1, "%" + keyword + "%");
            st.setString(2, "%" + keyword + "%");
            st.setString(3, "%" + keyword + "%");
            st.setString(4, "%" + keyword + "%");
            rs = st.executeQuery();

            while (rs.next()) {
                User modelUser = new User();
                modelUser.setIdUser(rs.getInt("id_user"));
                modelUser.setNama(rs.getString("nama"));
                modelUser.setEmail(rs.getString("email"));
                modelUser.setUsername(rs.getString("username"));
                modelUser.setRole(rs.getString("role"));

                list.add(modelUser);
            }
            rs.close();
            st.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    @Override
    public boolean validateOldPassword(String username, String oldPassword) {
        PreparedStatement st = null;
        ResultSet rs = null;
        String sql = "SELECT password FROM user WHERE username = ?";
        try {
            st = conn.prepareStatement(sql);
            st.setString(1, username);
            rs = st.executeQuery();
            
            if (rs.next()) {
                String hashedPassword = rs.getString("password");
                return BCrypt.checkpw(oldPassword, hashedPassword);
            } else {
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
}

    @Override
    public boolean ChangePassword(String username, String oldPassword, String newPassword) {
            PreparedStatement st = null;
            PreparedStatement stUpdate = null;
            ResultSet rs = null;
            String sql = "SELECT password FROM user WHERE username = ?"; 
        try {
            st = conn.prepareStatement(sql);
                st.setString(1, username);
                rs = st.executeQuery();

                if (rs.next()) {
                    String hashedPassword = rs.getString("password");
                    if (BCrypt.checkpw(oldPassword, hashedPassword)) {
                        String hashedNewPassword = BCrypt.hashpw(newPassword, BCrypt.gensalt());
                        String sqlUpdate = "UPDATE user SET password = ? WHERE username = ?";
                        stUpdate = conn.prepareStatement(sqlUpdate);
                        stUpdate.setString(1, hashedNewPassword);
                        stUpdate.setString(2, username);
                        int result = stUpdate.executeUpdate();
                        return result > 0;
                    } else {
                        return false;
                    }
                } else {
                    return false;
                }
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
        }
}