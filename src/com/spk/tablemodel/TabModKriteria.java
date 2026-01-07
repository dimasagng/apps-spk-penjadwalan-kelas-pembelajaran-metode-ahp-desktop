package com.spk.tablemodel;

import com.spk.model.Kriteria;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

public class TabModKriteria extends AbstractTableModel {

    private final List<Kriteria> list = new ArrayList<>();

    public Kriteria getData(int index) {
        return list.get(index);
    }

    public void clear() {
        list.clear();
        fireTableDataChanged();
    }

    public void setData(List<Kriteria> list) {
        clear();
        this.list.addAll(list);
        fireTableDataChanged();
    }

    private final String[] columnNames = {"No", "Kode Kriteria", "Nama Kriteria"};

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
        Kriteria model = list.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return (rowIndex + 1);
            case 1:
                return model.getKodeKriteria();
            case 2:
                return model.getNamaKriteria();
            default:
                return null;
        }
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];

        }
    }