package com.spk.tablemodel;

import com.spk.model.SubKriteria;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

public class TabModSubKriteria extends AbstractTableModel {

    private final List<SubKriteria> list = new ArrayList<>();

    public SubKriteria getData(int index) {
        return list.get(index);
    }

    public void clear() {
        list.clear();
        fireTableDataChanged();
    }

    public void setData(List<SubKriteria> list) {
        clear();
        this.list.addAll(list);
        fireTableDataChanged();
    }

    private final String[] columnNames = {"No", "Kode Sub Kriteria", "Nama Sub Kriteria", "Nama Kriteria"};

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
        SubKriteria model = list.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return (rowIndex + 1);
            case 1:
                return model.getKodeSubKriteria();
            case 2:
                return model.getNamaSubKriteria();
            case 3:
                return model.getNamaKriteriaInduk();
            default:
                return null;
        }
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];

        }
    }