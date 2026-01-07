package com.spk.form.input;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import com.spk.dao.KriteriaDAO;
import com.spk.form.FormKriteria;
import com.spk.model.Kriteria;
import com.spk.service.ServiceKriteria;
import static com.spk.util.AlertUtils.getOptionAlert;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import net.miginfocom.swing.MigLayout;
import raven.modal.ModalDialog;
import raven.modal.Toast;

public class FormInputKriteria extends JPanel {

    private JTextField txtKodeKriteria, txtNamaKriteria;
    private JButton btnSave, btnCancel;

    private final ServiceKriteria servis = new KriteriaDAO();
    private Kriteria model;
    private final FormKriteria formKriteria;
    private int idKriteria;

    public FormInputKriteria(Kriteria model, FormKriteria formKriteria) {
        init();

        this.model = model;
        this.formKriteria = formKriteria;
        if (model != null) {
            loadData();
        } else {
            generateKodeKriteria();
        }
    }

    private void init() {
        setLayout(new MigLayout("fillx, insets 5 30 5 30, wrap 2, gap 20, width 400","[50][fill, grow]"));

        txtKodeKriteria = new JTextField();
        txtKodeKriteria.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Masukkan kode kriteria");
        txtKodeKriteria.putClientProperty(FlatClientProperties.TEXT_FIELD_SHOW_CLEAR_BUTTON, true);

        txtNamaKriteria = new JTextField();
        txtNamaKriteria.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Masukkan nama kriteria");
        txtNamaKriteria.putClientProperty(FlatClientProperties.TEXT_FIELD_SHOW_CLEAR_BUTTON, true);

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
        add(new JLabel("Kode Kriteria"), "align right");
        add(txtKodeKriteria);
        add(new JLabel("Nama Kriteria"), "align right");
        add(txtNamaKriteria);
        add(createSeparator(), "span, growx, height 2!");
        add(btnSave, "span, split 2, align center, sg btn, hmin 30");
        add(btnCancel, "sg btn, hmin 30");
    }

    private JSeparator createSeparator() {
        JSeparator separator = new JSeparator();
        separator.putClientProperty(FlatClientProperties.STYLE, "foreground:rgb(206,206,206)");
        return separator;
    }

    private void generateKodeKriteria() {
        txtKodeKriteria.setText(servis.generateKodeKriteria());
        txtKodeKriteria.setEditable( false);
    }

    private boolean validasiInput(boolean isUpdate) {
        boolean valid = false;
        String namaKriteria = txtNamaKriteria.getText().trim();
        String currentNamaKriteria = isUpdate ? model.getNamaKriteria() : null;

        if (txtNamaKriteria.getText().trim().isEmpty()) {
            Toast.show(this, Toast.Type.INFO, "Nama kriteria tidak boleh kosong", getOptionAlert());
        } else {
            if (isUpdate && namaKriteria.equals(currentNamaKriteria)) {
                valid = true;
            } else {
                Kriteria modelKriteria = new Kriteria();
                modelKriteria.setNamaKriteria(namaKriteria);

                if (servis.validasiNamaKriteria(modelKriteria)) {
                    valid = true;
                } else {
                    Toast.show(this, Toast.Type.WARNING, "Nama kriteria sudah ada\nSilahkan masukkan nama kriteria yang berbeda", getOptionAlert());
                }
            }
        }

        return valid;
    }

    private void insertData() {
        if (validasiInput(false)) {
            String kodeKriteria = txtKodeKriteria.getText();
            String namaKriteria = txtNamaKriteria.getText();

            Kriteria modelKriteria = new Kriteria();
            modelKriteria.setKodeKriteria(kodeKriteria);
            modelKriteria.setNamaKriteria(namaKriteria);

            servis.insertData(modelKriteria);
            Toast.show(this, Toast.Type.SUCCESS, "Data has been successfully added", getOptionAlert());

            formKriteria.refreshTable();
            resetForm();
            generateKodeKriteria();
        }
    }

    private void loadData() {
        btnSave.setText("Update");

        idKriteria = model.getIdKriteria();
        txtKodeKriteria.setText(model.getKodeKriteria());
        txtNamaKriteria.setText(model.getNamaKriteria());
    }

    private void updateData() {
        if (validasiInput(true)) {
            String kodeKriteria = txtKodeKriteria.getText();
            String namaKriteria = txtNamaKriteria.getText();

            Kriteria modelKriteria = new Kriteria();
            modelKriteria.setIdKriteria(idKriteria);
            modelKriteria.setKodeKriteria(kodeKriteria);
            modelKriteria.setNamaKriteria(namaKriteria);

            servis.updateData(modelKriteria);
            Toast.show(this, Toast.Type.SUCCESS, "Data has been successfully updated", getOptionAlert());

            formKriteria.refreshTable();
            resetForm();
            ModalDialog.closeModal("Form Update");
        }
    }

    private void resetForm() {
        txtKodeKriteria.setText("");
        txtNamaKriteria.setText("");
    }
}