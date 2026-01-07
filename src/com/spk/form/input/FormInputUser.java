package com.spk.form.input;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import com.spk.dao.UserDAO;
import com.spk.form.FormUser;
import com.spk.model.User;
import com.spk.service.ServiceUser;
import static com.spk.util.AlertUtils.getOptionAlert;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.ItemEvent;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import net.miginfocom.swing.MigLayout;
import raven.modal.ModalDialog;
import raven.modal.Toast;

public class FormInputUser extends JPanel {
    
    private JLabel lbPassword;
    private JTextField txtNama, txtEmail, txtUsername;
    private JPasswordField txtPassword;
    private JComboBox cbxRole;
    private JButton btnSave, btnCancel;

    private final ServiceUser servis = new UserDAO();
    private User model;
    private final FormUser formUser;
    private int idUser;

    public FormInputUser(User model, FormUser formUser) {
        init();

        this.model = model;
        this.formUser = formUser;
        if (model != null) {
            loadData();
        }
    }

    private void init() {
        setLayout(new MigLayout("fillx, insets 5 30 5 30, wrap 2, gap 20, width 400","[50][fill, grow]"));

        txtNama = new JTextField();
        txtNama.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Masukkan nama");
        txtNama.putClientProperty(FlatClientProperties.TEXT_FIELD_SHOW_CLEAR_BUTTON, true);

        txtEmail = new JTextField();
        txtEmail.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Masukkan email");
        txtEmail.putClientProperty(FlatClientProperties.TEXT_FIELD_SHOW_CLEAR_BUTTON, true);
        
        lbPassword = new JLabel("Password");
        
        txtUsername = new JTextField();
        txtUsername.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Masukkan username");
        txtUsername.putClientProperty(FlatClientProperties.TEXT_FIELD_SHOW_CLEAR_BUTTON, true);
        
        txtPassword = new JPasswordField();
        txtPassword.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Masukkan password");
        txtPassword.putClientProperty(FlatClientProperties.TEXT_FIELD_SHOW_CLEAR_BUTTON, true);
                txtPassword.putClientProperty(FlatClientProperties.STYLE, ""
                + "showRevealButton:true;"
                + "showCapsLock:true");
                
                cbxRole = new JComboBox();
                initComboItem(cbxRole);
                setColorCbx();  

        btnSave = new JButton("Save");
        btnSave.putClientProperty(FlatClientProperties.STYLE, "background:@accentColor;foreground:rgb(255,255,255);");
        btnSave.setIcon(new FlatSVGIcon("com/spk/icon/save_white.svg", 0.8f));
        btnSave.setIconTextGap(5);
        btnSave.addActionListener((e) -> {
            if (model == null) {
                insertData();
            } else {
                updateData();
            }
        });

        btnCancel = new JButton("Cancel");
        btnCancel.setIcon(new FlatSVGIcon("com/spk/icon/cancel.svg", 0.4f));
        btnCancel.setIconTextGap(5);
        btnCancel.addActionListener((e) -> {
            if (model == null) {
                ModalDialog.closeModal("Form Input");
            } else {
                ModalDialog.closeModal("Form Update");
            }
        });
    
        add(createSeparator(), "span, growx, height 2!");
        add(new JLabel("Nama"), "align right");
        add(txtNama);
        add(new JLabel("Email"), "align right");
        add(txtEmail);
        add(new JLabel("Username"), "align right");
        add(txtUsername);
        add(lbPassword, "align right");
        add(txtPassword);
        add(new JLabel("Role"), "align right");
        add(cbxRole);
        add(createSeparator(), "span, growx, height 2!");
        add(btnSave, "span, split 2, align center, sg btn, hmin 30");
        add(btnCancel, "sg btn, hmin 30");
    }

    private JSeparator createSeparator() {
        JSeparator separator = new JSeparator();
        separator.putClientProperty(FlatClientProperties.STYLE, "foreground:rgb(206,206,206)");
        return separator;
    }
    
