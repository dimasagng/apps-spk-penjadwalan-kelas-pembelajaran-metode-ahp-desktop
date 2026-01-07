package com.spk.form.input;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import com.spk.dao.MuridDAO;
import com.spk.form.FormMurid;
import com.spk.model.Murid;
import static com.spk.util.AlertUtils.getOptionAlert;
import java.awt.event.ActionEvent; 
import java.awt.event.ActionListener;
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
import com.spk.service.ServiceMurid;

public class FormInputMurid extends JPanel {

    private JTextField txtNamaMurid;
    private JComboBox<String> cbxJumlahMurid;
    private JComboBox<String> cbxJenisProgram;
    private JComboBox<String> cbxPengajar;
    private JComboBox<String> cbxPreferensiWaktu;
    private JComboBox<String> cbxMediaPembelajaran;
    
    private JButton btnSave, btnCancel;

    private final ServiceMurid servis = new MuridDAO();
    private Murid model;
    private final FormMurid formMurid;
    private int idMurid;

    private final String[] JML_MURID_PILIHAN = {"Pilih Jumlah...", "1", "2", "3", "4"};
    
    private final String[] PROGRAM_PRIVATE = {"Pro Mixing", "Private Music Production", "Fundamentals Mixing", "Mastering", "Fundamentals Production", "Electronic Music", "Creative Editing"};
    private final String[] PROGRAM_REGULAR = {"Regular Onsite", "Regular Mixing"};
    
    private final String[] PENGAJAR_SEMUA = {"Pilih Pengajar...", "Hafizhjoys", "Envicimusic", "Reza Zulfikar", "Ngurah Wimba", "Badrun", "Ardiankeun"};
    private final String[] PENGAJAR_HAFZHJOYS_ENVICI = {"Pilih Pengajar...", "Hafizhjoys", "Envicimusic"};
    private final String[] PENGAJAR_HAFZHJOYS_ONLY = {"Pilih Pengajar...", "Hafizhjoys"};
    private final String[] PENGAJAR_ENVICI_BADRUN = {"Pilih Pengajar...", "Envicimusic", "Badrun"};

    private final String[] WAKTU_PILIHAN = {"Pilih Waktu...", "Siang", "Sore", "Malam"};
    private final String[] MEDIA_PILIHAN = {"Pilih Media...", "Online class", "Onsite class"};

    private ActionListener listenerJumlahMurid;
    private ActionListener listenerJenisProgram;

    public FormInputMurid(Murid model, FormMurid formMurid) {
        init();

        this.model = model;
        this.formMurid = formMurid;
        
        initComboBoxes();
        
        if (model != null) {
            loadData();
        } else {
            updateJenisProgram();
        }
    }

