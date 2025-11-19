package checkIn;

/**
 * CheckInView 클래스
 * 역할: MVC 패턴의 화면 보여주기
 * 1. 사용자가 보는 창(Window)과 버튼, 입력창들을 구성
 * 2. 사용자 입력을 컨트롤러에 전달(Getter)하거나, 컨트롤러가 준 데이터를 화면에 출력(Setter)
 * 3. 데이터 처리 로직은 포함 x
 * @author subin
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class CheckInView extends JFrame {
    // 화면 구성 요소 선
    private JTextField tfReservationId = new JTextField(10);
    private JButton btnSearch = new JButton("예약 조회");
    
    private JTextArea taInfo = new JTextArea(6, 30);
    
    private JTextField tfRoomNumber = new JTextField(5);
    private JButton btnCheckIn = new JButton("체크인 확정");

    public CheckInView() {
        // 기본설정
        setTitle("호텔 체크인 시스템");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // 닫으면 프로그램 종료
        setLayout(new FlowLayout());

        // 예약번호 패널
        JPanel pnlTop = new JPanel();
        pnlTop.add(new JLabel("예약번호: "));
        pnlTop.add(tfReservationId);
        pnlTop.add(btnSearch);
        add(pnlTop);

        // 예약번호 기반 고객 정보 출력
        taInfo.setEditable(false); // 사용자 수정 금지
        taInfo.setText("예약 번호를 입력하고 조회하세요.");
        add(new JScrollPane(taInfo)); 

        // 체크인 객실 패널
        JPanel pnlBottom = new JPanel();
        pnlBottom.add(new JLabel("배정 객실: "));
        pnlBottom.add(tfRoomNumber);
        pnlBottom.add(btnCheckIn);
        add(pnlBottom);

        btnCheckIn.setEnabled(false); // 조회 전엔 버튼 비활성화
        
        setVisible(true); // 창 띄우기
    }

    // Getter : 입력값 가져오기
    public String getReservationId() { return tfReservationId.getText(); }
    public String getRoomNumber() { return tfRoomNumber.getText(); }

    // Setter : 화면 업데이트
    public void setReservationInfo(Reservation r) {
        taInfo.setText(" [예약 정보 확인]\n");
        taInfo.append(" 고객명: " + r.getCustomerName() + "\n");
        taInfo.append(" 투숙기간: " + r.getPeriod() + "\n");
        taInfo.append(" 객실타입: " + r.getRoomType() + "\n");
        
        btnCheckIn.setEnabled(true); // 조회 성공 시 버튼 활성화
    }

    public void showMessage(String msg) {
        JOptionPane.showMessageDialog(this, msg);
    }

    // 이벤트 리스너 등록
    public void addSearchListener(ActionListener listener) { btnSearch.addActionListener(listener); }
    public void addCheckInListener(ActionListener listener) { btnCheckIn.addActionListener(listener); }
}