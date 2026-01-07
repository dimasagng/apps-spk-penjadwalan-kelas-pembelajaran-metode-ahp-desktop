package com.spk.form.input;

import com.spk.dao.UserDAO;
import com.spk.model.User;
import com.spk.service.ServiceUser;
import com.formdev.flatlaf.FlatClientProperties;
import com.spk.main.Form;
import com.spk.main.FormManager;
import static com.spk.util.AlertUtils.getOptionAlert;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.ItemEvent;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import net.miginfocom.swing.MigLayout;
import raven.modal.Toast;

public class FormUserProfile extends Form {

    private final ServiceUser servis = new UserDAO();

    private final JTextField txtNama = new JTextField();
    private final JTextField txtEmail = new JTextField();
    private final JTextField txtUsername = new JTextField();
    private final JComboBox cbxRole = new JComboBox();
    
    private final User loggedInUser;

    public FormUserProfile() {
        this.loggedInUser = FormManager.getLoggedInUser();
        init();
    }

    @Override
    public void formOpen() {
        super.formOpen();
        loadData();
    }

    private void init() {
        setLayout(new MigLayout("fillx, wrap", "[fill]", "[][][fill, grow]"));
        add(setInfo());
        add(createSeparator(), "span, growx, height 2, gapx 10 10");
        add(setButton());
    }

    private JSeparator createSeparator() {
        JSeparator separator = new JSeparator();
        separator.putClientProperty(FlatClientProperties.STYLE, "foreground:rgb(206,206,206)");
        return separator;
    }

    private JPanel setInfo() {
        JPanel panel = new JPanel(new MigLayout("fillx"));
        panel.putClientProperty(FlatClientProperties.STYLE, ""
                + "arc:10");

        JLabel lbTitle = new JLabel("Profile");
        lbTitle.putClientProperty(FlatClientProperties.STYLE, ""
                + "font:bold 18");

        panel.add(lbTitle);
        return panel;
    }

    private void reloadLoggedInUser() {
        User updatedUser = servis.getUserById(loggedInUser.getIdUser());
        loggedInUser.setNama(updatedUser.getNama());
        loggedInUser.setEmail(updatedUser.getEmail());
        loggedInUser.setUsername(updatedUser.getUsername());
        loggedInUser.setRole(updatedUser.getRole());
    }

    private void loadData() {
        txtNama.setText(loggedInUser.getNama());
        txtEmail.setText(loggedInUser.getEmail());
        txtUsername.setText(loggedInUser.getUsername());
        cbxRole.setSelectedItem(loggedInUser.getRole());
    }

    private JPanel setButton() {
        JPanel panel = new JPanel(new MigLayout("wrap, insets 20, gap 20", "[][fill, 400]"));
        panel.putClientProperty(FlatClientProperties.STYLE, ""
                + "arc:10");

        txtNama.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Enter the name");
        txtNama.putClientProperty(FlatClientProperties.TEXT_FIELD_SHOW_CLEAR_BUTTON, true);

        txtEmail.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Enter the email");
        txtEmail.putClientProperty(FlatClientProperties.TEXT_FIELD_SHOW_CLEAR_BUTTON, true);

        txtUsername.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Enter the username");
        txtUsername.putClientProperty(FlatClientProperties.TEXT_FIELD_SHOW_CLEAR_BUTTON, true);

        initComboItem(cbxRole);
        cbxRole.setSelectedItem(loggedInUser.getRole());
        setColorCbx();

        JButton btnSave = new JButton("Save");
        btnSave.putClientProperty(FlatClientProperties.STYLE, ""
                + "background:@accentColor;"
                + "foreground:rgb(255,255,255);");
        btnSave.setIcon(new FlatSVGIcon("com/spk/icon/save_white.svg", 0.8f));
        btnSave.setIconTextGap(5);
        btnSave.addActionListener((e) -> updateData());

        JButton btnCancel = new JButton("Cancel");
        btnCancel.setIcon(new FlatSVGIcon("com/spk/icon/delete.svg", 0.4f)); // (Icon di gambar "delete", mungkin seharusnya "cancel")
        btnCancel.setIconTextGap(5);
        btnCancel.addActionListener((e) -> loadData());

        panel.add(new JLabel("Nama"), "align right");
        panel.add(txtNama);
        panel.add(new JLabel("Email"), "align right");
        panel.add(txtEmail);
        panel.add(new JLabel("Username"), "align right");
        panel.add(txtUsername);
        panel.add(new JLabel("Role"), "align right");
        panel.add(cbxRole);
        //panel.add(createSeparator(), "span, growx, height 2!");
        panel.add(btnSave, "span, split 2, align center, sg btn, hmin 30");
        panel.add(btnCancel, "sg btn, hmin 30");

        return panel;
    }

    private void initComboItem(JComboBox cbx) {
        cbx.addItem("Select Role");
        cbx.addItem("Admin");
        cbx.addItem("User");
    }

    private void setColorBasedOnSelection() {
        if (cbxRole.getSelectedItem().equals("Select Role")) {
            cbxRole.putClientProperty(FlatClientProperties.STYLE, "foreground:$Label.disabledForeground");
        } else {
            cbxRole.putClientProperty(FlatClientProperties.STYLE, "foreground:$TextField.foreground");
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

    private boolean validasiInput(boolean isUpdate) {
        boolean valid = false;
        String username = txtUsername.getText().trim();
        String currentUsername = isUpdate ? loggedInUser.getUsername() : null;

        if (txtNama.getText().trim().isEmpty()) {
            Toast.show(this, Toast.Type.INFO, "Please enter the name", getOptionAlert());
        } else if (txtEmail.getText().trim().isEmpty()) {
            Toast.show(this, Toast.Type.INFO, "Please enter the email", getOptionAlert());
        } else if (txtUsername.getText().trim().isEmpty()) {
            Toast.show(this, Toast.Type.INFO, "Please enter the username", getOptionAlert());
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

    private void updateData() {
        if (validasiInput(true)) {
            String nama = txtNama.getText();
            String email = txtEmail.getText();
            String username = txtUsername.getText();
            String role = cbxRole.getSelectedItem().toString();

            User modelUser = new User();
            modelUser.setIdUser(loggedInUser.getIdUser());
            modelUser.setNama(nama);
            modelUser.setEmail(email);
            modelUser.setUsername(username);
            modelUser.setRole(role);

            servis.updateData(modelUser);
            Toast.show(this, Toast.Type.SUCCESS, "Data has been successfully updated", getOptionAlert());
            reloadLoggedInUser();
        }
    }
}