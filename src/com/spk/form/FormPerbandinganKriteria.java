package com.spk.form;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import com.spk.dao.KriteriaDAO;
import com.spk.dao.PerbandinganKriteriaDAO;
import com.spk.main.Form;
import com.spk.model.Kriteria;
import com.spk.service.ServiceKriteria;
import com.spk.service.ServicePerbandinganKriteria;
import static com.spk.util.AlertUtils.getOptionAlert;
import com.spk.util.NumericFilter;
import com.spk.util.TabelUtils;
import java.awt.Color;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
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

public class FormPerbandinganKriteria extends Form {
    
    private final ServiceKriteria servisKriteria = new KriteriaDAO();
    private final ServicePerbandinganKriteria servisPerbandinganKriteria = new PerbandinganKriteriaDAO();
    
    private List<Kriteria> listKriteria;
    private JTextField[][] comboMatrix;
    private JTextField[] kolomJumlah;
    private JPanel panelMatrix;
    private int n;
    
    private JTable tblNormalisasi;
    private JPanel panelNormalisasi;
    private JPanel panelEvaluasi;
    private JLabel lbEvaluasi;

    private double[] bobotTerakhir;
    private double crTerakhir;

    public FormPerbandinganKriteria() {
        init();
    }

    @Override
    public void formOpen() {
        super.formOpen();
        resetPerbandingan();
    }

    private void init() {
        setLayout(new MigLayout("fillx, wrap", "[fill]", "[][][][][grow, fill][grow, fill]"));
        add(setInfo());
        
        JPanel panelSeparator = new JPanel(new MigLayout());
        panelSeparator.putClientProperty(FlatClientProperties.STYLE, "background:rgb(206,206,206)");
        add(panelSeparator, "growx, h 1px!");
        add(setButton());
        
        listKriteria = servisKriteria.getData();
        n = listKriteria.size();
        comboMatrix = new JTextField[n][n];
        kolomJumlah = new JTextField[n];

        JPanel panel = new JPanel(new MigLayout("fill, insets 10", "", "[fill]"));
        panel.putClientProperty(FlatClientProperties.STYLE, "arc:10;background:rgb(255,255,255)");
        panelMatrix = new JPanel(new MigLayout("wrap " + (n + 1), "[]".repeat(n + 1)));
        panelMatrix.putClientProperty(FlatClientProperties.STYLE, "arc:10;background:rgb(255,255,255)");
        buildMatrix();
        JScrollPane scroll = new JScrollPane(panelMatrix);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.getVerticalScrollBar().setUnitIncrement(10);

        JLabel lbMatrix = new JLabel("Matriks Perbandingan Kriteria");
        JPanel panelSeparators = new JPanel(new MigLayout());
        panelSeparators.putClientProperty(FlatClientProperties.STYLE, "background:rgb(206,206,206)");

        panel.add(lbMatrix, "wrap");
        panel.add(panelSeparators, "growx, h 1px!, wrap");
        panel.add(scroll, "hmin 200, growx");
        add(panel);
        
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

        JLabel lbTitle = new JLabel("Input Perbandingan Kriteria (AHP)");
        lbTitle.putClientProperty(FlatClientProperties.STYLE, "font:bold 18");

        panel.add(lbTitle);
        return panel;
    }
    
    private JPanel setButton() {
    JPanel panel = new JPanel(new MigLayout("wrap", "[][][]"));
    panel.putClientProperty(FlatClientProperties.STYLE, "arc:10");

    JButton btnProses = new JButton("Proses");
    btnProses.putClientProperty(FlatClientProperties.STYLE, "background:@accentColor;foreground:rgb(255,255,255)");
    btnProses.setIcon(new FlatSVGIcon("com/spk/icon/process.svg", 0.4f));
    btnProses.setIconTextGap(5);
    btnProses.addActionListener((e) -> {
        prosesPerbandinganKriteria();
    });
    
    JButton btnSimpan = new JButton("Simpan");
    btnSimpan.setIcon(new FlatSVGIcon("com/spk/icon/save.svg", 0.4f));
    btnSimpan.setIconTextGap(5);
    btnSimpan.addActionListener((e) -> {
        simpanBobotKriteria();
     });   

    JButton btnReset = new JButton("Reset");
    btnReset.setIcon(new FlatSVGIcon("com/spk/icon/reset.svg", 0.4f));
    btnReset.setIconTextGap(5);
    btnReset.addActionListener((e) -> {
        resetPerbandingan();
    });

    panel.add(btnProses, "hmin 30, wmin 50");
    panel.add(btnSimpan, "hmin 30, wmin 50");
    panel.add(btnReset, "hmin 30, wmin 50");

    return panel;
}