    private void initComboItem(JComboBox cbxRole) {
        cbxRole.addItem("Select Role");
        cbxRole.addItem("Admin");
        cbxRole.addItem("User");
    }
    
private void setColorBasedOnSelection() {
    if (cbxRole.getSelectedItem().equals("Select Role")) {
        cbxRole.putClientProperty(FlatClientProperties.STYLE, "foreground:@placeholderColor;");
    } else {
        cbxRole.putClientProperty(FlatClientProperties.STYLE, "foreground: $TextField.foreground");
    }
}

private void setColorCbx() {
    cbxRole.addItemListener((e) -> {
        if (e.getStateChange() == ItemEvent.SELECTED) {
            setColorBasedOnSelection();
        }
    });

    cbxRole.addFocusListener(new FocusAdapter() {
        @Override
        public void focusLost(FocusEvent e) {
            setColorBasedOnSelection();
        }
    });

    setColorBasedOnSelection();
}

    private boolean validasiInput(boolean validatePassword, boolean isUpdate) {
        boolean valid = false;
            String username = txtUsername.getText().trim();
            String currentUsername = isUpdate ? model.getUsername() : null;
            

            if (txtNama.getText().trim().isEmpty()) {
                Toast.show(this, Toast.Type.INFO, "Please enter the name", getOptionAlert());
            } else if (txtEmail.getText().trim().isEmpty()) {
                Toast.show(this, Toast.Type.INFO, "Please enter the email", getOptionAlert());
            } else if (txtUsername.getText().trim().isEmpty()) {
                Toast.show(this, Toast.Type.INFO, "Please enter the username", getOptionAlert());
            } else if (validatePassword && txtPassword.getText().trim().isEmpty()) {
                Toast.show(this, Toast.Type.INFO, "Please enter the password", getOptionAlert());
            } else if (cbxRole.getSelectedItem().equals("Select Role")) {
                Toast.show(this, Toast.Type.INFO, "Please select a role", getOptionAlert());
            } else {
                if (isUpdate && username.equals(currentUsername)) {
                valid = true;
              } else {
                User modelUser = new User();
                modelUser.setUsername(username);

                if (!servis.validateUsername(modelUser)) {
                    valid = true;
                } else {
                    Toast.show(this, Toast.Type.WARNING, "This username is already taken\nPlease choose another one", getOptionAlert());
                }
            }
        }

    return valid;
}

    private void insertData() {
        if (validasiInput(true, false)) {
               String nama = txtNama.getText();
               String email = txtEmail.getText();
               String username = txtUsername.getText();
               String password = txtPassword.getText();
               String role = cbxRole.getSelectedItem().toString();

               User modelUser = new User();
               modelUser.setNama(nama);
               modelUser.setEmail(email);
               modelUser.setUsername(username);
               modelUser.setPassword(password);
               modelUser.setRole(role);

               servis.insertData(modelUser);
               Toast.show(this, Toast.Type.SUCCESS, "Data has been successfully added", getOptionAlert());

               formUser.refreshTable();
               resetForm();
        }
    }

    private void loadData() {
        btnSave.setText("Update");
        
        remove(lbPassword);
        remove(txtPassword);
        repaint();
        revalidate();

        idUser = model.getIdUser();
        txtNama.setText(model.getNama());
        txtEmail.setText(model.getEmail());
        txtUsername.setText(model.getUsername());
        cbxRole.setSelectedItem(model.getRole());
    }

    private void updateData() {
        if (validasiInput(false, true)) {
                String nama = txtNama.getText();
                String email = txtEmail.getText();
                String username = txtUsername.getText();
                String password = txtPassword.getText();
                String role = cbxRole.getSelectedItem().toString();

                User modelUser = new User();
                modelUser.setIdUser(idUser);
                modelUser.setNama(nama);
                modelUser.setEmail(email);
                modelUser.setUsername(username);
                modelUser.setPassword(password);
                modelUser.setRole(role);

                servis.updateData(modelUser);
                Toast.show(this, Toast.Type.SUCCESS, "Data has been successfully updated", getOptionAlert());

                formUser.refreshTable();
                resetForm();
                ModalDialog.closeModal("Form Update");
        }
    }

    private void resetForm() {
        txtNama.setText("");
        txtEmail.setText("");
        txtUsername.setText("");
        txtPassword.setText("");
        cbxRole.setSelectedIndex(0);
    }
}