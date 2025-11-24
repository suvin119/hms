package checkOut;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class CheckOutView extends JPanel {

    private JTextField tfRoomNumber = new JTextField(10);
    private JTextArea taInfo = new JTextArea(10, 30);

    private JButton btnSearch = new JButton("객실 조회");
    private JButton btnCalculate = new JButton("요금 계산");
    private JButton btnCheckOut = new JButton("체크아웃");
    private JButton btnBack = new JButton("돌아가기");

    public CheckOutView() {

        setLayout(new BorderLayout());

        // 상단 입력 패널
        JPanel topPanel = new JPanel();
        topPanel.add(new JLabel("객실 번호: "));
        topPanel.add(tfRoomNumber);
        topPanel.add(btnSearch);
        add(topPanel, BorderLayout.NORTH);

        // 중앙 정보창
        taInfo.setEditable(false);
        add(new JScrollPane(taInfo), BorderLayout.CENTER);

        // 하단 버튼
        JPanel bottomPanel = new JPanel();
        bottomPanel.add(btnCalculate);
        bottomPanel.add(btnCheckOut);
        bottomPanel.add(btnBack);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    // Getter
    public String getRoomNumber() {
        return tfRoomNumber.getText().trim();
    }

    // Setter
    public void setInfoText(String text) {
        taInfo.setText(text);
    }

    public void clearFields() {
        tfRoomNumber.setText("");
        taInfo.setText("");
    }

    public void showMessage(String msg) {
        JOptionPane.showMessageDialog(this, msg);
    }

    // Listener 등록
    public void setSearchListener(ActionListener l) {
        btnSearch.addActionListener(l);
    }

    public void setCalculateListener(ActionListener l) {
        btnCalculate.addActionListener(l);
    }

    public void setCheckOutListener(ActionListener l) {
        btnCheckOut.addActionListener(l);
    }

    public void setBackListener(ActionListener l) {
        btnBack.addActionListener(l);
    }
}
