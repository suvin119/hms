/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package checkIn;

/**
 *
 * @author subin
 * a test
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class CheckInView extends JFrame {
    // 컴포넌트 선언
    private JTextField tfReservationId = new JTextField(10);
    private JButton btnSearch = new JButton("예약 조회");
    
    private JTextArea taInfo = new JTextArea(6, 30);
    
    private JTextField tfRoomNumber = new JTextField(5);
    private JButton btnCheckIn = new JButton("체크인 확정");

    public CheckInView() {
        setTitle("호텔 체크인 시스템");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // 닫으면 프로그램 종료
        setLayout(new FlowLayout());

        // 1. 검색 패널
        JPanel pnlTop = new JPanel();
        pnlTop.add(new JLabel("예약번호: "));
        pnlTop.add(tfReservationId);
        pnlTop.add(btnSearch);
        add(pnlTop);

        // 2. 정보 출력 영역
        taInfo.setEditable(false);
        taInfo.setText("예약 번호를 입력하고 조회하세요.");
        add(new JScrollPane(taInfo));

        // 3. 체크인 패널
        JPanel pnlBottom = new JPanel();
        pnlBottom.add(new JLabel("배정 객실: "));
        pnlBottom.add(tfRoomNumber);
        pnlBottom.add(btnCheckIn);
        add(pnlBottom);

        btnCheckIn.setEnabled(false); // 조회 전엔 비활성화
        
        setVisible(true); // 창 띄우기
    }

    // Getter: 입력값 가져오기
    public String getReservationId() { return tfReservationId.getText(); }
    public String getRoomNumber() { return tfRoomNumber.getText(); }

    // Setter: 화면 업데이트
    public void setReservationInfo(Reservation r) {
        taInfo.setText(" [예약 정보 확인]\n");
        taInfo.append(" 고객명: " + r.getCustomerName() + "\n");
        taInfo.append(" 투숙기간: " + r.getPeriod() + "\n");
        taInfo.append(" 객실타입: " + r.getRoomType() + "\n");
        
        btnCheckIn.setEnabled(true); // 버튼 활성화
    }

    public void showMessage(String msg) {
        JOptionPane.showMessageDialog(this, msg);
    }

    // 리스너 등록 메소드
    public void addSearchListener(ActionListener listener) { btnSearch.addActionListener(listener); }
    public void addCheckInListener(ActionListener listener) { btnCheckIn.addActionListener(listener); }
}