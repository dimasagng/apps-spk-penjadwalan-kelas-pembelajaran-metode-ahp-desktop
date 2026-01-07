package com.spk.form.input;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import com.spk.dao.KriteriaDAO;
import com.spk.dao.SubKriteriaDAO;
import com.spk.form.FormSubKriteria;
import com.spk.model.Kriteria;
import com.spk.model.SubKriteria;
import com.spk.service.ServiceKriteria;
import com.spk.service.ServiceSubKriteria;
import static com.spk.util.AlertUtils.getOptionAlert;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import net.miginfocom.swing.MigLayout;
import raven.modal.ModalDialog;
import raven.modal.Toast;

public class FormInputSubKriteria extends JPanel {

    private JTextField txtKodeSubKriteria, txtNamaSubKriteria;
    private JComboBox<Kriteria> cbxKriteriaInduk;
    private JButton btnSave, btnCancel;

    private final ServiceSubKriteria servis = new SubKriteriaDAO();
    private final ServiceKriteria servisKriteria = new KriteriaDAO(); // Untuk ComboBox
    private SubKriteria model;
    private final FormSubKriteria formSubKriteria;
    private int idSubKriteria;

    public FormInputSubKriteria(SubKriteria model, FormSubKriteria formSubKriteria) {
        init();
        loadKriteriaInduk();

        this.model = model;
        this.formSubKriteria = formSubKriteria;
        
        if (model != null) {
            loadData();
        } else {
            generateKodeSubKriteria(); // Generate kode awal saat form dibuka
        }
    }

    private void init() {
        setLayout(new MigLayout("fillx, insets 5 30 5 30, wrap 2, gap 20, width 400","[100, align right][fill, grow]"));

        cbxKriteriaInduk = new JComboBox<>();
        txtKodeSubKriteria = new JTextField();
        txtKodeSubKriteria.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Kode otomatis");
        txtKodeSubKriteria.setEditable(false); // Read-only karena otomatis
        
        txtNamaSubKriteria = new JTextField();
        txtNamaSubKriteria.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Masukkan nama sub kriteria");

        btnSave = new JButton("Save");
        btnSave.putClientProperty(FlatClientProperties.STYLE, "background:@accentColor;foreground:rgb(255,255,255);");
        btnSave.setIcon(new FlatSVGIcon("com/spk/icon/save_white.svg", 0.8f));
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
        // PERBAIKAN LAYOUT: Kriteria Induk dulu, baru Kode Sub
        add(new JLabel("Kriteria Induk"));
        add(cbxKriteriaInduk);
        add(new JLabel("Kode Sub Kriteria"));
        add(txtKodeSubKriteria);
        add(new JLabel("Nama Sub Kriteria"));
        add(txtNamaSubKriteria);
        add(createSeparator(), "span, growx, height 2!");
        add(btnSave, "span, split 2, align center, sg btn, hmin 30");
        add(btnCancel, "sg btn, hmin 30");
    }
    
    private void loadKriteriaInduk() {
        DefaultComboBoxModel<Kriteria> model = new DefaultComboBoxModel<>();
        model.addElement(new Kriteria(0, "Pilih Kriteria"));
        List<Kriteria> list = servisKriteria.getData();
        for (Kriteria k : list) {
            model.addElement(k);
        }
        cbxKriteriaInduk.setModel(model);
    }

    private JSeparator createSeparator() {
        JSeparator separator = new JSeparator();
        separator.putClientProperty(FlatClientProperties.STYLE, "foreground:rgb(206,206,206)");
        return separator;
    }

    private void generateKodeSubKriteria() {
        // Memanggil method DAO yang sudah diperbaiki
        txtKodeSubKriteria.setText(servis.generateKodeSubKriteria());
    }

