package customers;

/**
 *
 * @author subin
 */

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;

public class CustomersView extends JPanel {
    // 테이블 모델 (데이터 담는 그릇)
    private String[] colNames = {"이름", "전화번호", "등급", "방문 횟수"};
    private DefaultTableModel model = new DefaultTableModel(colNames, 0) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };
    private JTable table = new JTable(model);

    // 버튼
    private JButton btnLoad = new JButton("새로고침");
    private JButton btnSync = new JButton("예약 기반 신규고객 등록"); // 첫 방문 손님 등록용
    private JButton btnEditGrade = new JButton("등급 변경");
    private JButton btnBack = new JButton("뒤로가기");

    public CustomersView() {
        setLayout(new BorderLayout());

        add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel btnPanel = new JPanel();
        btnPanel.add(btnLoad);
        btnPanel.add(btnSync);
        btnPanel.add(btnEditGrade);
        btnPanel.add(btnBack);

        add(btnPanel, BorderLayout.SOUTH);
    }


    public void clearTable() {
        model.setRowCount(0);
    }

    public void addRow(String name, String phone, String grade, String count) {
        model.addRow(new Object[]{name, phone, grade, count});
    }

    public int getSelectedRow() {
        return table.getSelectedRow();
    }

    public String getSelectedName() {
        int row = table.getSelectedRow();
        return (row != -1) ? (String) model.getValueAt(row, 0) : null;
    }

    public String getSelectedPhone() {
        int row = table.getSelectedRow();
        return (row != -1) ? (String) model.getValueAt(row, 1) : null;
    }

    public void addLoadListener(ActionListener l) { btnLoad.addActionListener(l); }
    public void addSyncListener(ActionListener l) { btnSync.addActionListener(l); }
    public void addEditGradeListener(ActionListener l) { btnEditGrade.addActionListener(l); }
    public void addBackListener(ActionListener l) { btnBack.addActionListener(l); }

    public void showMessage(String msg) {
        JOptionPane.showMessageDialog(this, msg);
    }
}