/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package checkIn;

/**
 *
 * @author subin
 */
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.List;

public class ConsoleView extends JFrame {
    private JTextArea roomStatusArea = new JTextArea(10, 30);
    private JTextField roomNumField = new JTextField(5);
    private JTextField nameField = new JTextField(10);
    private JButton checkInButton = new JButton("체크인");

    public ConsoleView() {
        // 윈도우 기본 설정
        setTitle("호텔 체크인 시스템");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // 1. 상단: 방 상태 보여주는 곳
        roomStatusArea.setEditable(false);
        add(new JScrollPane(roomStatusArea), BorderLayout.CENTER);

        // 2. 하단: 입력 패널
        JPanel inputPanel = new JPanel();
        inputPanel.add(new JLabel("방 번호:"));
        inputPanel.add(roomNumField);
        inputPanel.add(new JLabel("고객명:"));
        inputPanel.add(nameField);
        inputPanel.add(checkInButton);
        
        add(inputPanel, BorderLayout.SOUTH);
        
        setVisible(true);
    }

    // 방 목록 갱신 메소드
    public void updateRoomList(List<Room> rooms) {
        StringBuilder sb = new StringBuilder();
        sb.append("--- 현재 방 현황 ---\n\n");
        for (Room room : rooms) {
            String status = room.isOccupied() ? "[사용중 - " + room.getGuestName() + "]" : "[빈 방]";
            sb.append(room.getRoomNumber()).append("호 : ").append(status).append("\n");
        }
        roomStatusArea.setText(sb.toString());
    }

    // 입력값 가져오기
    public String getRoomNumInput() { return roomNumField.getText(); }
    public String getNameInput() { return nameField.getText(); }

    // 입력칸 초기화
    public void clearInputs() {
        roomNumField.setText("");
        nameField.setText("");
    }

    // 팝업 메시지
    public void showMessage(String msg) {
        JOptionPane.showMessageDialog(this, msg);
    }

    // 컨트롤러와 연결할 리스너 등록
    public void setCheckInButtonListener(ActionListener listener) {
        checkInButton.addActionListener(listener);
    }
}