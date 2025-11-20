package reservation;

/**
 *
 * @author subin
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.Vector; // JComboBox 사용을 위해 Vector 임포트

public class ReservationView extends JPanel {
    private JTextField tfName = new JTextField(15);
    private JTextField tfPhone = new JTextField(15);
    private JTextField tfInDate = new JTextField(10);
    private JTextField tfOutDate = new JTextField(10);
    private JTextField tfNumGuest = new JTextField(5);
    
    // 객실 타입 선택 (JComboBox)
    private JComboBox<String> cbRoomType;
    
    // 버튼
    private JButton btnRegister = new JButton("예약 확정");
    private JButton btnBack = new JButton("뒤로가기"); // 메인으로 돌아가는 버튼

    public ReservationView() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // 1. 중앙 입력 폼 패널
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(6, 2, 10, 5));

        // Room Type 콤보박스 데이터 초기화
        Vector<String> roomTypes = new Vector<>();
        roomTypes.add("스탠다드");
        roomTypes.add("디럭스");
        roomTypes.add("스위트");
        cbRoomType = new JComboBox<>(roomTypes);

        inputPanel.add(new JLabel("고객명:"));
        inputPanel.add(tfName);
        inputPanel.add(new JLabel("전화번호:"));
        inputPanel.add(tfPhone);
        inputPanel.add(new JLabel("체크인 날짜 (YYYY-MM-DD):"));
        inputPanel.add(tfInDate);
        inputPanel.add(new JLabel("체크아웃 날짜 (YYYY-MM-DD):"));
        inputPanel.add(tfOutDate);
        inputPanel.add(new JLabel("객실 타입:"));
        inputPanel.add(cbRoomType);
        inputPanel.add(new JLabel("투숙 인원:"));
        inputPanel.add(tfNumGuest);
        
        JPanel wrapperPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        wrapperPanel.add(inputPanel);
        add(wrapperPanel, BorderLayout.NORTH);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        
        btnBack.setPreferredSize(new Dimension(90, 30));
        btnRegister.setPreferredSize(new Dimension(100, 30));

        btnPanel.add(btnBack);
        btnPanel.add(btnRegister);
        
        add(btnPanel, BorderLayout.SOUTH);
    }
    
    // Getter
    public String getName() { return tfName.getText(); }
    public String getPhone() { return tfPhone.getText(); }
    public String getInDate() { return tfInDate.getText(); }
    public String getOutDate() { return tfOutDate.getText(); }
    public String getRoomType() { return (String) cbRoomType.getSelectedItem(); }
    public String getGuestCount() { return tfNumGuest.getText(); }

    // Listener 
    public void addRegisterListener(ActionListener l) { btnRegister.addActionListener(l); }
    public void addBackListener(ActionListener l) { btnBack.addActionListener(l); }
    
    public void showMessage(String msg) {
        JOptionPane.showMessageDialog(this, msg);
    }
}