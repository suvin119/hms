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
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionListener;

public class CheckInView extends JPanel {

    private JTextField tfReservationId = new JTextField(10);
    private JButton btnSearch = new JButton("예약 조회");

    private JTextArea taInfo = new JTextArea(6, 30);

    private JTextField tfCheckOutDate = new JTextField(10);
    private JTextField tfRoomNumber = new JTextField(5);
    private JButton btnCheckIn = new JButton("체크인 확정");
    private JButton btnBack = new JButton("뒤로가기");


    public CheckInView() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel pnlTop = new JPanel(new FlowLayout());
        pnlTop.add(new JLabel("예약번호: "));
        pnlTop.add(tfReservationId);
        pnlTop.add(btnSearch);
        add(pnlTop, BorderLayout.NORTH);

        taInfo.setEditable(false);
        taInfo.setText(" 예약 번호를 입력하고 조회하세요.");
        
        taInfo.setBackground(Color.WHITE); 

        JScrollPane scrollPane = new JScrollPane(taInfo);

        scrollPane.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));
        
        add(scrollPane, BorderLayout.CENTER);

        JPanel pnlBottom = new JPanel();
        pnlBottom.setLayout(new GridLayout(3, 1));

        JPanel pnlRow1 = new JPanel(new FlowLayout());
        pnlRow1.add(new JLabel("수정할 퇴실날짜: "));
        pnlRow1.add(tfCheckOutDate);
        pnlBottom.add(pnlRow1);

        JPanel pnlRow2 = new JPanel(new FlowLayout());
        pnlRow2.add(new JLabel("배정 객실: "));
        pnlRow2.add(tfRoomNumber);
        pnlBottom.add(pnlRow2);

        JPanel pnlRow3 = new JPanel(new FlowLayout());
        pnlRow3.add(btnBack); 
        pnlRow3.add(btnCheckIn);
        pnlBottom.add(pnlRow3);

        add(pnlBottom, BorderLayout.SOUTH);

        btnCheckIn.setEnabled(false);
    }

    // Getter
    public String getReservationId() { return tfReservationId.getText(); }
    public String getRoomNumber() { return tfRoomNumber.getText(); }
    public String getCheckOutDate() { return tfCheckOutDate.getText(); }

    // Setter (화면 갱신)
    public void setReservationInfo(Reservation r) {
        taInfo.setText("\n [예약 정보 확인]\n");
        taInfo.append(" ----------------------------------\n");
        taInfo.append("  고객명 : " + r.getCustomerName() + "\n");
        taInfo.append("  연락처 : " + r.getPhoneNumber() + "\n");
        taInfo.append("  체크인 날짜 : " + r.getCheckInDate() + "\n");
        taInfo.append("  인원 : " + r.getNumGuest() + "\n");
        
        tfCheckOutDate.setText(r.getCheckOutDate()); // 날짜 자동 채움
        btnCheckIn.setEnabled(true);
    }

    // 리스너 등록
    public void addSearchListener(ActionListener l) { btnSearch.addActionListener(l); }
    public void addCheckInListener(ActionListener l) { btnCheckIn.addActionListener(l); }
    public void addBackListener(ActionListener l) { btnBack.addActionListener(l); }

    public void showMessage(String msg) { JOptionPane.showMessageDialog(this, msg); }
}