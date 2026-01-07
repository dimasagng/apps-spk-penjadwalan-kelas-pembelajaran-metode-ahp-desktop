package com.spk.form;

import com.formdev.flatlaf.FlatClientProperties;
import com.spk.main.Form;
import com.spk.util.TabelUtils;
import java.awt.Insets;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextPane;
import net.miginfocom.swing.MigLayout;

public class FormDashboard extends Form{

    public FormDashboard() {
        init();
    }

    private void init() {
        setLayout(new MigLayout("insets 10, fill, wrap", "fill", "[]10[][fill, grow]"));
        add(panelTitle());
        add(panelDetail());
        add(panelTabel());
    }
    
    private JPanel panelTitle() {
        JPanel panel = new JPanel(new MigLayout("fill, insets 20"));
        panel.putClientProperty(FlatClientProperties.STYLE, "arc:10;background:rgb(255,255,255)");
        
        JLabel lbTitle = new JLabel();
        lbTitle.setText("<html>Sistem Pendukung Keputusan Penjadwalan Kelas Pembelajaran Dengan Metode <i>Analytic Hierarchy Process</i> (AHP)</html>");
        lbTitle.putClientProperty(FlatClientProperties.STYLE, "font:bold 16;foreground:@accentColor");
        
        panel.add(lbTitle);
        return panel;
    }
    
    private JPanel panelDetail(){
        JPanel panel = new JPanel(new MigLayout("fill, insets 20","fill","[]10[]10[]"));
        panel.putClientProperty(FlatClientProperties.STYLE, "arc:10;background:rgb(255,255,255)");
        
        JLabel lbTitle = new JLabel();
        lbTitle.setText("Metode Analytic Hierarchy Process (AHP)");
        lbTitle.putClientProperty(FlatClientProperties.STYLE, "font:bold 14");
        panel.add(lbTitle, "wrap");
        
        JTextPane textDefinisi = new JTextPane();
        textDefinisi.setText("Analytic Hierarchy Process (AHP) adalah metode pengambilan keputusan "
                + "yang membantu memilih alternatif terbaik berdasarkan berbagai kriteria. "
                + "AHP membandingkan elemen secara berpasangan dan menghitung bobot setiap elemen secara objektif.");
        textDefinisi.putClientProperty(FlatClientProperties.STYLE, "font:14");
        textDefinisi.setMargin(new Insets(0, 0, 0, 0));
        panel.add(textDefinisi, "wrap");
        
        JTextPane lbCara = new JTextPane();
        lbCara.setContentType("text/html");
        lbCara.setText("<html><b>Cara Penggunaan : </b><br>"
                + "1. Masukkan daftar murid dan kriteria. <br>"
                + "2. Lakukan perbandingan berpasangan antar kriteria. <br>"
                + "3. Lakukan perbandingan antar alternatif pada tiap kriteria. <br>"
                + "4. Sistem akan menghitung bobot dan menampilkan rekomendasi murid.</html>");
        lbCara.putClientProperty(FlatClientProperties.STYLE, "font:14");
        lbCara.setMargin(new Insets(0, 0, 0, 0));
        panel.add(lbCara, "wrap");
        
        JTextPane lbTips = new JTextPane();
        lbTips.setContentType("text/html");
        lbTips.setText("<html><b>Tips : </b><br>"
                + "💡 Gunakan skala 1–9 saat mengisi tabel perbandingan. <br>"
                + "💡 Jangan terlalu banyak kriteria agar pengambilan keputusan tetap efektif.</html>");
        lbTips.putClientProperty(FlatClientProperties.STYLE, "font:14");
        lbTips.setMargin(new Insets(0, 0, 0, 0));
        panel.add(lbTips, "wrap");
        
        return panel;
    }
    
    private JPanel panelTabel(){
        JPanel panel = new JPanel(new MigLayout("fill, insets 10 20 0 20", "fill", "[][][fill, grow]"));
        panel.putClientProperty(FlatClientProperties.STYLE, "arc:10;background:rgb(255,255,255)");
        
        JLabel lbTitle = new JLabel();
        lbTitle.setText("Tabel Skala Perbandingan Menurut Saaty (1980)");
        lbTitle.putClientProperty(FlatClientProperties.STYLE, "font:bold 14");
        panel.add(lbTitle,"wrap");
        
        JPanel panelSeparators = new JPanel(new MigLayout());
        panelSeparators.putClientProperty(FlatClientProperties.STYLE, "background:rgb(206,206,206)");
        panel.add(panelSeparators, "growx, h 1px!, wrap");
        
        String[] columnNames = {"Skala", "Tingkat Kepentingan", "Keterangan"};
        Object[][] data = {
                {"1", "Sama penting", "Kedua elemen sama penting"},
                {"3", "Sedikit lebih penting", "Pengalaman atau penilaian sedikit lebih menyukai satu elemen"},
                {"5", "Lebih penting", "Satu elemen sangat disukai dibandingkan lainnya"},
                {"7", "Sangat lebih penting", "Satu elemen jelas lebih disukai"},
                {"9", "Mutlak lebih penting", "Satu elemen mutlak lebih penting dari yang lain"},
                {"2, 4, 6, 8", "Nilai antara", "Digunakan untuk kompromi antara dua nilai sebelumnya"}
        };

        JTable saatyTable = new JTable(data, columnNames);
        JScrollPane scroll = new JScrollPane(saatyTable);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        
        TabelUtils.setColumnWidths(saatyTable, new int[]{0,1}, new int[]{70,150});
        TabelUtils.setHeaderAlignment(saatyTable, new int[]{0}, new int[]{JLabel.CENTER}, JLabel.LEFT);
        TabelUtils.setColumnAlignment(saatyTable, new int[]{0}, JLabel.CENTER);
        saatyTable.getTableHeader().putClientProperty(FlatClientProperties.STYLE, "" +
                "height:30;" +
                "hoverBackground:null;" +
                "pressedBackground:null;" +
                "separatorColor:$TableHeader.background;"
                + "font:14");
        saatyTable.putClientProperty(FlatClientProperties.STYLE, "" +
                "rowHeight:30;" +
                "showHorizontalLines:true;" +
                "intercellSpacing:0,1;" +
                "cellFocusColor:$TableHeader.hoverBackground;" +
                "selectionBackground:$TableHeader.hoverBackground;" +
                "selectionInactiveBackground:$TableHeader.hoverBackground;" +
                "selectionForeground:$Table.foreground;"
                + "font:14");
        
        panel.add(scroll);
        return panel;
    }
    
    
}
