package com.spk.form;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import com.spk.dao.UserDAO;
import com.spk.main.Form;
import com.spk.main.FormManager;
import com.spk.model.User;
import com.spk.service.ServiceUser;
import static com.spk.util.AlertUtils.getOptionAlert;
import java.awt.Image;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import net.miginfocom.swing.MigLayout;
import raven.modal.Toast;

public class FormLogin extends Form {

    private JLabel imageLogo;
    private JPanel mainPanel;
    private JPanel panelForm;

    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JButton btnLogin;
    
    private final ServiceUser servis = new UserDAO();

    public FormLogin() {
        init();
        setActionButton();
    }

    private void init() {
        setLayout(new MigLayout("fill, insets 20", "[center]", "[center]"));

        mainPanel = new JPanel(new MigLayout("insets 50", "[][]", "[fill][grow]"));
        mainPanel.putClientProperty(FlatClientProperties.STYLE, "arc:20;background:@accentColor");

        JPanel panelLogo = new JPanel(new MigLayout("wrap", "300", "[]0[]"));
        panelLogo.putClientProperty(FlatClientProperties.STYLE, "background:@accentColor");
        
        imageLogo = new JLabel();
        imageLogo.setIcon(new ImageIcon(new ImageIcon(getClass().getResource("/com/spk/img/Logo.png"))
                .getImage()
                .getScaledInstance(150, 100, Image.SCALE_SMOOTH)));

        //JLabel lbTitleLogo = new JLabel("SPK AHP Penjadwalan");
        //lbTitleLogo.putClientProperty(FlatClientProperties.STYLE, "foreground:rgb(255,255,255);font:bold italic +14 Roboto");

        JLabel lbDetail = new JLabel("Sistem Pendukung Keputusan");
        lbDetail.putClientProperty(FlatClientProperties.STYLE, "foreground:rgb(255,255,255);font:bold +8 Roboto");

        JLabel lbDetail2 = new JLabel("Penjadwalan Kelas Pembelajaran Dengan Metode");
        lbDetail2.putClientProperty(FlatClientProperties.STYLE, "foreground:rgb(255,255,255);font:bold +8 Roboto");
        
        // PERUBAHAN: Menggunakan HTML untuk styling teks campuran (Italic dan Normal)
        // Bagian <i>...</i> akan miring, (AHP) akan mengikuti style dasar (Bold, tidak miring)
        JLabel lbDetail3 = new JLabel("<html><i>Analytic Hierarchy Process</i> (AHP)</html>");
        // Note: 'italic' dihapus dari properti font di bawah ini agar (AHP) tegak
        lbDetail3.putClientProperty(FlatClientProperties.STYLE, "foreground:rgb(255,255,255);font:bold +8 Roboto");

        JLabel lbCreated = new JLabel("");
        lbCreated.putClientProperty(FlatClientProperties.STYLE, "foreground:rgb(140,140,140);font:12");

        panelLogo.add(imageLogo, "align center, gapy 30, gap 25px 0px");
        //panelLogo.add(lbTitleLogo, "align center, gapy 5, gap 25px 0px");
        panelLogo.add(lbDetail, "align center, gapy 5, gap 25px 0px");
        panelLogo.add(lbDetail2, "align center, gapy 5, gap 25px 0px");
        panelLogo.add(lbDetail3, "align center, gapy 5, gap 25px 0px");
        panelLogo.add(lbCreated, "align center, gapy 5, gap 25px 0px");

        panelForm = new JPanel(new MigLayout("wrap, insets 20", "fill, 200:250"));
        panelForm.putClientProperty(FlatClientProperties.STYLE, "arc:20;background:rgb(255,255,255)");

        JLabel lbTitleForm = new JLabel("Login", JLabel.CENTER);
        lbTitleForm.putClientProperty(FlatClientProperties.STYLE, "foreground:@accentColor;font:bold +10");

        JLabel lbDescription = new JLabel("Please sign in to access your dashboard", JLabel.CENTER);
        lbDescription.putClientProperty(FlatClientProperties.STYLE, "foreground:@accentColor");

        JLabel lbUsername = new JLabel("Username");
        lbUsername.putClientProperty(FlatClientProperties.STYLE, "foreground:@accentColor");
        
        txtUsername = new JTextField();
        txtUsername.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Username");
        txtUsername.putClientProperty(FlatClientProperties.TEXT_FIELD_SHOW_CLEAR_BUTTON, true);
        txtUsername.putClientProperty(FlatClientProperties.TEXT_FIELD_LEADING_ICON,
                new FlatSVGIcon("com/spk/icon/username.svg", 20, 20));
        txtUsername.putClientProperty(FlatClientProperties.STYLE, "arc:10");

        JLabel lbPassword = new JLabel("Password");
        lbPassword.putClientProperty(FlatClientProperties.STYLE, "foreground:@accentColor");

        txtPassword = new JPasswordField();
        txtPassword.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Password");
        txtPassword.putClientProperty(FlatClientProperties.TEXT_FIELD_SHOW_CLEAR_BUTTON, true);
        txtPassword.putClientProperty(FlatClientProperties.TEXT_FIELD_LEADING_ICON,
                new FlatSVGIcon("com/spk/icon/password.svg", 20, 20));
        txtPassword.putClientProperty(FlatClientProperties.STYLE, ""
                + "arc:10;"
                + "showRevealButton:true;"
                + "showCapsLock:true");

        btnLogin = new JButton("Login");
        btnLogin.putClientProperty(FlatClientProperties.STYLE, ""
                + "background:@accentColor;"
                + "foreground:rgb(255,255,255);"
                + "arc:10;"
                + "borderWidth:0;"
                + "focusWidth:0;"
                + "innerFocusWidth:0;"
                + "font:bold 16");

        panelForm.add(lbTitleForm);
        panelForm.add(lbDescription);
        panelForm.add(lbUsername, "gapy 8");
        panelForm.add(txtUsername, "hmin 30");
        panelForm.add(lbPassword, "gapy 8");
        panelForm.add(txtPassword, "hmin 30");
        panelForm.add(btnLogin, "hmin 30, gapy 15 15");

        mainPanel.add(panelForm);
        mainPanel.add(panelLogo);

        add(mainPanel);
        
        btnLogin.addActionListener((e) -> {
            processLogin();
        });
    }
    
