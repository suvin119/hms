package main;

/**
 *
 * @author subin
 */
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class AdminMenuView extends JPanel {

    private JButton btnRoom = new JButton("객실 관리");
    private JButton btnCustomer = new JButton("고객 관리");
    private JButton btnUser = new JButton("직원 관리");
    private JButton btnReport = new JButton("보고서");
    private JButton btnService = new JButton("부대서비스");
    private JButton btnMenu = new JButton("식음료 메뉴");
    private JButton btnBack = new JButton("메인으로 돌아가기");

    public AdminMenuView() {
        setLayout(new BorderLayout());

        JPanel titlePanel = new JPanel();
        JLabel titleLabel = new JLabel("[관리자 전용] 시스템 관리");
        titleLabel.setFont(new Font("맑은 고딕", Font.BOLD, 20));
        titleLabel.setForeground(Color.RED);
        titlePanel.add(titleLabel);
        add(titlePanel, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel(new GridLayout(2, 3, 10, 10));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        setFont(btnRoom);
        setFont(btnCustomer);
        setFont(btnReport);
        setFont(btnUser);
        setFont(btnService);
        setFont(btnMenu);

        buttonPanel.add(btnRoom);
        buttonPanel.add(btnCustomer);
        buttonPanel.add(btnReport);
        buttonPanel.add(btnUser);
        buttonPanel.add(btnService);
        buttonPanel.add(btnMenu);
        
        add(buttonPanel, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel();
        bottomPanel.add(btnBack);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private void setFont(JButton btn) {
        btn.setFont(new Font("맑은 고딕", Font.BOLD, 16));
    }

    // 리스너 등록 메서드들
    public void setRoomAdminListener(ActionListener l) { btnRoom.addActionListener(l); }
    public void setCustomerListener(ActionListener l) { btnCustomer.addActionListener(l); }
    public void setReportListener(ActionListener l) { btnReport.addActionListener(l); }
    public void setUserListener(ActionListener l) { btnUser.addActionListener(l); }
    public void setServiceListener(ActionListener l) { btnService.addActionListener(l); }
    public void setMenuListener(ActionListener l) { btnMenu.addActionListener(l); }
    public void setBackListener(ActionListener l) { btnBack.addActionListener(l); }

    public void showMessage(String msg) {
        JOptionPane.showMessageDialog(this, msg);
    }
}