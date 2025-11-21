package checkOut;

import javax.swing.*;
import java.awt.*;

public class CheckOutView extends JPanel {
    public JTextField tfReservationId = new JTextField(10);
    public JTextField tfRoomNumber = new JTextField(5);
    public JTextField tfCheckOutDate = new JTextField(10);
    public JButton btnCalculate = new JButton("체크아웃 요금 계산");
    public JTextArea taInfo = new JTextArea(10, 30);

    public CheckOutView() {
        setLayout(new BorderLayout());
        JPanel top = new JPanel();
        top.add(new JLabel("예약ID:")); top.add(tfReservationId);
        top.add(new JLabel("객실:")); top.add(tfRoomNumber);
        top.add(new JLabel("실제 체크아웃 날짜:")); top.add(tfCheckOutDate);
        top.add(btnCalculate);
        add(top, BorderLayout.NORTH);

        taInfo.setEditable(false);
        add(new JScrollPane(taInfo), BorderLayout.CENTER);
    }
}