    private void buildMatrix() {
        listKriteria = servisKriteria.getData();
        n = listKriteria.size();
        comboMatrix = new JTextField[n][n];
        kolomJumlah = new JTextField[n];

        panelMatrix.removeAll();
        panelMatrix.setLayout(new MigLayout("wrap " + (n + 1), "[]".repeat(n + 1)));
        if(n != 0) {
            panelMatrix.add(new JLabel("Kriteria"));
    }
        
        for (Kriteria k : listKriteria) {
            panelMatrix.add(new JLabel(k.getNamaKriteria(), SwingConstants.CENTER));
        }

        for (int i = 0; i < n; i++) {
            panelMatrix.add(new JLabel(listKriteria.get(i).getNamaKriteria()));
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
                panelMatrix.add(field);
            }
        }
        
                if(n != 0) {
            panelMatrix.add(new JLabel("Jumlah"));
    }
        for (int j = 0; j < n; j++) {
            JTextField jumlahField = new JTextField();
            jumlahField.setEditable(false);
            kolomJumlah[j] = jumlahField;
            panelMatrix.add(jumlahField);
        }

        panelMatrix.revalidate();
        panelMatrix.repaint();
    }
    
    private void prosesPerbandinganKriteria() {
         servisPerbandinganKriteria.deleteData();

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

                 int id1 = listKriteria.get(i).getIdKriteria();
                 int id2 = listKriteria.get(j).getIdKriteria();

                 servisPerbandinganKriteria.insertData(id1, id2, nilai);
                 servisPerbandinganKriteria.insertData(id2, id1, 1.0/nilai);

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
        String[] kolom = new String[n + 2];
        kolom[0] = "Kriteria";
        for (int i = 0; i < n; i++) {
            kolom[i + 1] = listKriteria.get(i).getNamaKriteria();
        }
        kolom[n + 1] = "Bobot";

        Object[][] data = new Object[n + 3][n + 3];

        double[][] matrix = new double[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (i == j) {
                    matrix[i][j] = 1.0;
                } else {
                    matrix[i][j] = servisPerbandinganKriteria.getNilai(
                            listKriteria.get(i).getIdKriteria(),
                            listKriteria.get(j).getIdKriteria()
                    );
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
                normalisasiMatrix[i][j] = matrix[i][j] / colSum[j];
                sum += normalisasiMatrix[i][j];
            }

            bobot[i] = sum / n;
        }

        double[] prioritasVektor = new double[n];
        double lambdaMax = 0;
        for (int i = 0; i < n; i++) {
            double rowSum = 0;
            for (int j = 0; j < n; j++) {
                rowSum += matrix[i][j] * bobot[j];
            }
            prioritasVektor[i] = rowSum;
            lambdaMax += rowSum / bobot[i];
        }
        lambdaMax /= n;

        double ci = (lambdaMax - n) / (n - 1);
        double[] ir = {0.00, 0.00, 0.58, 0.90, 1.12, 1.24, 1.32, 1.41, 1.45, 1.49};
        double cr = (n <= 10) ? (ci / ir[n - 1]) : 0;

        for (int i = 0; i < n; i++) {
            data[i][0] = listKriteria.get(i).getNamaKriteria();
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

        lbEvaluasi.setText("Evaluasi Konsistensi : " + status);
        lbEvaluasi.setForeground(colorStatus);

        panelEvaluasi.add(lbLogoHasil, "align left");
        panelEvaluasi.add(lbEvaluasi, "align left");

        panelEvaluasi.revalidate();
        panelEvaluasi.repaint();
    }

        private void simpanBobotKriteria() {
            if (crTerakhir >= 0.1) {
                Toast.show(this, Toast.Type.ERROR, "Konsistensi buruk! (CR ≥ 0.1), Silahkan input ulang data perbandingan", getOptionAlert());
                return;
            }

            for (int i = 0; i < n; i++) {
                int idKriteria = listKriteria.get(i).getIdKriteria();
                servisKriteria.updateBobot(idKriteria, bobotTerakhir[i]);
            }
            
            resetPerbandingan();
            Toast.show(this, Toast.Type.SUCCESS, "Bobot kriteria berhasil disimpan", getOptionAlert());
        }
        
        private void resetPerbandingan() {
        panelMatrix.removeAll();
        panelMatrix.repaint();
        panelMatrix.revalidate();
        buildMatrix();
        panelNormalisasi.removeAll();
        panelNormalisasi.repaint();
        panelNormalisasi.revalidate();
        panelEvaluasi.removeAll();
        panelEvaluasi.repaint();
        panelEvaluasi.revalidate();
        tblNormalisasi = null;
    }  

}