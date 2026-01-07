package com.spk.form;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import com.spk.dao.KriteriaDAO;
import com.spk.dao.SubKriteriaDAO;
import com.spk.dao.PerbandinganSubKriteriaDAO;
import com.spk.main.Form;
import com.spk.model.Kriteria;
import com.spk.model.SubKriteria;
import com.spk.service.ServiceKriteria;
import com.spk.service.ServiceSubKriteria;
import com.spk.service.ServicePerbandinganSubKriteria;
import static com.spk.util.AlertUtils.getOptionAlert;
import com.spk.util.NumericFilter;
import com.spk.util.TabelUtils;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.AbstractDocument;
import net.miginfocom.swing.MigLayout;
import raven.modal.Toast;

public class FormPerbandinganSubKriteria extends Form {
    
    private final ServiceKriteria servisKriteria = new KriteriaDAO();
    private final ServiceSubKriteria servisSubKriteria = new SubKriteriaDAO();
    private final ServicePerbandinganSubKriteria servisPerbandingan = new PerbandinganSubKriteriaDAO();
    
    private List<SubKriteria> listSubKriteria;
    private JTextField[][] comboMatrix;
    private JTextField[] kolomJumlah;
    private JPanel panelMatrix;
    private JComboBox<Kriteria> cbxKriteriaInduk;
    private int n; // Jumlah sub-kriteria
    
    // Komponen baru untuk layout dinamis
    private JScrollPane scrollMatrix;
    //private JLabel lbPlaceholder;
    
    private JTable tblNormalisasi;
    private JPanel panelNormalisasi;
    private JPanel panelEvaluasi;
    private JLabel lbEvaluasi;

    private double[] bobotTerakhir;
    private double crTerakhir;

    public FormPerbandinganSubKriteria() {
        init();
        loadKriteriaInduk();
    }

    @Override
    public void formOpen() {
        super.formOpen();
        loadKriteriaInduk(); // Muat ulang data kriteria jika ada perubahan
        resetPerbandingan();
    }

