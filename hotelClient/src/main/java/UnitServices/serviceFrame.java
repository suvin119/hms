package UnitServices;

import javax.swing.*;
import java.awt.*;

public class serviceFrame extends JFrame {

    public serviceFrame() {
        setTitle("부대 서비스 관리");
        setSize(650, 450);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel(new GridLayout(5, 2, 10, 10));

        JComboBox<String> serviceList = new JComboBox<>(
                new String[]{"레스토랑", "룸서비스", "수영장", "스파"}
        );

        JButton btnAdd = new JButton("서비스 추가");
        JButton btnHistory = new JButton("사용내역 조회");

        panel.add(new JLabel("서비스 선택:"));
        panel.add(serviceList);
        panel.add(btnAdd);
        panel.add(btnHistory);

        add(panel);
    }

    public static void main(String[] args) {
        new serviceFrame().setVisible(true);
    }
}


