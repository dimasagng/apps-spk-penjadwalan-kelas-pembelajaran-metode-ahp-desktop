package com.spk.main;

import com.spk.dao.UserDAO;
import com.spk.form.FormDashboard;
import com.spk.form.FormLogin;
import com.spk.form.FormRegistrasi;
import com.spk.model.User;
import com.spk.service.ServiceUser;
import com.spk.util.About;
import javax.swing.JFrame;
import raven.modal.Drawer;
import raven.modal.ModalDialog;
import raven.modal.component.SimpleModalBorder;
import raven.modal.demo.utils.UndoRedo;

public class FormManager {

    public static final UndoRedo<Form> FORMS = new UndoRedo<>();
    private static MainForm mainForm;
    private static JFrame frame;
    private static FormLogin formlogin;
    private static User loggedInUser;

    public static void install(JFrame f) {
        frame = f;

    ServiceUser servis = new UserDAO();
    if (!servis.isUserExists()) {
        registrasi();
    } else {
        logout();
    }
}

    public static void showForm(Form form) {
        if (form != FORMS.getCurrent()) {
            FORMS.add(form);
            form.formCheck();
            form.formOpen();
            mainForm.setForm(form);
        }
    }
    
    public static void login(User user) {
    loggedInUser = user;

    Drawer.installDrawer(frame, new Menu());
    Drawer.setVisible(true);

    frame.getContentPane().removeAll();
    frame.getContentPane().add(getMainForm());
    Drawer.setSelectedItemClass(FormDashboard.class);
    frame.repaint();
    frame.revalidate();
}
    
    public static User getLoggedInUser() {
        return loggedInUser;
    }

    public static void logout() {
        if (loggedInUser == null) {
            Drawer.installDrawer(frame, new Menu());
        }
        
            Drawer.setVisible(false);
            frame.getContentPane().removeAll();
            FormLogin login = getLogin();
            login.formCheck();
            frame.getContentPane().add(login);
            FORMS.clear();
            frame.repaint();
            frame.revalidate();
        }
    
        public static void registrasi() {
        if (loggedInUser == null) {
            Drawer.installDrawer(frame, new Menu());
        }
        
            Drawer.setVisible(false);
            frame.getContentPane().removeAll();
            FormRegistrasi formRegistrasi = new FormRegistrasi();
            frame.getContentPane().add(formRegistrasi);
            FORMS.clear();
            frame.repaint();
            frame.revalidate();
        }

        public static JFrame getJFrame() {
            return frame;
        }

        private static MainForm getMainForm() {
            if (mainForm == null) {
                mainForm = new MainForm();
            }
            return mainForm;
        }

        private static FormLogin getLogin() {
            if (formlogin == null) {
                formlogin = new FormLogin();
            }
            return formlogin;
        }
        
       public static void showAbout() {
        // PERUBAHAN: Parameter judul diubah dari "About" menjadi null
        // Ini akan menghilangkan header teks "About" di atas logo
        ModalDialog.showModal(frame, new SimpleModalBorder(new About(), null),
                ModalDialog.createOption().setAnimationEnabled(false)
        );
    }
}