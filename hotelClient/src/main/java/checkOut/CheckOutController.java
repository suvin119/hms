package checkOut;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import javax.swing.JOptionPane; 
import javax.swing.JPanel;

// Room 클래스
import roomAdmin.Room; 

// Pay 패키지의 클래스들
import Pay.BillingController; 
import Pay.BookingInfo; 

public class CheckOutController {

    private final CheckOutView view; 
    private List<Room> rooms; // 객실 목록 (DB 대용)
    private BillingController billingController; 
    private BookingInfo currentBooking; 

    // 💡 1. 메인 화면으로 돌아가기 위한 콜백 필드 추가 (Runnable은 성공/뒤로가기 모두 사용)
    private Runnable onNavigateToMain;

    public CheckOutController(CheckOutView view, List<Room> initialRooms) { 
        this.view = view;
        this.rooms = initialRooms; 
        this.billingController = new BillingController(); 

        view.addSearchListener(new SearchListener()); 
        view.addCheckoutListener(new CheckOutListener()); 
        view.addBackListener(new BackListener());
    }
    
    // 💡 2. Main.java에서 호출하는 setOnSuccess 메서드 구현
    public void setOnSuccess(Runnable action) {
        this.onNavigateToMain = action;
    }
    
    // 💡 3. Main.java에서 호출하는 getView() 메서드 구현 (이미 있었지만 재확인)
public CheckOutView getView() { 
        return this.view; 
    }
    
    // ----------------------------
    // 1. 객실 정보 조회 및 계산 리스너
    // ----------------------------
    class SearchListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String roomNumStr = view.getRoomNumber();
            
            int currentRoomId;
            try {
                // 방 번호 입력 오류 처리
                currentRoomId = Integer.parseInt(roomNumStr);
            } catch (NumberFormatException ex) {
                view.showMessage("유효한 방 번호를 입력하세요.", "오류", JOptionPane.ERROR_MESSAGE);
                view.resetView();
                return;
            }

            // 1. BillingController를 통해 예약 정보 조회
            Optional<BookingInfo> bookingOpt = billingController.getBookingDetails(currentRoomId);

            if (bookingOpt.isPresent()) {
                currentBooking = bookingOpt.get();
                
                // 2. View에 고객 및 부대 서비스 정보 업데이트
                view.displayBookingInfo(currentBooking); 

                // 3. 최종 금액 계산 (부대 서비스 + 지연 수수료 포함)
                LocalDate actualCheckOutDate = LocalDate.now();    
                double totalBill = billingController.calculateFinalBill(currentRoomId, actualCheckOutDate);
                view.displayTotalBill(totalBill);
                
            } else {
                view.showMessage("해당 방의 예약 정보를 찾을 수 없습니다.", "정보 없음", JOptionPane.WARNING_MESSAGE);
                view.resetView();
            }
        }
    }

    // ----------------------------
    // 2. 체크아웃 및 결제 리스너
    // ----------------------------
    class CheckOutListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (currentBooking == null) {
                view.showMessage("먼저 객실 조회를 해주세요.", "경고", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int currentRoomId = currentBooking.getRoomId();
            String roomNumStr = String.valueOf(currentRoomId); 
            
            // 테스트를 위해 예정일보다 하루 늦게 체크아웃 처리 (지연 수수료 계산 유도)
            LocalDate actualDate = currentBooking.getPlannedCheckOutDate().plusDays(1);    
            
            // 1. 최종 금액 재계산
            double finalBill = billingController.calculateFinalBill(currentRoomId, actualDate);
            
            String message = String.format("총 금액 %,.0f원을 결제하고 체크아웃 하시겠습니까?\n(실제 체크아웃 날짜: %s)", finalBill, actualDate);
            
            int confirm = JOptionPane.showConfirmDialog(view, message, "결제 확인", JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                // 2. BillingController를 통해 최종 결제 및 DB 처리
                boolean success = billingController.processFinalCheckout(currentRoomId, actualDate, finalBill);
                
                if (success) {
                    // 3. Room 상태 변경 (OCCUPIED -> AVAILABLE)
                    Room room = findRoom(roomNumStr); 
                    if (room != null) {
                        room.setStatus(Room.Status.AVAILABLE); 
                    }
                    
                    view.showMessage("체크아웃이 완료되었습니다!", "성공", JOptionPane.INFORMATION_MESSAGE);
                    view.resetView();
                    currentBooking = null; // 상태 초기화
                    
                    // 💡 4. 체크아웃 성공 시 메인 화면으로 이동 요청
                    if (onNavigateToMain != null) {
                        onNavigateToMain.run();
                    }
                } else {
                    view.showMessage("체크아웃 처리 중 오류가 발생했습니다.", "오류", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }
    
    // ----------------------------
    // 3. 뒤로가기 리스너 (Back 버튼)
    // ----------------------------
    class BackListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            // 💡 5. Main.java에 화면 전환을 위임 (체크아웃 성공 시와 동일한 콜백 사용)
            if (onNavigateToMain != null) {
                onNavigateToMain.run();
            }
        }
    }
    
    // 방 번호로 Room 객체 찾기 (Room 객체는 String 방 번호를 사용한다고 가정)
    private Room findRoom(String roomNum) {
        for (Room r : rooms) {
            if (r.getRoomNumber().equals(roomNum)) return r;
        }
        return null;
    }
}