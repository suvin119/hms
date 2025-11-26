package checkOut;

import Pay.BookingInfo;
import Pay.ExtraChargeInfo;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * View: 체크아웃 UI 구현. 
 * 모든 비즈니스 로직은 Controller(CheckOutController)에게 위임합니다.
 * @author subin
 */
public class CheckOutView extends JPanel {
    
    // UI 요소 선언
    private JTextField roomField;
    private JLabel guestLabel, plannedDateLabel, totalBillLabel;
    private JTable extraChargeTable;
    private JButton searchButton, checkoutButton, backButton; // 💡 뒤로가기 버튼 추가

    public CheckOutView() {
        setLayout(new BorderLayout(15, 15));
        
        // ------------------ 1. 검색 패널 (NORTH) ------------------
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        roomField = new JTextField(8);
        searchButton = new JButton("객실 정보 조회 (Search)");
        
        searchPanel.add(new JLabel("방 번호:"));
        searchPanel.add(roomField);
        searchPanel.add(searchButton);
        add(searchPanel, BorderLayout.NORTH);
        
        // ------------------ 2. 메인 정보 패널 (CENTER) ------------------
        JPanel mainPanel = new JPanel(new GridLayout(2, 1, 10, 10));
        
        // 2-1. 예약 정보 표시 패널
        JPanel infoPanel = new JPanel(new GridLayout(2, 1, 5, 5));
        infoPanel.setBorder(BorderFactory.createTitledBorder("예약 정보"));
        guestLabel = new JLabel("고객명: -");
        plannedDateLabel = new JLabel("예정 체크아웃: -");
        infoPanel.add(guestLabel);
        infoPanel.add(plannedDateLabel);
        
        mainPanel.add(infoPanel);

        // 2-2. 부대 서비스 테이블
        String[] columnNames = {"서비스 항목", "금액 (원)"};
        extraChargeTable = new JTable(new DefaultTableModel(columnNames, 0));
        JScrollPane tableScrollPane = new JScrollPane(extraChargeTable);
        tableScrollPane.setBorder(BorderFactory.createTitledBorder("부대 서비스 요금"));
        
        mainPanel.add(tableScrollPane);
        
        add(mainPanel, BorderLayout.CENTER);
        
        // ------------------ 3. 결제 및 버튼 패널 (SOUTH) ------------------
        JPanel bottomPanel = new JPanel(new BorderLayout(0, 10));
        
        totalBillLabel = new JLabel("최종 청구 금액: 0원", SwingConstants.RIGHT);
        totalBillLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
        totalBillLabel.setForeground(new Color(0, 100, 0)); // 진한 녹색
        
        checkoutButton = new JButton("결제 및 체크아웃 완료 (Checkout)");
        checkoutButton.setEnabled(false);    // 조회 전에는 비활성화
        
        backButton = new JButton("뒤로가기 (Back)");
        
        JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 10, 0));
        buttonPanel.add(backButton);
        buttonPanel.add(checkoutButton);

        bottomPanel.add(totalBillLabel, BorderLayout.NORTH);
        bottomPanel.add(buttonPanel, BorderLayout.SOUTH);
        add(bottomPanel, BorderLayout.SOUTH);
    }
    
    // ----------------------------------------------------
    // Controller 연결을 위한 공개 메서드 (Getter & Listener Setter)
    // ----------------------------------------------------

    /** @return JTextField에 입력된 방 번호 문자열 */
    public String getRoomNumber() {
        return roomField.getText().trim();
    }
    
    /** Controller가 Search 버튼 클릭 이벤트를 처리할 수 있도록 리스너를 등록합니다. */
    public void addSearchListener(ActionListener listener) {
        searchButton.addActionListener(listener);
    }

    /** Controller가 Checkout 버튼 클릭 이벤트를 처리할 수 있도록 리스너를 등록합니다. */
    public void addCheckoutListener(ActionListener listener) {
        checkoutButton.addActionListener(listener);
    }
    
    /** Controller가 Back 버튼 클릭 이벤트를 처리할 수 있도록 리스너를 등록합니다. */
    public void addBackListener(ActionListener listener) {
        backButton.addActionListener(listener);
    }

    // ----------------------------------------------------
    // Controller가 UI 상태를 업데이트하기 위한 메서드
    // ----------------------------------------------------

    /** Controller가 조회된 예약 정보를 View에 표시 */
    public void displayBookingInfo(BookingInfo booking) {
        guestLabel.setText("고객명: " + booking.getGuestName());
        plannedDateLabel.setText("예정 체크아웃: " + booking.getPlannedCheckOutDate());
        
        updateExtraChargeTable(booking.getExtraCharges());
        checkoutButton.setEnabled(true);
    }
    
    /** Controller가 부대 서비스 JTable을 업데이트 */
    public void updateExtraChargeTable(List<ExtraChargeInfo> charges) {
        DefaultTableModel model = (DefaultTableModel) extraChargeTable.getModel();
        model.setRowCount(0);
        for (ExtraChargeInfo charge : charges) {
            model.addRow(new Object[]{
                charge.getServiceName(),
                String.format("%,.0f원", charge.getAmount()) // 금액 포맷 수정
            });
        }
    }
    
    /** Controller가 최종 청구 금액을 표시 */
    public void displayTotalBill(double totalBill) {
        if (totalBill >= 0) {
            totalBillLabel.setText(String.format("최종 청구 금액: %,.0f원", totalBill)); // 금액 포맷 수정
        } else {
            totalBillLabel.setText("최종 청구 금액: 계산 오류");
        }
    }
    
    /** View의 모든 상태를 초기화 */
    public void resetView() {
        roomField.setText("");
        guestLabel.setText("고객명: -");
        plannedDateLabel.setText("예정 체크아웃: -");
        totalBillLabel.setText("최종 청구 금액: 0원");
        ((DefaultTableModel) extraChargeTable.getModel()).setRowCount(0);
        checkoutButton.setEnabled(false);
    }

    /** Controller가 사용자에게 메시지 표시 */
    public void showMessage(String message, String title, int messageType) {
        JOptionPane.showMessageDialog(this, message, title, messageType);
    }
}