    private void setActionButton() {
    txtUsername.addKeyListener(new KeyAdapter() {
        @Override
        public void keyPressed(KeyEvent e) {
            if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                processLogin();
            }
        }
    });

    txtPassword.addKeyListener(new KeyAdapter() {
        @Override
        public void keyPressed(KeyEvent e) {
            if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                processLogin();
            }
        }
    });
}
    
    private boolean validasiInput() {
    boolean valid = false;

    if (txtUsername.getText().trim().isEmpty()) {
        Toast.show(this, Toast.Type.INFO, "Please enter the username", getOptionAlert());
        txtUsername.putClientProperty("JComponent.outline", "error");
    } else if (txtPassword.getText().trim().isEmpty()) { // Catatan: Menggunakan .getText() pada JPasswordField tidak disarankan, sebaiknya .getPassword()
        Toast.show(this, Toast.Type.INFO, "Please enter the password", getOptionAlert());
    } else {
        valid = true;
        txtUsername.putClientProperty("JComponent.outline", null);
    }

    return valid;
}
    
    private void processLogin() {
    if (validasiInput() == true) {
        String username = txtUsername.getText();
        String password = txtPassword.getText();

        User modelUser = new User();
        modelUser.setUsername(username);
        modelUser.setPassword(password);

        User user = servis.processLogin(modelUser);
        if (user != null) {
            FormManager.login(user);
            resetForm();
        } else {
            Toast.show(this, Toast.Type.ERROR, "Incorrect username or password. Please try again", getOptionAlert());
        }
    }
}

    private void resetForm() {
        txtUsername.setText("");
        txtPassword.setText("");
    }
    
}