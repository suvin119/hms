package checkOut;

import Pay.BookingInfo;
import Pay.ExtraChargeInfo;

import javax.swing.*;
import java.io.*;
import java.net.Socket;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class CheckOutController {

    private CheckOutView view;
    private BookingInfo currentBooking;
    private Runnable onSuccessCallback;

    // 서버 IP & PORT
    private final String SERVER_IP = "127.0.0.1";
    private final int SERVER_PORT = 9999;

    public CheckOutController(CheckOutView view) {
        this.view = view;
        initListeners();
    }

    public void setOnSuccess(Runnable callback) {
        this.onSuccessCallback = callback;
    }

    /** 현재 체크아웃할 BookingInfo 설정 */
    public void setCurrentBooking(BookingInfo booking) {
        this.currentBooking = booking;
        updateView();
    }

    /** 초기 버튼 리스너 연결 */
    private void initListeners() {

        // ==========================
        // 1) 방 검색 버튼
        // ==========================
        view.addSearchListener(e -> {
            String roomStr = view.getRoomNumber();
            if (roomStr.isEmpty()) {
                view.showMessage("방 번호를 입력하세요.", "오류", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                int roomId = Integer.parseInt(roomStr);

                BookingInfo booking = loadBookingFromServer(roomId);

                if (booking != null) {
                    setCurrentBooking(booking);
                } else {
                    view.showMessage("해당 방에 체크인 정보가 없습니다.", "알림", JOptionPane.INFORMATION_MESSAGE);
                }

            } catch (NumberFormatException ex) {
                view.showMessage("방 번호는 숫자만 가능합니다.", "오류", JOptionPane.ERROR_MESSAGE);
            }
        });

        // ==========================
        // 2) 체크아웃 버튼
        // ==========================
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
            
            /// 체크아웃 완료/실패
            boolean updateSuccess = processCheckoutOnServer(currentBooking.getRoomId());

            if (updateSuccess) {
                JOptionPane.showMessageDialog(view,
                        "결제가 완료되었습니다.\n총 금액: " + String.format("%,.0f원", total),
                        "체크아웃 완료", JOptionPane.INFORMATION_MESSAGE);

                if (onSuccessCallback != null) {
                    onSuccessCallback.run();
                }

                view.resetView();
                currentBooking = null;
            } else {
                view.showMessage("서버 통신 오류로 체크아웃 처리에 실패했습니다.", "오류", JOptionPane.ERROR_MESSAGE);
            }
        });

    }

    /** 뷰 갱신 */
    private void updateView() {
        if (currentBooking != null) {
            view.displayBookingInfo(currentBooking);
        }
    }
    
    //추가========
    private boolean processCheckoutOnServer(int roomId) {
        String response = sendServerRequest("CHECKOUT|" + roomId);
        return response != null && response.startsWith("OK|");
    }
   //===================

    // =====================================================
    // ========== 서버에서 rooms.txt 읽기 ================
    // =====================================================
private BookingInfo loadBookingFromServer(int roomId) {
    String response = sendServerRequest("ROOMS_LOAD|" + roomId);

    if (response == null || !response.startsWith("OK|")) return null;

    String[] rows = response.substring(3).split("#");
    for (String row : rows) {
        String[] parts = row.split("\\|");
        int id = Integer.parseInt(parts[0]);
        String type = parts[1];
        double baseRate = Double.parseDouble(parts[2]);
        String status = parts[3];

        if (id == roomId && status.equals("투숙중")) {
            List<ExtraChargeInfo> extras = loadExtraServicesFromServer(roomId);
            LocalDate checkIn = LocalDate.now().minusDays(1);
            LocalDate plannedOut = LocalDate.now().plusDays(1);
            return new BookingInfo(roomId, "고객" + roomId, checkIn, plannedOut, baseRate, 0, extras);
        }
    }
    return null;
}

//======================
//부대 서비스 데이터 로딩
//=======================


private List<ExtraChargeInfo> loadExtraServicesFromServer(int roomId) {
    List<ExtraChargeInfo> list = new ArrayList<>();
    String response = sendServerRequest("ROOMS_SERVICE_USAGE|" + roomId);

    if (response == null || !response.startsWith("OK|")) return list;

    String[] rows = response.substring(3).split("#");
    for (String row : rows) {
        String[] parts = row.split("\\|");
        if (parts.length < 4) continue;

        int rId = Integer.parseInt(parts[0]);
        if (rId != roomId) continue;

        int serviceType = Integer.parseInt(parts[1]);
        double amount = Double.parseDouble(parts[3]);

        // 메뉴 ID를 한글 이름으로 변환
        String menuName = switch (serviceType) {
            case 1 -> "룸 서비스";
            case 2 -> "미니바";
            case 3 -> "세탁";
            case 4 -> "식당";
            default -> "기타 서비스";
        };
        
        list.add(new ExtraChargeInfo(menuName, amount));
    }
    return list;
}


    // =====================================================
    // ============== 서버 메시지 전송 메서드 ==============
    // =====================================================
    private String sendServerRequest(String msg) {
        try (Socket socket = new Socket(SERVER_IP, SERVER_PORT);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

            out.println(msg);
            return in.readLine();

        } catch (Exception e) {
            System.out.println("[CheckOut] 서버 요청 실패: " + e.getMessage());
        }
        return null;
    }
}