    private boolean validasiInput(boolean isUpdate) {
        boolean valid = false;
        String namaSubKriteria = txtNamaSubKriteria.getText().trim();
        String currentNamaSubKriteria = isUpdate ? model.getNamaSubKriteria() : null;

        if (cbxKriteriaInduk.getSelectedIndex() == 0) {
            Toast.show(this, Toast.Type.INFO, "Kriteria harus dipilih", getOptionAlert());
        } else if (txtNamaSubKriteria.getText().trim().isEmpty()) {
            Toast.show(this, Toast.Type.INFO, "Nama sub kriteria tidak boleh kosong", getOptionAlert());
        } else {
            if (isUpdate && namaSubKriteria.equals(currentNamaSubKriteria)) {
                valid = true;
            } else {
                SubKriteria modelSubKriteria = new SubKriteria();
                modelSubKriteria.setNamaSubKriteria(namaSubKriteria);
                if (servis.validasiNamaSubKriteria(modelSubKriteria)) {
                    valid = true;
                } else {
                    Toast.show(this, Toast.Type.WARNING, "Nama sub kriteria sudah ada", getOptionAlert());
                }
            }
        }
        return valid;
    }

    private void insertData() {
        if (validasiInput(false)) {
            Kriteria kriteria = (Kriteria) cbxKriteriaInduk.getSelectedItem();
            String kodeSubKriteria = txtKodeSubKriteria.getText();
            String namaSubKriteria = txtNamaSubKriteria.getText();

            SubKriteria modelSubKriteria = new SubKriteria();
            modelSubKriteria.setIdKriteria(kriteria.getIdKriteria());
            modelSubKriteria.setKodeSubKriteria(kodeSubKriteria);
            modelSubKriteria.setNamaSubKriteria(namaSubKriteria);

           servis.insertData(modelSubKriteria);
            Toast.show(this, Toast.Type.SUCCESS, "Data has been successfully added", getOptionAlert());

            formSubKriteria.refreshTable();
            
            // Reset form agar bisa input lagi, tapi Kriteria Induk jangan direset biar cepat
            // cbxKriteriaInduk.setSelectedIndex(0); 
            txtNamaSubKriteria.setText("");
            
            // Generate kode baru untuk input selanjutnya (misal habis S23 jadi S24)
            generateKodeSubKriteria();
        }
    }

    private void loadData() {
        btnSave.setText("Update");

        idSubKriteria = model.getIdSubKriteria();
        txtKodeSubKriteria.setText(model.getKodeSubKriteria());
        txtNamaSubKriteria.setText(model.getNamaSubKriteria());
        
        // Set ComboBox Kriteria Induk
        for (int i = 0; i < cbxKriteriaInduk.getItemCount(); i++) {
            Kriteria k = cbxKriteriaInduk.getItemAt(i);
            if (k.getIdKriteria() == model.getIdKriteria()) {
                cbxKriteriaInduk.setSelectedIndex(i);
                break;
            }
        }
    }

    private void updateData() {
        if (validasiInput(true)) {
            Kriteria kriteria = (Kriteria) cbxKriteriaInduk.getSelectedItem();
            // Kode Sub Kriteria TIDAK boleh diubah saat update untuk menjaga konsistensi
            // String kodeSubKriteria = txtKodeSubKriteria.getText(); 
            String namaSubKriteria = txtNamaSubKriteria.getText();

            SubKriteria modelSubKriteria = new SubKriteria();
            modelSubKriteria.setIdSubKriteria(idSubKriteria);
            modelSubKriteria.setIdKriteria(kriteria.getIdKriteria());
            
            // Gunakan kode yang lama dari model, jangan dari textfield (untuk keamanan)
            modelSubKriteria.setKodeSubKriteria(model.getKodeSubKriteria()); 
            
            modelSubKriteria.setNamaSubKriteria(namaSubKriteria);

            servis.updateData(modelSubKriteria);
            Toast.show(this, Toast.Type.SUCCESS, "Data has been successfully updated", getOptionAlert());

            formSubKriteria.refreshTable();
            resetForm();
            ModalDialog.closeModal("Form Update");
        }
    }

    private void resetForm() {
        cbxKriteriaInduk.setSelectedIndex(0);
        txtKodeSubKriteria.setText("");
        txtNamaSubKriteria.setText("");
    }
}