   private void init() {
        // PERUBAHAN LAYOUT: Disesuaikan menjadi 7 komponen (seperti Alternatif)
        setLayout(new MigLayout("fillx, wrap", "[fill]", "[][][][][][grow, fill][grow, fill]"));
        add(setInfo());
        
        JPanel panelSeparator = new JPanel(new MigLayout());
        panelSeparator.putClientProperty(FlatClientProperties.STYLE, "background:rgb(206,206,206)");
        add(panelSeparator, "growx, h 1px!");
        
        // PERUBAHAN LAYOUT: Tombol dipindah ke atas
        add(setButton());
        
        // PERUBAHAN LAYOUT: ComboBox dibuat dan ditambahkan di luar panel putih
        cbxKriteriaInduk = new JComboBox<>();
        cbxKriteriaInduk.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boolean kriteriaDipilih = cbxKriteriaInduk.getSelectedIndex() > 0;

                if (kriteriaDipilih) {
                    Kriteria k = (Kriteria) cbxKriteriaInduk.getSelectedItem();
                    listSubKriteria = servisSubKriteria.getDataByKriteria(k.getIdKriteria());
                    n = (listSubKriteria != null) ? listSubKriteria.size() : 0;
                } else {
                    listSubKriteria = null;
                    n = 0;
                }
                
                // PERUBAHAN LOGIKA: Kontrol visibilitas placeholder dihapus
                // lbPlaceholder.setVisible(!kriteriaDipilih);
                // scrollMatrix.setVisible(kriteriaDipilih);

                buildMatrix(); // Bangun matriks (akan kosong jika n=0)
                clearHasilPerhitungan(); // Bersihkan hasil lama
            }
        });
        
        // PERUBAHAN LAYOUT: ComboBox ditambahkan ke Form (layout utama)
        add(cbxKriteriaInduk, "growx");

        // Panel Matriks Utama (Panel Putih)
        // PERUBAHAN LAYOUT: Panel ini sekarang HANYA berisi Judul Matriks dan Matriks
        JPanel panel = new JPanel(new MigLayout("fill, insets 10", "[fill]", "[][][fill]")); // Layout diubah
        panel.putClientProperty(FlatClientProperties.STYLE, "arc:10;background:rgb(255,255,255)");

        // --- Sisa panel putih ---

        JLabel lbMatrix = new JLabel("Matriks Perbandingan Sub Kriteria");
        JPanel panelSeparators = new JPanel(new MigLayout());
        panelSeparators.putClientProperty(FlatClientProperties.STYLE, "background:rgb(206,206,206)");

        // Panel untuk matriks (di dalam scrollpane)
        panelMatrix = new JPanel(new MigLayout("wrap 1")); // Layout awal
        panelMatrix.putClientProperty(FlatClientProperties.STYLE, "background:rgb(255,255,255)");
        
        scrollMatrix = new JScrollPane(panelMatrix);
        scrollMatrix.setBorder(BorderFactory.createEmptyBorder());
        scrollMatrix.getVerticalScrollBar().setUnitIncrement(10);
        // PERUBAHAN LOGIKA: Dihapus -> scrollMatrix.setVisible(false); (Sekarang selalu terlihat)

        // PERUBAHAN LOGIKA: Placeholder Label dihapus
        
        // Tambahkan komponen ke panel putih
        // PERUBAHAN LAYOUT: ComboBox dihapus dari sini
        panel.add(lbMatrix, "wrap"); // Judul
        panel.add(panelSeparators, "growx, h 1px!, wrap"); // Garis
        
        // PERUBAHAN LAYOUT: Hanya scrollMatrix yang ditambahkan (placeholder dihapus)
        panel.add(scrollMatrix, "hmin 200, growx");

        add(panel); // Tambahkan panel putih utama ke form

        // Panel Normalisasi & Evaluasi (Tidak berubah)
        panelNormalisasi = new JPanel(new MigLayout("fill, insets 10", "fill", "[fill]"));
        panelNormalisasi.putClientProperty(FlatClientProperties.STYLE, "arc:10;background:rgb(255,255,255)");
        add(panelNormalisasi);

        panelEvaluasi = new JPanel(new MigLayout("fill", "[][grow]"));
        panelEvaluasi.putClientProperty(FlatClientProperties.STYLE, "arc:10;background:rgb(255,255,255)");
        add(panelEvaluasi);
    }
    private JPanel setInfo() {
        JPanel panel = new JPanel(new MigLayout("fillx"));
        panel.putClientProperty(FlatClientProperties.STYLE, "arc:10");
        JLabel lbTitle = new JLabel("Input Perbandingan Sub Kriteria (AHP)");
        lbTitle.putClientProperty(FlatClientProperties.STYLE, "font:bold 18");
        panel.add(lbTitle);
        return panel;
    }
    
    // PERUBAHAN LAYOUT: Metode setKriteriaInduk() dihapus karena ComboBox dipindah ke init()

    private void loadKriteriaInduk() {
        DefaultComboBoxModel<Kriteria> model = new DefaultComboBoxModel<>();
        model.addElement(new Kriteria(0, "Pilih Kriteria")); // Teks disesuaikan
        List<Kriteria> list = servisKriteria.getData();
        for (Kriteria k : list) {
            model.addElement(k);
        }
        cbxKriteriaInduk.setModel(model);
    }
    
    private JPanel setButton() {
        JPanel panel = new JPanel(new MigLayout("wrap", "[][][]"));
        panel.putClientProperty(FlatClientProperties.STYLE, "arc:10");

        JButton btnProses = new JButton("Proses");
        btnProses.putClientProperty(FlatClientProperties.STYLE, "background:@accentColor;foreground:rgb(255,255,255)");
        btnProses.setIcon(new FlatSVGIcon("com/spk/icon/process.svg", 0.4f));
        btnProses.addActionListener((e) -> {
            prosesPerbandinganSubKriteria();
        });
        
        JButton btnSimpan = new JButton("Simpan");
        btnSimpan.setIcon(new FlatSVGIcon("com/spk/icon/save.svg", 0.4f));
        btnSimpan.addActionListener((e) -> {
            simpanBobotSubKriteria();
        });   

        JButton btnReset = new JButton("Reset");
        btnReset.setIcon(new FlatSVGIcon("com/spk/icon/reset.svg", 0.4f));
        btnReset.addActionListener((e) -> {
            resetPerbandingan();
        });

        panel.add(btnProses, "hmin 30, wmin 50");
        panel.add(btnSimpan, "hmin 30, wmin 50");
        panel.add(btnReset, "hmin 30, wmin 50");
        return panel;
    }

    private void buildMatrix() {
        panelMatrix.removeAll();

        // PERUBAHAN LOGIKA: Placeholder sekarang ditangani di ActionListener ComboBox
        if (n == 0 || listSubKriteria == null) {
            panelMatrix.revalidate();
            panelMatrix.repaint();
            return;
        }
        
        comboMatrix = new JTextField[n][n];
        kolomJumlah = new JTextField[n];

        panelMatrix.setLayout(new MigLayout("wrap " + (n + 1), "[]".repeat(n + 1)));
        
        // PERUBAHAN LOGIKA: Tambahkan header hanya jika n > 0 (konsisten dgn Alternatif)
        if(n != 0) {
            panelMatrix.add(new JLabel("Kriteria"));
        }
        
        for (SubKriteria k : listSubKriteria) {
            panelMatrix.add(new JLabel(k.getNamaSubKriteria(), SwingConstants.CENTER));
        }

        for (int i = 0; i < n; i++) {
            panelMatrix.add(new JLabel(listSubKriteria.get(i).getNamaSubKriteria()));
            for (int j = 0; j < n; j++) {
                JTextField field = new JTextField();
                if (i == j) {
                    field.setText("1");
                    field.setEditable(false);
                } else if (i < j) {
                    field.setText("");
                    ((AbstractDocument) field.getDocument()).setDocumentFilter(new NumericFilter());
                } else {
                    field.setEditable(false);
                }
                comboMatrix[i][j] = field;
                // PERUBAHAN LAYOUT: Menghapus 'w 50!' agar lebar kolom otomatis (seperti di FormPerbandinganAlternatif)
                panelMatrix.add(field, "growx"); 
            }
        }
        
        // PERUBAHAN LOGIKA: Tambahkan header hanya jika n > 0
        if(n != 0) {
           panelMatrix.add(new JLabel("Jumlah"));
        }
        for (int j = 0; j < n; j++) {
            JTextField jumlahField = new JTextField();
            jumlahField.setEditable(false);
            kolomJumlah[j] = jumlahField;
            panelMatrix.add(jumlahField, "growx"); // Menghapus 'w 50!'
        }

        panelMatrix.revalidate();
        panelMatrix.repaint();
    }
    
    private void prosesPerbandinganSubKriteria() {
        if (cbxKriteriaInduk.getSelectedIndex() == 0 || n == 0) {
             Toast.show(this, Toast.Type.INFO, "Silakan pilih kriteria terlebih dahulu", getOptionAlert());
             return;
        }
        
        Kriteria kriteriaInduk = (Kriteria) cbxKriteriaInduk.getSelectedItem();
        servisPerbandingan.deleteData(kriteriaInduk.getIdKriteria());

        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                String valStr = comboMatrix[i][j].getText();
                if (valStr == null || valStr.trim().isEmpty()) {
                    Toast.show(this, Toast.Type.INFO, "Nilai belum lengkap di baris " + (i + 1) + ", kolom " + (j + 1), getOptionAlert());
                    return;
                }
                double nilai;
                try {
                    nilai = Double.parseDouble(valStr);
                    if (nilai <= 0) throw new NumberFormatException();
                } catch (NumberFormatException e) {
                    Toast.show(this, Toast.Type.INFO, "Nilai tidak valid di baris " + (i + 1) + ", kolom " + (j + 1), getOptionAlert());
                    return;
                }
                int id1 = listSubKriteria.get(i).getIdSubKriteria();
                int id2 = listSubKriteria.get(j).getIdSubKriteria();
                
                servisPerbandingan.insertData(kriteriaInduk.getIdKriteria(), id1, id2, nilai);
                servisPerbandingan.insertData(kriteriaInduk.getIdKriteria(), id2, id1, 1.0/nilai);

                comboMatrix[j][i].setText(String.format("%.3f", 1.0/nilai));
            }
        }
        
        hitungJumlahKolom();
        tampilNormalisasi();
        Toast.show(this, Toast.Type.SUCCESS, "Perbandingan berhasil diproses. Klik simpan jika sudah sesuai", getOptionAlert());
    }

    private void hitungJumlahKolom() {
        for (int j = 0; j < n; j++) {
            double sum = 0.0;
            for (int i = 0; i < n; i++) {
                try {
                    String val = comboMatrix[i][j].getText();
                    if (val != null && !val.trim().isEmpty()) {
                        sum += Double.parseDouble(val.trim());
                    }
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }
            if (sum % 1 == 0) {
                kolomJumlah[j].setText(String.format("%.0f", sum));
            } else {
                kolomJumlah[j].setText(String.format("%.3f", sum));
            }
        }
    }

    private void tampilNormalisasi() {
        Kriteria kriteriaInduk = (Kriteria) cbxKriteriaInduk.getSelectedItem();
        
        // Mencegah error jika proses ditekan saat n=0
        if (n == 0 || listSubKriteria == null) return;

        String[] kolom = new String[n + 2];
        kolom[0] = "Kriteria";
        for (int i = 0; i < n; i++) {
            kolom[i + 1] = listSubKriteria.get(i).getNamaSubKriteria();
        }
        kolom[n + 1] = "Bobot";

        Object[][] data = new Object[n + 3][n + 3];

        double[][] matrix = new double[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (i == j) {
                    matrix[i][j] = 1.0;
                } else {
                    Double nilai = servisPerbandingan.getNilai(
                            kriteriaInduk.getIdKriteria(),
                            listSubKriteria.get(i).getIdSubKriteria(),
                            listSubKriteria.get(j).getIdSubKriteria()
                    );
                    matrix[i][j] = (nilai != null) ? nilai : 0;
                }
            }
        }
    
        double[] colSum = new double[n];
        for (int j = 0; j < n; j++) {
            for (int i = 0; i < n; i++) {
                colSum[j] += matrix[i][j];
            }
        }

        double[][] normalisasiMatrix = new double[n][n];
        double[] bobot = new double[n];
        for (int i = 0; i < n; i++) {
            double sum = 0;
            for (int j = 0; j < n; j++) {
                // Mencegah NaN (Not a Number) jika colSum[j] adalah 0
                normalisasiMatrix[i][j] = (colSum[j] != 0) ? (matrix[i][j] / colSum[j]) : 0;
                sum += normalisasiMatrix[i][j];
            }
            bobot[i] = (n != 0) ? (sum / n) : 0;
        }

        double[] prioritasVektor = new double[n];
        double lambdaMax = 0;
        for (int i = 0; i < n; i++) {
            double rowSum = 0;
            for (int j = 0; j < n; j++) {
                rowSum += matrix[i][j] * bobot[j];
            }
            prioritasVektor[i] = rowSum;
            // Mencegah NaN jika bobot[i] adalah 0
            lambdaMax += (bobot[i] != 0) ? (rowSum / bobot[i]) : 0;
        }
        lambdaMax = (n != 0) ? (lambdaMax / n) : 0;

        double ci = (n > 1) ? (lambdaMax - n) / (n - 1) : 0;
        double[] ir = {0.00, 0.00, 0.58, 0.90, 1.12, 1.24, 1.32, 1.41, 1.45, 1.49, 1.51, 1.53, 1.56, 1.57, 1.59};
        double cr = 0;
        // Penyesuaian pengecekan batas array ir
        if (n >= 1 && (n-1) < ir.length) {
            cr = (ir[n - 1] == 0) ? 0 : (ci / ir[n - 1]);
        }

        for (int i = 0; i < n; i++) {
            data[i][0] = listSubKriteria.get(i).getNamaSubKriteria();
            for (int j = 0; j < n; j++) {
                data[i][j + 1] = String.format("%.3f", normalisasiMatrix[i][j]);
            }
            data[i][n + 1] = String.format("%.3f", bobot[i]);
        }
        
        bobotTerakhir = bobot;
        crTerakhir = cr;

        data[n][0] = "Principle Eigen Vector (λ maks)";
        data[n][n + 1] = String.format("%.3f", lambdaMax);

        data[n + 1][0] = "Consistency Index";
        data[n + 1][n + 1] = String.format("%.3f", ci);

        data[n + 2][0] = "Consistency Ratio";
        data[n + 2][n + 1] = String.format("%.0f %%", cr * 100);

        DefaultTableModel model = new DefaultTableModel(data, kolom) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        panelNormalisasi.removeAll();
        tblNormalisasi = null;

        JLabel lbMatriksNormalisasi = new JLabel("Matriks Normalisasi");
        JPanel panelSeparator = new JPanel(new MigLayout());
        panelSeparator.putClientProperty(FlatClientProperties.STYLE, "background:rgb(206,206,206)");

        panelNormalisasi.add(lbMatriksNormalisasi, "wrap");
        panelNormalisasi.add(panelSeparator, "growx, h 1px!, wrap");
        
    if (tblNormalisasi == null) {
        tblNormalisasi = new JTable(model);
        JScrollPane scroll = new JScrollPane(tblNormalisasi);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        panelNormalisasi.add(scroll, "wrap, growx");
    } else {
        tblNormalisasi.setModel(model);
    }

    panelNormalisasi.revalidate();
    panelNormalisasi.repaint();
    
        TabelUtils.setColumnWidths(tblNormalisasi, new int[]{0}, new int[]{250});
        TabelUtils.setHeaderAlignment(tblNormalisasi, new int[]{0}, new int[]{JLabel.LEFT}, JLabel.LEFT);

        tblNormalisasi.getTableHeader().putClientProperty(FlatClientProperties.STYLE, ""
                + "height:30;"
                + "hoverBackground:null;"
                + "pressedBackground:null;"
                + "separatorColor:$TableHeader.background;");

        tblNormalisasi.putClientProperty(FlatClientProperties.STYLE, ""
                + "rowHeight:30;"
                + "showHorizontalLines:true;"
                + "intercellSpacing:0,1;"
                + "cellFocusColor:$TableHeader.hoverBackground;"
                + "selectionBackground:$TableHeader.hoverBackground;"
                + "selectionInactiveBackground:$TableHeader.hoverBackground;"
                + "selectionForeground:$Table.foreground;");

        lbEvaluasi = new JLabel();
        lbEvaluasi.putClientProperty(FlatClientProperties.STYLE, "font:bold italic 14");
        panelEvaluasi.removeAll();
        JLabel lbLogoHasil;
        Color colorStatus;

        String status;
        if (cr < 0.1) {
            status = "Konsistensi OK ✅ (CR = " + String.format("%.3f", cr) + ")";
            lbLogoHasil = new JLabel(new FlatSVGIcon("com/spk/icon/ok.svg", 50, 50));
            colorStatus = new Color(114, 202, 175);
        } else {
            status = "Konsistensi buruk ❌ (CR = " + String.format("%.3f", cr) + ")";
            lbLogoHasil = new JLabel(new FlatSVGIcon("com/spk/icon/attention.svg", 50, 50));
            colorStatus = Color.RED;

            panelEvaluasi.add(lbLogoHasil, "align left");
            panelEvaluasi.add(lbEvaluasi, "align left");
        }

        // Hanya tambahkan icon & label jika status adalah 'buruk' atau 'OK'
        lbEvaluasi.setText("Evaluasi Konsistensi : " + status);
        lbEvaluasi.setForeground(colorStatus);
        
        panelEvaluasi.add(lbLogoHasil, "align left");
        panelEvaluasi.add(lbEvaluasi, "align left");

        panelEvaluasi.revalidate();
        panelEvaluasi.repaint();
    }

        private void simpanBobotSubKriteria() {
        if (bobotTerakhir == null || listSubKriteria == null) {
            Toast.show(this, Toast.Type.INFO, "Silakan proses perbandingan terlebih dahulu", getOptionAlert());
            return;
        }
        if (crTerakhir >= 0.1) {
            Toast.show(this, Toast.Type.ERROR, "Konsistensi buruk! (CR ≥ 0.1), Bobot tidak dapat disimpan", getOptionAlert());
            return;
        }
        for (int i = 0; i < n; i++) {
            int idSubKriteria = listSubKriteria.get(i).getIdSubKriteria();
            servisSubKriteria.updateBobot(idSubKriteria, bobotTerakhir[i]);
        }
        resetPerbandingan();
        Toast.show(this, Toast.Type.SUCCESS, "Bobot sub kriteria berhasil disimpan", getOptionAlert());
    }
        
        private void resetPerbandingan() {
        cbxKriteriaInduk.setSelectedIndex(0); // Reset ComboBox
        
        // PERUBAHAN LOGIKA: Listener ComboBox akan menangani sisanya (menyembunyikan matriks, menampilkan placeholder)
        
        // Bersihkan hasil secara manual untuk memastikan
        clearHasilPerhitungan();
    }  
        
        private void clearHasilPerhitungan() {
        panelNormalisasi.removeAll();
        panelNormalisasi.revalidate();
        panelNormalisasi.repaint();
        panelEvaluasi.removeAll();
        panelEvaluasi.revalidate();
        panelEvaluasi.repaint();
        tblNormalisasi = null;
        bobotTerakhir = null;
        crTerakhir = 0;
    }
}