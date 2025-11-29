package menuAdmin;

/**
 *
 * @author subin
 */

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;

public class MenuAdminView extends JPanel {
    private JTextField idField, nameField, priceField;
    private JButton addButton, updateButton, deleteButton, refreshButton, backButton;
    private DefaultTableModel tableModel;
    private JTable menuTable;

    public MenuAdminView() {
        setLayout(new BorderLayout());

        String[] columnNames = {"메뉴 ID", "메뉴 이름", "가격"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        menuTable = new JTable(tableModel);
        add(new JScrollPane(menuTable), BorderLayout.CENTER);

        JPanel inputPanel = new JPanel(new GridLayout(2, 1));
        JPanel fieldsPanel = new JPanel();

        fieldsPanel.add(new JLabel("ID(자동생성):"));
        idField = new JTextField(5);
        idField.setEditable(false);
        fieldsPanel.add(idField);

        fieldsPanel.add(new JLabel("이름:"));
        nameField = new JTextField(10);
        fieldsPanel.add(nameField);

        fieldsPanel.add(new JLabel("가격:"));
        priceField = new JTextField(7);
        fieldsPanel.add(priceField);

        inputPanel.add(fieldsPanel);

        JPanel buttonsPanel = new JPanel();
        addButton = new JButton("메뉴 등록");
        updateButton = new JButton("정보 수정");
        deleteButton = new JButton("삭제");
        refreshButton = new JButton("새로고침");
        backButton = new JButton("뒤로가기");

        buttonsPanel.add(addButton);
        buttonsPanel.add(updateButton);
        buttonsPanel.add(deleteButton);
        buttonsPanel.add(refreshButton);
        buttonsPanel.add(backButton);
        inputPanel.add(buttonsPanel);

        add(inputPanel, BorderLayout.SOUTH);
    }

    // Getters
    public String getMenuName() { return nameField.getText().trim(); }
    public String getPrice() { return priceField.getText().trim(); }
    
    public void clearTable() { tableModel.setRowCount(0); }
    public void addRow(Object[] rowData) { tableModel.addRow(rowData); }

    // 마지막 행 id 가져오기
    public String getLastIdFromTable() {
        int rowCount = tableModel.getRowCount();
        if (rowCount > 0) {
            return (String) tableModel.getValueAt(rowCount - 1, 0);
        }
        return null;
    }

    public String getSelectedMenuId() {
        int row = menuTable.getSelectedRow();
        if (row != -1) return (String) tableModel.getValueAt(row, 0);
        return null;
    }

    public void addAddListener(ActionListener listener) { addButton.addActionListener(listener); }
    public void addUpdateListener(ActionListener listener) { updateButton.addActionListener(listener); }
    public void addDeleteListener(ActionListener listener) { deleteButton.addActionListener(listener); }
    public void addRefreshListener(ActionListener listener) { refreshButton.addActionListener(listener); }
    public void addBackListener(ActionListener listener) { backButton.addActionListener(listener); }
    
    public void showMessage(String msg) { JOptionPane.showMessageDialog(this, msg); }
    
    public void clearInputFields() {
        idField.setText("");
        nameField.setText("");
        priceField.setText("");
    }
}