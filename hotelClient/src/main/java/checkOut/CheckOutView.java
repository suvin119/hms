package checkOut;

import Pay.BillingController;
import Pay.BookingInfo;
import Pay.ExtraChargeInfo;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * View: 체크아웃 UI 구현
 */
public class CheckoutView extends JPanel {
    
    private BillingController controller;
    private JTextField roomField;
    private JLabel guestLabel, plannedDateLabel, totalBillLabel;
    private JTable extraChargeTable;
    private JButton searchButton, checkoutButton;

    private int currentRoomId = -1;
    private BookingInfo currentBooking;

    public CheckoutView() {
        this.controller = new BillingController(); 
        setLayout(new BorderLayout(10, 10));
        
        // UI 구성 (생략된 부분은 이전 코드와 동일)
        JPanel searchPanel = new JPanel(new FlowLayout());
        roomField = new JTextField(5);
        searchButton = new JButton("객실 정보 조회");
        searchButton.addActionListener(e -> loadBookingDetails());
        searchPanel.add(new JLabel("방 번호:"));
        searchPanel.add(roomField);
        searchPanel.add(searchButton);
        add(searchPanel, BorderLayout.NORTH);
        
        JPanel mainPanel = new JPanel(new GridLayout(2, 1));
        
        JPanel infoPanel = new JPanel(new GridLayout(3, 2));
        guestLabel = new JLabel("고객명: -");
        plannedDateLabel = new JLabel("예정 체크아웃: -");
        infoPanel.add(guestLabel);
        infoPanel.add(plannedDateLabel);
        mainPanel.add(infoPanel);

        String[] columnNames = {"서비스", "금액"};
        extraChargeTable = new JTable(new DefaultTableModel(columnNames, 0));
        mainPanel.add(new JScrollPane(extraChargeTable));
        
        add(mainPanel, BorderLayout.CENTER);
        
        JPanel bottomPanel = new JPanel(new BorderLayout());
        totalBillLabel = new JLabel("최종 청구 금액: 0.0원", SwingConstants.RIGHT);
        totalBillLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        checkoutButton = new JButton("체크아웃 및 결제 완료");
        checkoutButton.setEnabled(false); 
        checkoutButton.addActionListener(e -> processCheckout());

        bottomPanel.add(totalBillLabel, BorderLayout.NORTH);
        bottomPanel.add(checkoutButton, BorderLayout.SOUTH);
        add(bottomPanel, BorderLayout.SOUTH);
    }
    
    private void loadBookingDetails() {
        try {
            currentRoomId = Integer.parseInt(roomField.getText().trim());
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "유효한 방 번호를 입력하세요.", "오류", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Optional<BookingInfo> bookingOpt = controller.getBookingDetails(currentRoomId);

        if (bookingOpt.isPresent()) {
            currentBooking = bookingOpt.get();
            
            // 1. 고객 및 예약 정보 업데이트
            guestLabel.setText("고객명: " + currentBooking.getGuestName());
            plannedDateLabel.setText("예정 체크아웃: " + currentBooking.getPlannedCheckOutDate());
            
            // 2. 부대 서비스 JTable 업데이트
            updateExtraChargeTable(currentBooking.getExtraCharges());

            // 3. 최종 금액 계산 및 표시
            calculateAndDisplayTotalBill(currentRoomId);
            
            checkoutButton.setEnabled(true);
        } else {
            JOptionPane.showMessageDialog(this, "해당 방의 예약 정보를 찾을 수 없습니다.", "정보 없음", JOptionPane.WARNING_MESSAGE);
            checkoutButton.setEnabled(false);
            resetView();
        }
    }
    
    private void updateExtraChargeTable(List<ExtraChargeInfo> charges) {
        DefaultTableModel model = (DefaultTableModel) extraChargeTable.getModel();
        model.setRowCount(0);
        for (ExtraChargeInfo charge : charges) {
            model.addRow(new Object[]{
                charge.getServiceName(),
                String.format("%.2f", charge.getAmount())
            });
        }
    }
    
    private void calculateAndDisplayTotalBill(int roomId) {
        LocalDate actualCheckOutDate = LocalDate.now(); 
        double totalBill = controller.calculateFinalBill(roomId, actualCheckOutDate);
        
        if (totalBill >= 0) {
            totalBillLabel.setText(String.format("최종 청구 금액: %.2f원", totalBill));
        } else {
            totalBillLabel.setText("최종 청구 금액: 계산 오류");
        }
    }

    private void processCheckout() {
        if (currentRoomId == -1 || currentBooking == null) return;

        // 테스트를 위해 예정일보다 하루 늦게 체크아웃 처리 (추가 요금 발생 유도)
        LocalDate actualDate = currentBooking.getPlannedCheckOutDate().plusDays(1); 
        
        double finalBill = controller.calculateFinalBill(currentRoomId, actualDate);
        
        String message = String.format("총 금액 %.2f원을 결제하고 체크아웃 하시겠습니까?\n(실제 체크아웃 날짜: %s)", finalBill, actualDate);
        int confirm = JOptionPane.showConfirmDialog(this, message, "결제 확인", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            boolean success = controller.processFinalCheckout(currentRoomId, actualDate, finalBill);
            
            if (success) {
                JOptionPane.showMessageDialog(this, "체크아웃이 완료되었습니다!", "성공", JOptionPane.INFORMATION_MESSAGE);
                resetView();
            } else {
                JOptionPane.showMessageDialog(this, "체크아웃 처리 중 오류가 발생했습니다.", "오류", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void resetView() {
        currentRoomId = -1;
        currentBooking = null;
        roomField.setText("");
        guestLabel.setText("고객명: -");
        plannedDateLabel.setText("예정 체크아웃: -");
        totalBillLabel.setText("최종 청구 금액: 0.0원");
        ((DefaultTableModel) extraChargeTable.getModel()).setRowCount(0);
        checkoutButton.setEnabled(false);
    }
    
    // 메인 실행 예시
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("호텔 체크아웃 관리 시스템 (checkOut)");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.add(new CheckoutView());
            frame.setSize(600, 500);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}