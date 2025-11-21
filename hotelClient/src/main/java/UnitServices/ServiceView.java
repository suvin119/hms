package UnitServices;

import javax.swing.*;
import java.awt.*;

public class ServiceView extends JPanel {
    public JTextField tfReservationId = new JTextField(10);
    public JTextField tfServiceId = new JTextField(10);
    public JTextField tfQuantity = new JTextField(10);
    public JButton btnAddService = new JButton("서비스 추가");
    public JTextArea taInfo = new JTextArea(10, 30);

    public ServiceView() {
        setLayout(new BorderLayout());
        JPanel inputPanel = new JPanel();
        inputPanel.add(new JLabel("예약ID:")); inputPanel.add(tfReservationId);
        inputPanel.add(new JLabel("서비스ID:")); inputPanel.add(tfServiceId);
        inputPanel.add(new JLabel("수량:")); inputPanel.add(tfQuantity);
        inputPanel.add(btnAddService);

        add(inputPanel, BorderLayout.NORTH);
        add(new JScrollPane(taInfo), BorderLayout.CENTER);
    }
}
