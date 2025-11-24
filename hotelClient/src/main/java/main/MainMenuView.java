package main;

/**
 *
 * @author subin
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class MainMenuView extends JPanel {
    
    private JButton btnReservation = new JButton("예약 등록");
    private JButton btnCheckIn = new JButton("체크인");
    private JButton btnCheckOut = new JButton("체크아웃");
    private JButton btnRoomManage = new JButton("관리");

    public MainMenuView() {
        
        setLayout(new BorderLayout());

        JPanel titlePanel = new JPanel();
        JLabel titleLabel = new JLabel("HMS 업무");
        titleLabel.setFont(new Font("맑은 고딕", Font.BOLD, 24));
        titlePanel.add(titleLabel);
        titlePanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        add(titlePanel, BorderLayout.NORTH);


        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(2, 2, 10, 10)); // (행, 열, 좌우간격, 상하간격)
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 50, 50)); // 전체 여백

        setFontForBtn(btnReservation);
        setFontForBtn(btnCheckIn);
        setFontForBtn(btnCheckOut);
        setFontForBtn(btnRoomManage);

        buttonPanel.add(btnReservation);
        buttonPanel.add(btnCheckIn);
        buttonPanel.add(btnCheckOut);
        buttonPanel.add(btnRoomManage);

        add(buttonPanel, BorderLayout.CENTER);
    }

    private void setFontForBtn(JButton btn) {
        btn.setFont(new Font("맑은 고딕", Font.BOLD, 18));
    }

    // 리스너 등록 메소드 (Controller 사용)
    public void setReservationListener(ActionListener l) { btnReservation.addActionListener(l); }
    public void setCheckInListener(ActionListener l) { btnCheckIn.addActionListener(l); }
    public void setCheckOutListener(ActionListener l) { btnCheckOut.addActionListener(l); }
    public void setAdminButtonListener(ActionListener l) { btnRoomManage.addActionListener(l); }
    
    public void showMessage(String msg) {
        JOptionPane.showMessageDialog(this, msg);
    }
}