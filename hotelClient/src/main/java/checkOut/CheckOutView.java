package checkOut;

import Pay.BookingInfo;
import Pay.ExtraChargeInfo;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class CheckOutView extends JPanel {

    private JTextField roomField;
    private JLabel guestLabel, plannedDateLabel, totalBillLabel;
    private JTable extraChargeTable;
    private JButton searchButton, checkoutButton, backButton;

    public CheckOutView() {
        setLayout(new BorderLayout(15, 15));

        // ------------------ 검색 패널 ------------------
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        roomField = new JTextField(8);
        searchButton = new JButton("객실 정보 조회 (Search)");
        searchPanel.add(new JLabel("방 번호:"));
        searchPanel.add(roomField);
        searchPanel.add(searchButton);
        add(searchPanel, BorderLayout.NORTH);

        // ------------------ 정보 패널 ------------------
        JPanel mainPanel = new JPanel(new GridLayout(2, 1, 10, 10));

        JPanel infoPanel = new JPanel(new GridLayout(2, 1, 5, 5));
        infoPanel.setBorder(BorderFactory.createTitledBorder("예약 정보"));
        guestLabel = new JLabel("고객명: -");
        plannedDateLabel = new JLabel("예정 체크아웃: -");
        infoPanel.add(guestLabel);
        infoPanel.add(plannedDateLabel);

        mainPanel.add(infoPanel);

        String[] columnNames = {"서비스 항목", "금액 (원)"};
        extraChargeTable = new JTable(new DefaultTableModel(columnNames, 0));
        JScrollPane tableScrollPane = new JScrollPane(extraChargeTable);
        tableScrollPane.setBorder(BorderFactory.createTitledBorder("부대 서비스 요금"));

        mainPanel.add(tableScrollPane);
        add(mainPanel, BorderLayout.CENTER);

        // ------------------ 버튼/총액 패널 ------------------
        JPanel bottomPanel = new JPanel(new BorderLayout(0, 10));
        totalBillLabel = new JLabel("최종 청구 금액: 0원", SwingConstants.RIGHT);
        totalBillLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
        totalBillLabel.setForeground(new Color(0, 100, 0));

        checkoutButton = new JButton("결제 및 체크아웃 완료 (Checkout)");
        checkoutButton.setEnabled(false);

        backButton = new JButton("뒤로가기 (Back)");

        JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 10, 0));
        buttonPanel.add(backButton);
        buttonPanel.add(checkoutButton);

        bottomPanel.add(totalBillLabel, BorderLayout.NORTH);
        bottomPanel.add(buttonPanel, BorderLayout.SOUTH);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    // ------------------ Getter / Listener ------------------
    public String getRoomNumber() {
        return roomField.getText().trim();
    }

    public void addSearchListener(ActionListener listener) {
        searchButton.addActionListener(listener);
    }

    public void addCheckoutListener(ActionListener listener) {
        checkoutButton.addActionListener(listener);
    }

    public void addBackListener(ActionListener listener) {
        backButton.addActionListener(listener);
    }

    // ------------------ 화면 업데이트 ------------------
    public void displayBookingInfo(BookingInfo booking) {
        guestLabel.setText("고객명: " + booking.getGuestName());
        plannedDateLabel.setText("예정 체크아웃: " + booking.getPlannedCheckOutDate());
        updateExtraChargeTable(booking.getExtraCharges());
        checkoutButton.setEnabled(true);
    }

    public void updateExtraChargeTable(List<ExtraChargeInfo> charges) {
        DefaultTableModel model = (DefaultTableModel) extraChargeTable.getModel();
        model.setRowCount(0);
        for (ExtraChargeInfo charge : charges) {
            model.addRow(new Object[]{
                    charge.getServiceName(),
                    String.format("%,.0f원", charge.getAmount())
            });
        }
        displayTotalBill(calculateTotal(charges));
    }

    public void displayTotalBill(double totalBill) {
        if (totalBill >= 0) {
            totalBillLabel.setText(String.format("최종 청구 금액: %,.0f원", totalBill));
        } else {
            totalBillLabel.setText("최종 청구 금액: 계산 오류");
        }
    }

    public void resetView() {
        roomField.setText("");
        guestLabel.setText("고객명: -");
        plannedDateLabel.setText("예정 체크아웃: -");
        totalBillLabel.setText("최종 청구 금액: 0원");
        ((DefaultTableModel) extraChargeTable.getModel()).setRowCount(0);
        checkoutButton.setEnabled(false);
    }

    public void showMessage(String message, String title, int messageType) {
        JOptionPane.showMessageDialog(this, message, title, messageType);
    }

    // ------------------ 새로운 메서드: JTable에 있는 부대서비스 금액 가져오기 ------------------
    public List<ExtraChargeInfo> getExtraCharges() {
        List<ExtraChargeInfo> list = new ArrayList<>();
        DefaultTableModel model = (DefaultTableModel) extraChargeTable.getModel();
        for (int i = 0; i < model.getRowCount(); i++) {
            String name = (String) model.getValueAt(i, 0);
            String valueStr = ((String) model.getValueAt(i, 1)).replaceAll("[^0-9.]", "");
            double amount = 0;
            try {
                amount = Double.parseDouble(valueStr);
            } catch (NumberFormatException ignored) {}
            list.add(new ExtraChargeInfo(name, amount));
        }
        return list;
    }

    // ------------------ 계산용 ------------------
    private double calculateTotal(List<ExtraChargeInfo> charges) {
        double total = 0;
        for (ExtraChargeInfo charge : charges) {
            total += charge.getAmount();
        }
        return total;
    }
}
