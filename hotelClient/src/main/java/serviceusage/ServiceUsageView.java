package serviceusage;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class ServiceUsageView extends JPanel {

    private JTextField txtRoom;
    private JTextField txtMenuId;
    private JTextField txtQty;

    private JButton btnAdd;
    private JButton btnSearch;
    private JButton btnBack;   // ← 추가


    private DefaultTableModel tableModel;

    public ServiceUsageView() {

        setLayout(new BorderLayout());

        // --- 입력 영역 ---
        JPanel inputPanel = new JPanel(new GridLayout(5, 2, 5, 5));

        inputPanel.add(new JLabel("객실 번호:"));
        txtRoom = new JTextField();
        inputPanel.add(txtRoom);

        inputPanel.add(new JLabel("메뉴 ID:"));
        txtMenuId = new JTextField();
        inputPanel.add(txtMenuId);

        inputPanel.add(new JLabel("수량:"));
        txtQty = new JTextField();
        inputPanel.add(txtQty);

        btnAdd = new JButton("부대 서비스 추가");
        btnSearch = new JButton("사용 내역 조회");

        inputPanel.add(btnAdd);
        inputPanel.add(btnSearch);
        
        btnBack = new JButton("뒤로가기");
        inputPanel.add(btnBack);

        add(inputPanel, BorderLayout.NORTH);

        // --- 테이블 영역 ---
        tableModel = new DefaultTableModel(new String[]{"메뉴 이름", "금액"}, 0);
        JTable table = new JTable(tableModel);

        add(new JScrollPane(table), BorderLayout.CENTER);
    }

    // 🔹 Controller가 사용하는 getter들
    public String getRoomNumber() { return txtRoom.getText(); }
    public String getMenuId() { return txtMenuId.getText(); }
    public String getQuantity() { return txtQty.getText(); }

    // 🔹 버튼 listener
    public void addAddUsageListener(java.awt.event.ActionListener l) {
        btnAdd.addActionListener(l);
    }

    public void addSearchUsageListener(java.awt.event.ActionListener l) {
        btnSearch.addActionListener(l);
    }
    
    public void addBackListener(java.awt.event.ActionListener l) {
        btnBack.addActionListener(l);
    }
    
    // 🔹 테이블 제어 메소드
    public void clearUsageTable() {
        tableModel.setRowCount(0);
    }

    public void addUsageRow(String menuName, String amount) {
        tableModel.addRow(new String[]{menuName, amount});
    }

    // 🔹 메시지 출력
    public void showMessage(String msg) {
        JOptionPane.showMessageDialog(null, msg);
    }
}