    private void init() {
        setLayout(new MigLayout("fillx, insets 5 30 5 30, wrap 2, gap 20, width 400","[130, align right][fill, grow]"));

        txtNamaMurid = new JTextField();
        txtNamaMurid.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Masukkan nama murid");
        
        cbxJumlahMurid = new JComboBox<>();
        cbxJenisProgram = new JComboBox<>();
        cbxPengajar = new JComboBox<>();
        cbxPreferensiWaktu = new JComboBox<>();
        cbxMediaPembelajaran = new JComboBox<>();
        

        listenerJumlahMurid = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateJenisProgram();
            }
        };
        cbxJumlahMurid.addActionListener(listenerJumlahMurid);
        
        listenerJenisProgram = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateMediaPembelajaran();
                updatePengajar(); 
            }
        };
        cbxJenisProgram.addActionListener(listenerJenisProgram);
     
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
        add(new JLabel("Nama Murid"));
        add(txtNamaMurid);
        add(new JLabel("Pilih Jumlah Murid"));
        add(cbxJumlahMurid);
        add(new JLabel("Pilih Jenis Program"));
        add(cbxJenisProgram);
        add(new JLabel("Pilih Pengajar"));
        add(cbxPengajar);
        add(new JLabel("Pilih Preferensi Waktu"));
        add(cbxPreferensiWaktu);
        add(new JLabel("Pilih Media Pembelajaran"));
        add(cbxMediaPembelajaran);
        add(createSeparator(), "span, growx, height 2!");
        add(btnSave, "span, split 2, align center, sg btn, hmin 30");
        add(btnCancel, "sg btn, hmin 30");
    }
    
    private void initComboBoxes() {
        cbxJumlahMurid.setModel(new DefaultComboBoxModel<>(JML_MURID_PILIHAN));
        cbxPengajar.setModel(new DefaultComboBoxModel<>(PENGAJAR_SEMUA)); // Default ke list lengkap
        cbxPreferensiWaktu.setModel(new DefaultComboBoxModel<>(WAKTU_PILIHAN));
        cbxMediaPembelajaran.setModel(new DefaultComboBoxModel<>(MEDIA_PILIHAN));
    }
    
    private void updateJenisProgram() {
        int selectedIndex = cbxJumlahMurid.getSelectedIndex();
        Object currentProgram = cbxJenisProgram.getSelectedItem();

        DefaultComboBoxModel<String> programModel = new DefaultComboBoxModel<>();
        programModel.addElement("Pilih Program...");

        if (selectedIndex == 1) { // "1" (Private)
            for (String program : PROGRAM_PRIVATE) {
                programModel.addElement(program);
            }
        } else if (selectedIndex > 1) { // "2", "3", atau "4"
            for (String program : PROGRAM_REGULAR) {
                programModel.addElement(program);
            }
        }
        
        cbxJenisProgram.setModel(programModel);
        
        if (currentProgram != null && programModel.getIndexOf(currentProgram) != -1) {
            cbxJenisProgram.setSelectedItem(currentProgram);
        } else {
            cbxJenisProgram.setSelectedIndex(0); 
        }
    }
    
    private void updateMediaPembelajaran() {
        if (cbxJenisProgram.getItemCount() == 0 || cbxJenisProgram.getSelectedIndex() <= 0) {
            cbxMediaPembelajaran.setSelectedIndex(0);
            cbxMediaPembelajaran.setEnabled(true);
            return;
        }

        String selectedProgram = cbxJenisProgram.getSelectedItem().toString();

        if (selectedProgram.equals("Regular Onsite")) {
            cbxMediaPembelajaran.setSelectedItem("Onsite class");
            cbxMediaPembelajaran.setEnabled(false);
        } else {
            cbxMediaPembelajaran.setSelectedItem("Online class");
            cbxMediaPembelajaran.setEnabled(false);
        }
    }
    
    private void updatePengajar() {
        Object currentPengajar = cbxPengajar.getSelectedItem();
        String[] listPengajar;

        if (cbxJenisProgram.getItemCount() == 0 || cbxJenisProgram.getSelectedIndex() <= 0) {
            listPengajar = PENGAJAR_SEMUA;
        } else {
            String selectedProgram = cbxJenisProgram.getSelectedItem().toString();
            
            switch (selectedProgram) {
                case "Regular Onsite":
                case "Private Music Production":
                    listPengajar = PENGAJAR_HAFZHJOYS_ENVICI;
                    break;
                
                case "Regular Mixing":
                case "Pro Mixing":
                    listPengajar = PENGAJAR_HAFZHJOYS_ONLY;
                    break;

                case "Electronic Music":
                    listPengajar = PENGAJAR_ENVICI_BADRUN;
                    break;

                case "Fundamentals Mixing":
                case "Mastering":
                case "Fundamentals Production":
                case "Creative Editing":
                    listPengajar = PENGAJAR_SEMUA;
                    break;
                    
                default:
                    listPengajar = PENGAJAR_SEMUA;
                    break;
            }
        }
        
        DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>(listPengajar);
        cbxPengajar.setModel(model);

        if (currentPengajar != null && model.getIndexOf(currentPengajar) != -1) {
            cbxPengajar.setSelectedItem(currentPengajar);
        } else {
            cbxPengajar.setSelectedIndex(0);
        }
    }

    private JSeparator createSeparator() {
        JSeparator separator = new JSeparator();
        separator.putClientProperty(FlatClientProperties.STYLE, "foreground:rgb(206,206,206)");
        return separator;
    }

    private boolean validasiInput(boolean isUpdate) {
        boolean valid = false;
        String namaMurid = txtNamaMurid.getText().trim();
        String currentNamaMurid = isUpdate ? model.getNamaMurid() : null;

        if (txtNamaMurid.getText().trim().isEmpty()) {
            Toast.show(this, Toast.Type.INFO, "Nama murid tidak boleh kosong", getOptionAlert());
        } else if (cbxJumlahMurid.getSelectedIndex() == 0) {
            Toast.show(this, Toast.Type.INFO, "Silakan pilih jumlah murid", getOptionAlert());
        } else if (cbxJenisProgram.getSelectedIndex() == 0) {
            Toast.show(this, Toast.Type.INFO, "Silakan pilih jenis program", getOptionAlert());
        } else if (cbxPengajar.getSelectedIndex() == 0) {
            Toast.show(this, Toast.Type.INFO, "Silakan pilih pengajar", getOptionAlert());
        } else if (cbxPreferensiWaktu.getSelectedIndex() == 0) {
            Toast.show(this, Toast.Type.INFO, "Silakan pilih preferensi waktu", getOptionAlert());
        } else if (cbxMediaPembelajaran.getSelectedIndex() == 0) {
            Toast.show(this, Toast.Type.INFO, "Silakan pilih media pembelajaran", getOptionAlert());
        } else {

            if (isUpdate && namaMurid.equals(currentNamaMurid)) {
                valid = true;
            } else {
                Murid modelMurid = new Murid();
                modelMurid.setNamaMurid(namaMurid);
                if (servis.validasiNamaMurid(modelMurid)) {
                    valid = true;
                } else {
                    Toast.show(this, Toast.Type.WARNING, "Nama murid sudah ada\nSilahkan masukkan nama murid lainnya", getOptionAlert());
                }
            }
        }
        return valid;
    }

    private void insertData() {
        if (validasiInput(false)) {
            Murid modelMurid = new Murid();
            
            String newKode = servis.generateKodeMurid();
            modelMurid.setKodeMurid(newKode);
            
            modelMurid.setNamaMurid(txtNamaMurid.getText());
            modelMurid.setJumlahMurid(cbxJumlahMurid.getSelectedItem().toString());
            modelMurid.setJenisProgram(cbxJenisProgram.getSelectedItem().toString());
            modelMurid.setPengajar(cbxPengajar.getSelectedItem().toString());
            modelMurid.setPreferensiWaktu(cbxPreferensiWaktu.getSelectedItem().toString());
            modelMurid.setMediaPembelajaran(cbxMediaPembelajaran.getSelectedItem().toString());

            servis.insertData(modelMurid);
            Toast.show(this, Toast.Type.SUCCESS, "Data has been successfully added", getOptionAlert());

            formMurid.refreshTable();
            resetForm();
        }
    }

    private void loadData() {
        btnSave.setText("Update");

        idMurid = model.getIdMurid();
        txtNamaMurid.setText(model.getNamaMurid());
        
        cbxJumlahMurid.removeActionListener(listenerJumlahMurid);
        cbxJenisProgram.removeActionListener(listenerJenisProgram);


        cbxPreferensiWaktu.setSelectedItem(model.getPreferensiWaktu());

        cbxJumlahMurid.setSelectedItem(model.getJumlahMurid());
        
        updateJenisProgram(); 
        cbxJenisProgram.setSelectedItem(model.getJenisProgram());
        
        updateMediaPembelajaran();
        updatePengajar();
        cbxPengajar.setSelectedItem(model.getPengajar());
        cbxMediaPembelajaran.setSelectedItem(model.getMediaPembelajaran());

        cbxJumlahMurid.addActionListener(listenerJumlahMurid);
        cbxJenisProgram.addActionListener(listenerJenisProgram);
    }

    private void updateData() {
        if (validasiInput(true)) {
            Murid modelMurid = new Murid();
            modelMurid.setIdMurid(idMurid);

            modelMurid.setKodeMurid(model.getKodeMurid());
            
            modelMurid.setNamaMurid(txtNamaMurid.getText());
            modelMurid.setJumlahMurid(cbxJumlahMurid.getSelectedItem().toString());
            modelMurid.setJenisProgram(cbxJenisProgram.getSelectedItem().toString());
            modelMurid.setPengajar(cbxPengajar.getSelectedItem().toString());
            modelMurid.setPreferensiWaktu(cbxPreferensiWaktu.getSelectedItem().toString());
            modelMurid.setMediaPembelajaran(cbxMediaPembelajaran.getSelectedItem().toString());

            servis.updateData(modelMurid);
            Toast.show(this, Toast.Type.SUCCESS, "Data has been successfully updated", getOptionAlert());

            formMurid.refreshTable();
            resetForm();
            ModalDialog.closeModal("Form Update");
        }
    }

    private void resetForm() {
        txtNamaMurid.setText("");
        cbxJumlahMurid.setSelectedIndex(0);
        cbxPreferensiWaktu.setSelectedIndex(0);
    }
}