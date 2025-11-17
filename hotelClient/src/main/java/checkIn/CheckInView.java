package checkIn;

/**
 *
 * @author subin
 */
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class CheckInView extends JFrame {
    // 1. 검색 영역
    private JTextField searchIdField = new JTextField(10);
    private JButton searchButton = new JButton("예약 조회");

    // 2. 고객 정보 표시 영역
    private JLabel nameLabel = new JLabel("-");
    private JLabel phoneLabel = new JLabel("-");

    // 3. 방 배정 영역
    private JTextField roomNumField = new JTextField(5);
    private JTextField dateField = new JTextField("2025-00-00", 10);
    private JButton confirmButton = new JButton("체크인 확정");

    public CheckInView() {
        setTitle("호텔 체크인 클라이언트");
        setSize(400, 350);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(4, 1)); // 4행 레이아웃

        // Panel 1: 검색
        JPanel p1 = new JPanel();
        p1.add(new JLabel("예약번호 입력: "));
        p1.add(searchIdField);
        p1.add(searchButton);
        add(p1);

        // Panel 2: 정보 확인
        JPanel p2 = new JPanel();
        p2.setBorder(BorderFactory.createTitledBorder("예약자 정보"));
        p2.add(new JLabel("이름: ")); p2.add(nameLabel);
        p2.add(Box.createHorizontalStrut(20)); // 간격
        p2.add(new JLabel("연락처: ")); p2.add(phoneLabel);
        add(p2);

        // Panel 3: 방 배정 입력
        JPanel p3 = new JPanel();
        p3.setBorder(BorderFactory.createTitledBorder("객실 배정"));
        p3.add(new JLabel("방 번호:")); p3.add(roomNumField);
        p3.add(new JLabel("체크아웃:")); p3.add(dateField);
        add(p3);

        // Panel 4: 버튼
        JPanel p4 = new JPanel();
        p4.add(confirmButton);
        add(p4);
        
        confirmButton.setEnabled(false); // 조회 전에는 비활성화
        setVisible(true);
    }

    // Getters & Setters
    public String getSearchId() { return searchIdField.getText(); }
    public String getRoomNum() { return roomNumField.getText(); }
    public String getCheckoutDate() { return dateField.getText(); }
    
    public void setCustomerInfo(String name, String phone) {
        nameLabel.setText(name);
        phoneLabel.setText(phone);
        confirmButton.setEnabled(true); // 정보 뜨면 버튼 활성화
    }

    public void showMessage(String msg) {
        JOptionPane.showMessageDialog(this, msg);
    }

    public void addSearchListener(ActionListener l) { searchButton.addActionListener(l); }
    public void addCheckInListener(ActionListener l) { confirmButton.addActionListener(l); }
}
