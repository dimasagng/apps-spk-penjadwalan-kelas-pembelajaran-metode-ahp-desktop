package com.spk.tablemodel;

import com.spk.model.Murid;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

public class TabModMurid extends AbstractTableModel {

    private final List<Murid> list = new ArrayList<>();

    public Murid getData(int index) {
        return list.get(index);
    }

    public void clear() {
        list.clear();
        fireTableDataChanged();
    }

    public void setData(List<Murid> list) {
        clear();
        this.list.addAll(list);
        fireTableDataChanged();
    }

    private final String[] columnNames = {"ID Murid", "Nama Murid", "Jumlah Murid", "Program Kelas", "Pengajar", "Waktu", "Media"};

    @Override
    public int getRowCount() {
        return list.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Murid model = list.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return model.getKodeMurid();
            case 1:
                return model.getNamaMurid();
            case 2:
                return model.getJumlahMurid();
            case 3:
                return model.getJenisProgram();
            case 4:
                return model.getPengajar();
            case 5:
                return model.getPreferensiWaktu();
            case 6:
                return model.getMediaPembelajaran();
            default:
                return null;
        }
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];

        }
    }