package checkOut;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

// 중요: JFrame이 아니라 JPanel이어야 메인 화면 안에 들어갑니다.
public class CheckOutView extends JPanel {

    private JTextField tfRoomNumber = new JTextField(10);
    private JTextArea taInfo = new JTextArea(10, 30);

    private JButton btnSearch = new JButton("객실 조회");
    private JButton btnCalculate = new JButton("요금 계산");
    private JButton btnCheckOut = new JButton("체크아웃");
    private JButton btnBack = new JButton("돌아가기");

    public CheckOutView() {
        // JPanel은 setTitle이나 setSize가 필요 없습니다. (메인 창이 알아서 함)
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

        // 하단 버튼 패널
        JPanel bottomPanel = new JPanel();
        bottomPanel.add(btnCalculate);
        bottomPanel.add(btnCheckOut);
        bottomPanel.add(btnBack);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    // -------------------------
    // Getter / Setter
    // -------------------------
    public String getRoomNumber() {
        return tfRoomNumber.getText().trim();
    }

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
    
    // Main.java에서 버튼을 가져다 쓸 수 있도록 추가 (옵션)
    public JButton getBackBtn() {
        return btnBack;
    }

    // -------------------------
    // Listener 등록 메서드 (컨트롤러 연결용)
    // -------------------------
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