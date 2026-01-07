package com.spk.service;

import com.spk.model.User;
import java.util.List;

public interface ServiceUser {
    
    boolean isUserExists();
    boolean validateUsername(User model);
    
    void insertData(User model);
    void updateData(User model);
    void deleteData(User model);
    
    User processLogin(User model);
    User getUserById(int idUser);
    
    List<User> getData();
    List<User> searchData(String keyword);
    
    boolean validateOldPassword(String username, String oldPassword);
    boolean ChangePassword(String username, String oldPassword, String newPassword);
    
}
