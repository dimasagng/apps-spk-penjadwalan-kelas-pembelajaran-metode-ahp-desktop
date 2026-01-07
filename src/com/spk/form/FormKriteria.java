package com.spk.form;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import com.spk.dao.KriteriaDAO;
import com.spk.form.input.FormInputKriteria;
import com.spk.main.Form;
import com.spk.model.Kriteria;
import com.spk.service.ServiceKriteria;
import com.spk.tablemodel.TabModKriteria;
import static com.spk.util.AlertUtils.getOptionAlert;
import com.spk.util.MessageModal;
import com.spk.util.TabelUtils;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.JTextField;
import net.miginfocom.swing.MigLayout;
import raven.modal.ModalDialog;
import raven.modal.Toast;
import raven.modal.component.SimpleModalBorder;
import raven.modal.option.Option;

public class FormKriteria extends Form {

    private final ServiceKriteria servis = new KriteriaDAO();
    private final TabModKriteria tblModel = new TabModKriteria();
    
    private final JTable tblData = new JTable();
    private JTextField txtSearch;

    private int idKriteria;

    public FormKriteria() {
            init();
        }

        private void init() {
            setLayout(new MigLayout("fillx, wrap", "[fill]", "[][][][fill, grow]"));
            add(setInfo());
            add(createSeparator());
            add(setButton());
            add(setTableData());
        }

        private JPanel setInfo() {
            JPanel panel = new JPanel(new MigLayout("fillx"));
            panel.putClientProperty(FlatClientProperties.STYLE, "arc:10");

            JLabel lbTitle = new JLabel("Data Kriteria");
            lbTitle.putClientProperty(FlatClientProperties.STYLE, "font:bold 18");

            panel.add(lbTitle);
            return panel;
        }
        
        private JSeparator createSeparator() {
        JSeparator separator = new JSeparator();
        separator.putClientProperty(FlatClientProperties.STYLE, "foreground:rgb(206,206,206)");
        return separator;
    }

        private JPanel setButton() {
            JPanel panel = new JPanel(new MigLayout("wrap", "[][][]push[]"));
            panel.putClientProperty(FlatClientProperties.STYLE, "arc:10");

        JButton btnAdd = new JButton("Add");
        btnAdd.putClientProperty(FlatClientProperties.STYLE, "background:@accentColor;foreground:rgb(255,255,255)");
        btnAdd.setIcon(new FlatSVGIcon("com/spk/icon/add_white.svg", 0.8f));
        btnAdd.setIconTextGap(5);
        btnAdd.addActionListener((e) -> {
            insertData(); 
        });

        JButton btnUpdate = new JButton("Update");
        btnUpdate.setIcon(new FlatSVGIcon("com/spk/icon/edit.svg", 0.4f));
        btnUpdate.setIconTextGap(5);
        btnUpdate.addActionListener((e) -> {
            updateData();
        });

        JButton btnDelete = new JButton("Delete");
        btnDelete.setIcon(new FlatSVGIcon("com/spk/icon/delete.svg", 0.4f));
        btnDelete.setIconTextGap(5);
        btnDelete.addActionListener((e) -> {
            deleteData();
        });

        txtSearch = new JTextField();
        txtSearch.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Search...");
        txtSearch.putClientProperty(FlatClientProperties.TEXT_FIELD_SHOW_CLEAR_BUTTON, true);
        txtSearch.putClientProperty(FlatClientProperties.TEXT_FIELD_LEADING_ICON, new FlatSVGIcon("raven/modal/demo/icons/search.svg", 0.4f));
        txtSearch.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                searchData();
            }
        });

        panel.add(btnAdd, "hmin 30, wmin 90, sg btn");
        panel.add(btnUpdate, "hmin 30, wmin 50");
        panel.add(btnDelete, "hmin 30, wmin 50");
        panel.add(txtSearch, "hmin 30, width 300, gapx 8");

        return panel;
    }

    private JPanel setTableData() {
        JPanel panel = new JPanel(new MigLayout("fill, insets 5 0 5 0", "fill", "fill"));
        panel.putClientProperty(FlatClientProperties.STYLE, "arc:10;background:rgb(255,255,255)");

        loadData();
        setTableProperties();

        JScrollPane scroll = new JScrollPane(tblData);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        panel.add(scroll);

        return panel;
    }

    private void setTableProperties() {
        TabelUtils.setColumnWidths(tblData, new int[]{0, 1}, new int[]{50, 150});
        TabelUtils.setHeaderAlignment(tblData, new int[]{0}, new int[]{JLabel.CENTER}, JLabel.LEFT);
        TabelUtils.setColumnAlignment(tblData, new int[]{0}, JLabel.CENTER);

        tblData.putClientProperty(FlatClientProperties.STYLE, ""
                + "showHorizontalLines:true;"
                + "intercellSpacing:0,1;");
    }

    private void loadData() {
        List<Kriteria> list = servis.getData();
        tblModel.setData(list);
        tblData.setModel(tblModel);
    }

    private void searchData() {
        String keyword = txtSearch.getText();
        List<Kriteria> list = servis.searchData(keyword);
        tblModel.setData(list);
        tblData.setModel(tblModel);
    }

    public void refreshTable() {
        loadData();
    }

    private void insertData() {
        Option option = ModalDialog.createOption();
        ModalDialog.showModal(this,
                new SimpleModalBorder(new FormInputKriteria(null, this),
                        "Add Kriteria"), option, "Form Input");
    }
    
    private void updateData() {
    int row = tblData.getSelectedRow();
    if (row != -1) {
        Kriteria model = tblModel.getData(row);
        Option option = ModalDialog.createOption();
        ModalDialog.showModal(this,
                new SimpleModalBorder(new FormInputKriteria(model, this),
                        "Update Kriteria"), option, "Form Update");
    } else {
        Toast.show(this, Toast.Type.INFO, "Please select the data you want to update", getOptionAlert());
    }
}   
    private void deleteData() {
        int row = tblData.getSelectedRow();
        if (row != -1) {
            Kriteria model = tblModel.getData(row);
            idKriteria = model.getIdKriteria();

            String message = "Are you sure you want to delete this data ?";
            String title = "Confirmation";

            ModalDialog.showModal(this,
                    new MessageModal(
                            MessageModal.Type.INFO,
                            message,
                            title,
                            SimpleModalBorder.YES_NO_OPTION,
                            (controller, action) -> {
                                if (action == SimpleModalBorder.YES_OPTION) {
                                    Kriteria modelKriteria = new Kriteria();
                                    modelKriteria.setIdKriteria(idKriteria);

                                    servis.deleteData(modelKriteria);
                                    Toast.show(this, Toast.Type.SUCCESS, "Data has been successfully deleted", getOptionAlert());
                                    loadData();
                                }
            }));
        } else {
            Toast.show(this, Toast.Type.INFO, "Please select the data you want to delete", getOptionAlert());
        }
    }
}