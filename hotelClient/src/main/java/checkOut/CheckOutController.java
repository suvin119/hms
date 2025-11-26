package checkOut;

import Pay.BookingInfo;
import Pay.ExtraChargeInfo;

import javax.swing.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class CheckOutController {

    private CheckOutView view;
    private BookingInfo currentBooking;

    private Runnable onSuccessCallback;

    public CheckOutController(CheckOutView view) {
        this.view = view;
        initListeners();
    }

    public void setOnSuccess(Runnable callback) {
        this.onSuccessCallback = callback;
    }

    /** 현재 체크아웃할 BookingInfo를 설정 */
    public void setCurrentBooking(BookingInfo booking) {
        this.currentBooking = booking;
        updateView();
    }

    /** Controller에서 Checkout 버튼 클릭 이벤트 처리 */
    private void initListeners() {
        // Search 버튼 클릭
        view.addSearchListener(e -> {
            String roomNumber = view.getRoomNumber();
            if (roomNumber.isEmpty()) {
                view.showMessage("방 번호를 입력하세요.", "오류", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (currentBooking != null && Integer.toString(currentBooking.getRoomId()).equals(roomNumber)) {
                view.displayBookingInfo(currentBooking);
            } else {
                view.showMessage("해당 방에 체크인 정보가 없습니다.", "알림", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        // Checkout 버튼 클릭
        view.addCheckoutListener(e -> {
            if (currentBooking == null) {
                view.showMessage("체크인 정보가 없습니다.", "오류", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // 총 금액 계산
            double total = currentBooking.getBaseRoomRate();
            if (currentBooking.getExtraCharges() != null) {
                for (ExtraChargeInfo extra : currentBooking.getExtraCharges()) {
                    total += extra.getAmount();
                }
            }
            total -= currentBooking.getPromotionalDiscount();
            if (total < 0) total = 0;

            view.displayTotalBill(total);

            // 성공 메시지
            JOptionPane.showMessageDialog(view, "결제가 완료되었습니다.\n총 금액: " + String.format("%,.0f원", total),
                    "체크아웃 완료", JOptionPane.INFORMATION_MESSAGE);

            // 체크아웃 완료 후 callback 실행
            if (onSuccessCallback != null) {
                onSuccessCallback.run();
            }

            // 뷰 초기화
            view.resetView();
            currentBooking = null;
        });
    }

    /** 뷰 갱신 */
    private void updateView() {
        if (currentBooking != null) {
            view.displayBookingInfo(currentBooking);
        }
    }

    /** 예시: ExtraCharge 생성 (룸 서비스, 미니바, 세탁, 식당) */
    public static List<ExtraChargeInfo> getExtraChargesForRoom(int roomId) {
        List<ExtraChargeInfo> extras = new ArrayList<>();
        extras.add(new ExtraChargeInfo("룸 서비스", 30000));
        extras.add(new ExtraChargeInfo("미니바", 20000));
        extras.add(new ExtraChargeInfo("세탁", 15000));
        extras.add(new ExtraChargeInfo("식당", 40000));
        return extras;
    }

    /** BookingInfo 생성 편의 메서드 */
    public static BookingInfo createBooking(String guestName, String checkInDateStr, String plannedCheckOutStr,
                                            double baseRate, double discount, List<ExtraChargeInfo> extras) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate checkIn = LocalDate.parse(checkInDateStr, formatter);
        LocalDate plannedCheckOut = LocalDate.parse(plannedCheckOutStr, formatter);

        return new BookingInfo(1, guestName, checkIn, plannedCheckOut, baseRate, discount, extras);
    }
}
