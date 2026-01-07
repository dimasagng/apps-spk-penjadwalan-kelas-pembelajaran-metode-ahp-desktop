package com.spk.form;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.spk.dao.HasilAkhirDAO;
import com.spk.dao.MuridDAO;
import com.spk.dao.KriteriaDAO;
import com.spk.main.Form;
import com.spk.model.HasilAkhir;
import com.spk.model.Murid;
import com.spk.model.Kriteria;
import com.spk.service.ServiceHasilAkhir;
import com.spk.service.ServiceKriteria;
import com.spk.util.TabelUtils;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import net.miginfocom.swing.MigLayout;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import com.spk.service.ServiceMurid;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class FormHasilPerhitungan extends Form {
    
    private final ServiceHasilAkhir servisHasilAkhir = new HasilAkhirDAO();
    private final ServiceKriteria servisKriteria = new KriteriaDAO();
    private final ServiceMurid servisMurid= new MuridDAO();
    
    private JTable tblHasil, tblRanking;
    private JPanel panelHasil, panelRanking;
    private JButton btnExportPDF, btnExportExcel;

    private List<HasilAkhir> listHasilAkhir = new ArrayList<>();

    public FormHasilPerhitungan() {
        init();
    }

    private void init() {
        setLayout(new MigLayout("fillx, wrap", "[fill]", "[][][][][grow, fill][grow, fill]"));
        add(setInfo());
        
        JPanel panelSeparator = new JPanel(new MigLayout());
        panelSeparator.putClientProperty(FlatClientProperties.STYLE, "background:rgb(206,206,206)");
        add(panelSeparator, "growx, h 1px!");
        add(setButton());
        
        panelHasil = new JPanel(new MigLayout("fill", "fill", "fill"));
        panelHasil.putClientProperty(FlatClientProperties.STYLE, "arc:10;background:rgb(255,255,255)");
        tblHasil = new JTable();
        JScrollPane scroll = new JScrollPane(tblHasil);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.getVerticalScrollBar().setUnitIncrement(10);

        JLabel lbHasil = new JLabel("Hasil Perhitungan");
        lbHasil.putClientProperty(FlatClientProperties.STYLE, "font:bold 14 ");
        JPanel panelSeparators = new JPanel(new MigLayout());
        panelSeparators.putClientProperty(FlatClientProperties.STYLE, "background:rgb(206,206,206)");

        panelHasil.add(lbHasil, "wrap");
        panelHasil.add(panelSeparators, "growx, h 1px!, wrap");
        panelHasil.add(scroll, "hmin 200, growx");
        add(panelHasil);
        
        panelRanking = new JPanel(new MigLayout("fill", "fill", "fill"));
        panelRanking.putClientProperty(FlatClientProperties.STYLE, "arc:10;background:rgb(255,255,255)");
        tblRanking = new JTable();
        JScrollPane scrolls = new JScrollPane(tblRanking);
        scrolls.setBorder(BorderFactory.createEmptyBorder());
        scrolls.getVerticalScrollBar().setUnitIncrement(10);

        JLabel lbRanking = new JLabel("Perangkingan");
        lbRanking.putClientProperty(FlatClientProperties.STYLE, "font:bold 14 ");
        JPanel panelSeparatorRanking = new JPanel(new MigLayout());
        panelSeparatorRanking.putClientProperty(FlatClientProperties.STYLE, "background:rgb(206,206,206)");
        
        panelRanking.add(lbRanking, "wrap");
        panelRanking.add(panelSeparatorRanking, "growx, h 1px!, wrap");
        panelRanking.add(scrolls, "hmin 200, growx");
        add(panelRanking);
    }
    private JPanel setInfo() {
        JPanel panel = new JPanel(new MigLayout("fillx"));
        panel.putClientProperty(FlatClientProperties.STYLE, "arc:10");

        JLabel lbTitle = new JLabel("Hasil Akhir");
        lbTitle.putClientProperty(FlatClientProperties.STYLE, "font:bold 18");

        panel.add(lbTitle);
        return panel;
    }
    
    private JPanel setButton() {
        JPanel panel = new JPanel(new MigLayout("wrap", "[][][]"));
        panel.putClientProperty(FlatClientProperties.STYLE, "arc:10");

        JButton btnHitung = new JButton("Hitung Hasil Akhir");
        btnHitung.putClientProperty(FlatClientProperties.STYLE, "background:@accentColor;foreground:rgb(255,255,255)");
        btnHitung.setIcon(new FlatSVGIcon("com/spk/icon/process.svg", 0.4f));
        btnHitung.setIconTextGap(5);
        btnHitung.addActionListener((e) -> {
            simpanHasilAkhir();
        });

        btnExportPDF = new JButton("PDF");
        btnExportPDF.setIcon(new FlatSVGIcon("com/spk/icon/pdf.svg", 0.4f));
        btnExportPDF.setIconTextGap(5);
        btnExportPDF.addActionListener((e) -> {
            JFileChooser chooser = new JFileChooser();
            if (chooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
                File file = chooser.getSelectedFile();
                if (!file.getName().toLowerCase().endsWith(".pdf")) {
                    file = new File(file.getAbsolutePath() + ".pdf");
                }
                exportToPDF(file);
            }
        });

        btnExportExcel = new JButton("Excel");
        btnExportExcel.setIcon(new FlatSVGIcon("com/spk/icon/xls.svg", 0.4f));
        btnExportExcel.setIconTextGap(5);
        btnExportExcel.addActionListener((e) -> {
            JFileChooser chooser = new JFileChooser();
            if (chooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
                File file = chooser.getSelectedFile();
                if (!file.getName().toLowerCase().endsWith(".xlsx")) {
                    file = new File(file.getAbsolutePath() + ".xlsx");
                }
                exportToExcel(file);
            }
        });

        panel.add(btnHitung, "hmin 30, wmin 50");
        panel.add(btnExportPDF, "hmin 30, wmin 50");
        panel.add(btnExportExcel, "hmin 30, wmin 50");

        return panel;
    }

    private void simpanHasilAkhir() {
        servisHasilAkhir.deleteHasilAkhir();

        List<Kriteria> listKriteria = servisKriteria.getData();
        List<Murid> listMurid = servisMurid.getData();
        Map<Integer, Double> bobotKriteria = servisHasilAkhir.getBobotKriteria();

        for (Murid s : listMurid) {
            double total = 0;
            for (Kriteria k : listKriteria) {
                double bobotAlternatif = servisHasilAkhir.getBobotAlternatif(k.getIdKriteria(), s.getIdMurid());
                double nilai = bobotAlternatif * bobotKriteria.getOrDefault(k.getIdKriteria(), 0.0);
                total += nilai;
            }

            servisHasilAkhir.insertHasilAkhir(s.getIdMurid(), total);
            tampilHasilAkhir();
            setStyleTable();
            btnExportPDF.setEnabled(true);
            btnExportExcel.setEnabled(true);
        }
    }

    private void tampilHasilAkhir() {
        List<Kriteria> listKriteria = servisKriteria.getData();
        List<Murid> listMurid = servisMurid.getData();
        Map<Integer, Double> bobotKriteria = servisHasilAkhir.getBobotKriteria();

        String[] kolom = new String[listKriteria.size() + 2];
        kolom[0] = "Murid";
        for (int i = 0; i < listKriteria.size(); i++) {
            kolom[i + 1] = listKriteria.get(i).getNamaKriteria();
        }
        kolom[kolom.length - 1] = "Total";

        Object[][] data = new Object[listMurid.size()][kolom.length];
        listHasilAkhir.clear();

        for (int i = 0; i < listMurid.size(); i++) {
            Murid s = listMurid.get(i);
            Map<String, Double> nilaiPerKriteria = new HashMap<>();
            double total = 0;

            data[i][0] = s.getNamaMurid();

            for (int j = 0; j < listKriteria.size(); j++) {
                Kriteria k = listKriteria.get(j);
                double bobotAlternatif = servisHasilAkhir.getBobotAlternatif(k.getIdKriteria(), s.getIdMurid());
                double nilai = bobotAlternatif * bobotKriteria.getOrDefault(k.getIdKriteria(), 0.0);

                nilaiPerKriteria.put(k.getNamaKriteria(), nilai);
                total += nilai;
                data[i][j + 1] = String.format("%.3f", nilai);
            }
            data[i][kolom.length - 1] = String.format("%.3f", total);

            HasilAkhir hasil = new HasilAkhir(s.getNamaMurid(), nilaiPerKriteria, total, 0);
            listHasilAkhir.add(hasil);
        }
        
        tblHasil.setModel(new DefaultTableModel(data, kolom) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        });

        listHasilAkhir.sort((a, b) -> Double.compare(b.getTotal(), a.getTotal()));
        for (int i = 0; i < listHasilAkhir.size(); i++) {
            listHasilAkhir.get(i).setRanking(i + 1);
        }

        String[] kolomRanking = {"Ranking", "Nama Murid", "Total"};
        Object[][] dataRanking = new Object[listHasilAkhir.size()][3];
        for (int i = 0; i < listHasilAkhir.size(); i++) {
            HasilAkhir h = listHasilAkhir.get(i);
            dataRanking[i][0] = h.getRanking();
            dataRanking[i][1] = h.getNamaMurid();
            dataRanking[i][2] = String.format("%.3f", h.getTotal());
        }

        tblRanking.setModel(new DefaultTableModel(dataRanking, kolomRanking) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        });
    }

    private void setStyleTable() {
        TabelUtils.setColumnWidths(tblHasil, new int[]{0}, new int[]{250});
        TabelUtils.setHeaderAlignment(tblHasil, new int[]{0}, new int[]{JLabel.LEFT}, JLabel.LEFT);
        tblHasil.putClientProperty(FlatClientProperties.STYLE, ""
                + "showHorizontalLines:true;"
                + "intercellSpacing:0,1");

        TabelUtils.setColumnWidths(tblRanking, new int[]{0}, new int[]{80});
        TabelUtils.setHeaderAlignment(tblRanking, new int[]{0}, new int[]{JLabel.CENTER}, JLabel.LEFT);
        TabelUtils.setColumnAlignment(tblRanking, new int[]{0}, JLabel.CENTER);

        tblRanking.putClientProperty(FlatClientProperties.STYLE, ""
                + "showHorizontalLines:true;"
                + "intercellSpacing:0,1");
    }

    private void exportToPDF(File file) {
         try {
            // 1. Setup Dokumen A4 Portrait
            Document document = new Document(PageSize.A4);
            // Simpan instance writer untuk penggunaan absolute positioning nanti
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(file));
            document.open();

            // 2. Membuat Header (Logo & Subjudul Center)
            
            // --- Logo ---
            String logoPath = "src/com/spk/img/Logo.png";
            try {
                com.itextpdf.text.Image logo = com.itextpdf.text.Image.getInstance(logoPath);
                // UPDATE: Mengubah ukuran dari 100 ke 60 agar tidak terlalu memakan tempat vertikal
                logo.scaleAbsolute(150, 100); 
                logo.setAlignment(Element.ALIGN_CENTER); // Logo Center
                logo.setSpacingAfter(0f); 
                document.add(logo);
            } catch (Exception ex) {
                Paragraph pLogoFallback = new Paragraph("LOGO", new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD));
                pLogoFallback.setAlignment(Element.ALIGN_CENTER);
                document.add(pLogoFallback);
            }

           // --- Subjudul ---
            Font fontSubtitle = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);
            Paragraph pSubtitle = new Paragraph("Hasil Penilaian Murid\nBerhak Mendapatkan Jadwal Kelas Terlebih Dahulu", fontSubtitle);
            pSubtitle.setAlignment(Element.ALIGN_CENTER); // Teks Center
            // UPDATE: Mengurangi spasi sebelum judul
            pSubtitle.setSpacingBefore(0f); 
            document.add(pSubtitle);

            document.add(new Paragraph("\n"));

            // 3. Garis Pemisah Header (Double Line style)
            PdfPTable lineTable = new PdfPTable(1);
            lineTable.setWidthPercentage(100);
            PdfPCell lineCell = new PdfPCell(new Phrase(" "));
            lineCell.setBorder(Rectangle.BOTTOM);
            lineCell.setBorderWidthBottom(2f); // Garis tebal
            lineTable.addCell(lineCell);
            document.add(lineTable);
            
            document.add(new Paragraph("\n"));

            // 4. Tabel Data (NO, ID MURID, NAMA MURID, NILAI)
            PdfPTable tableData = new PdfPTable(4); 
            tableData.setWidthPercentage(100);
            tableData.setWidths(new float[]{0.5f, 1.5f, 3f, 1.5f}); 

            Font fontHeaderTable = new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD);
            Font fontCellTable = new Font(Font.FontFamily.HELVETICA, 10);

            // Header Tabel
            String[] headers = {"NO", "ID MURID", "NAMA MURID", "NILAI"};
            for (String h : headers) {
                PdfPCell cell = new PdfPCell(new Phrase(h, fontHeaderTable));
                setHeaderCellStyle(cell);
                tableData.addCell(cell);
            }

            // Isi Data Tabel
            int no = 1;
            List<Murid> allMurid = servisMurid.getData(); 
            
            for (HasilAkhir h : listHasilAkhir) {
                String kodeMurid = "-";
                for(Murid m : allMurid) {
                    if(m.getNamaMurid().equals(h.getNamaMurid())) {
                        kodeMurid = m.getKodeMurid();
                        break;
                    }
                }

                PdfPCell c1 = new PdfPCell(new Phrase(String.valueOf(no++), fontCellTable));
                setDataCellStyle(c1);
                tableData.addCell(c1);

                PdfPCell c2 = new PdfPCell(new Phrase(kodeMurid, fontCellTable));
                setDataCellStyle(c2);
                tableData.addCell(c2);

                PdfPCell c3 = new PdfPCell(new Phrase(h.getNamaMurid(), fontCellTable));
                setDataCellStyle(c3); 
                c3.setHorizontalAlignment(Element.ALIGN_LEFT); 
                tableData.addCell(c3);

                PdfPCell c4 = new PdfPCell(new Phrase(String.format("%.3f", h.getTotal()), fontCellTable));
                setDataCellStyle(c4);
                tableData.addCell(c4);
            }

            document.add(tableData);
            
            // --- BAGIAN FOOTER (Absolute Positioning) ---
            // Menggunakan writeSelectedRows untuk memastikan footer selalu di paling bawah halaman
            
            // Format tanggal lokal Indonesia
            SimpleDateFormat sdf = new SimpleDateFormat("d MMMM yyyy", new Locale("id", "ID"));
            String tanggal = sdf.format(new Date());

            Font fontFooter = new Font(Font.FontFamily.HELVETICA, 10);
            Paragraph pFooter = new Paragraph("MENGETAHUI\nBandung, " + tanggal + "\n\n\n\n( Hafizhjoys )", fontFooter);
            pFooter.setAlignment(Element.ALIGN_CENTER);

            // Kita buat tabel footer dengan 2 kolom: 
            // Kolom 1 (Kiri/Kosong): 70%
            // Kolom 2 (Kanan/TTD): 30%
            PdfPTable footerTable = new PdfPTable(2);
            footerTable.setTotalWidth(document.right() - document.left()); // Lebar sesuai margin halaman
            footerTable.setLockedWidth(true); // Kunci lebar agar bisa fixed position
            footerTable.setWidths(new float[]{7f, 3f});

            // Row 1: Garis Pemisah (Span 2 kolom)
            PdfPCell cellLineFull = new PdfPCell(new Phrase(" "));
            cellLineFull.setColspan(2);
            cellLineFull.setBorder(Rectangle.BOTTOM);
            cellLineFull.setBorderWidthBottom(1f); // Garis pemisah footer
            cellLineFull.setPaddingBottom(5f); // Jarak antara garis dan teks dibawahnya
            footerTable.addCell(cellLineFull);

            // Row 2 Col 1: Kosong (Kiri)
            PdfPCell cellEmpty = new PdfPCell(new Phrase(" "));
            cellEmpty.setBorder(Rectangle.NO_BORDER);
            footerTable.addCell(cellEmpty);

            // Row 2 Col 2: Tanda Tangan (Kanan)
            PdfPCell cellSign = new PdfPCell(pFooter);
            cellSign.setBorder(Rectangle.NO_BORDER);
            cellSign.setHorizontalAlignment(Element.ALIGN_CENTER);
            cellSign.setPaddingTop(5f);
            footerTable.addCell(cellSign);

            // Tulis footer di posisi absolut
            // X: Margin Kiri
            // Y: Margin Bawah + Tinggi Footer (karena writeSelectedRows menggambar dari Top-Left ke bawah)
            // Kita beri sedikit buffer (+20) dari batas bawah kertas
            float yPos = document.bottom() + footerTable.getTotalHeight() + 20;
            
            footerTable.writeSelectedRows(0, -1, document.left(), yPos, writer.getDirectContent());

            document.close();
            JOptionPane.showMessageDialog(this, "Berhasil ekspor PDF!");

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Gagal ekspor PDF: " + e.getMessage());
        }
    }

    // Helper Style Header Tabel
    private void setHeaderCellStyle(PdfPCell cell) {
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setPadding(5f);
        cell.setBackgroundColor(BaseColor.WHITE); // Putih polos sesuai referensi
        cell.setBorderWidth(1f);
    }

    // Helper Style Isi Tabel
    private void setDataCellStyle(PdfPCell cell) {
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setPadding(5f);
        cell.setBorderWidth(1f);
    }

    private void exportToExcel(File file) {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Hasil AHP");

            // --- Styling Excel ---
            
            // Style Header
            CellStyle headerStyle = workbook.createCellStyle();
            XSSFFont font = (XSSFFont) workbook.createFont();
            font.setBold(true);
            headerStyle.setFont(font);
            headerStyle.setAlignment(HorizontalAlignment.CENTER);
            headerStyle.setVerticalAlignment(org.apache.poi.ss.usermodel.VerticalAlignment.CENTER);
            
            // Style Data Center (No, ID, Nilai)
            CellStyle dataStyleCenter = workbook.createCellStyle();
            dataStyleCenter.setAlignment(HorizontalAlignment.CENTER);
            dataStyleCenter.setVerticalAlignment(org.apache.poi.ss.usermodel.VerticalAlignment.CENTER);
            
            // Style Data Left (Nama)
            CellStyle dataStyleLeft = workbook.createCellStyle();
            dataStyleLeft.setAlignment(HorizontalAlignment.LEFT);
            dataStyleLeft.setVerticalAlignment(org.apache.poi.ss.usermodel.VerticalAlignment.CENTER);

            // --- Header Row ---
            Row headerRow = sheet.createRow(0);
            String[] headers = {"NO", "ID MURID", "NAMA MURID", "NILAI"};
            
            for(int i = 0; i < headers.length; i++){
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
            }

            // --- Isi Data ---
            List<Murid> allMurid = servisMurid.getData(); // Ambil data murid untuk lookup ID
            int no = 1;
            int rowNum = 1;

            for (HasilAkhir h : listHasilAkhir) {
                // Lookup ID Murid berdasarkan Nama
                String kodeMurid = "-";
                for(Murid m : allMurid) {
                    if(m.getNamaMurid().equals(h.getNamaMurid())) {
                        kodeMurid = m.getKodeMurid();
                        break;
                    }
                }

                Row row = sheet.createRow(rowNum++);
                
                // Kolom 1: NO
                Cell cellNo = row.createCell(0);
                cellNo.setCellValue(no++);
                cellNo.setCellStyle(dataStyleCenter);

                // Kolom 2: ID MURID
                Cell cellId = row.createCell(1);
                cellId.setCellValue(kodeMurid);
                cellId.setCellStyle(dataStyleCenter);

                // Kolom 3: NAMA MURID
                Cell cellNama = row.createCell(2);
                cellNama.setCellValue(h.getNamaMurid());
                cellNama.setCellStyle(dataStyleLeft); // Rata Kiri seperti PDF

                // Kolom 4: NILAI
                Cell cellNilai = row.createCell(3);
                cellNilai.setCellValue(h.getTotal()); // Simpan sebagai angka (double)
                cellNilai.setCellStyle(dataStyleCenter);
            }

            // Autosize kolom agar rapi
            for(int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }

            // Simpan file
            try (FileOutputStream fos = new FileOutputStream(file)) {
                workbook.write(fos);
            }

            JOptionPane.showMessageDialog(this, "Berhasil ekspor Excel!");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Gagal export " + e.getMessage());
        }
    }
}