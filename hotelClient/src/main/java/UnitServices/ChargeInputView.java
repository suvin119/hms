package UnitServices;

import javax.swing.*;
import java.awt.*;

/**
 * View: 부대 서비스 요금 추가 입력 UI 구현
 */
public class ChargeInputView extends JPanel {
    
    private ServiceController controller;
    private JTextField roomField;
    private JComboBox<String> serviceCombo;
    private JTextField amountField;
    private JButton addButton;

    public ChargeInputView() {
        this.controller = new ServiceController();
        setLayout(new GridLayout(5, 2, 10, 10));

        String[] services = {"룸 서비스", "미니바", "세탁", "식당"};
        serviceCombo = new JComboBox<>(services);
        roomField = new JTextField(5);
        amountField = new JTextField(5);
        addButton = new JButton("부대 비용 추가");
        
        addButton.addActionListener(e -> addExtraCharge());

        add(new JLabel("객실 번호:"));
        add(roomField);
        add(new JLabel("서비스 종류:"));
        add(serviceCombo);
        add(new JLabel("금액 (원):"));
        add(amountField);
        add(new JLabel("")); // 빈 공간
        add(addButton);
    }

    private void addExtraCharge() {
        int roomId;
        double amount;
        String serviceName = (String) serviceCombo.getSelectedItem();

        try {
            roomId = Integer.parseInt(roomField.getText().trim());
            amount = Double.parseDouble(amountField.getText().trim());
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "객실 번호와 금액을 숫자로 정확히 입력하세요.", 
                    "입력 오류", JOptionPane.ERROR_MESSAGE);
            return;
        }

        boolean success = controller.addCharge(roomId, serviceName, amount);

        if (success) {
            JOptionPane.showMessageDialog(this, String.format("Room %d에 %s (%.2f원)이 추가되었습니다.", 
                    roomId, serviceName, amount), "성공", JOptionPane.INFORMATION_MESSAGE);
            roomField.setText("");
            amountField.setText("");
        } else {
             JOptionPane.showMessageDialog(this, "부대 비용 추가에 실패했습니다. (Server 로그 확인)", 
                     "실패", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    // 메인 실행 예시
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("부대 서비스 요금 추가");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.add(new ChargeInputView());
            frame.setSize(400, 250);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}