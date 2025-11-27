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

            JOptionPane.showMessageDialog(view,
                    "결제가 완료되었습니다.\n총 금액: " + String.format("%,.0f원", total),
                    "체크아웃 완료", JOptionPane.INFORMATION_MESSAGE);

            if (onSuccessCallback != null) {
                onSuccessCallback.run();
            }

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

    // =====================================================
    // ========== 서버에서 rooms.txt 읽기 ================
    // =====================================================
    private BookingInfo loadBookingFromServer(int roomId) {
        String response = sendServerRequest("ROOM_LOAD");

        if (response == null || !response.startsWith("OK|")) {
            return null; // 방 정보 불러오기 실패
        }

        // "OK|방ID|타입|요금|상태#방ID|타입|요금|상태#..."
        String data = response.substring(3);
        String[] rows = data.split("#");

        for (String row : rows) {
            String[] parts = row.split("\\|");
            if (parts.length < 4) continue;

            int id = Integer.parseInt(parts[0]);
            String type = parts[1];
            double baseRate = Double.parseDouble(parts[2]);
            String status = parts[3];

            if (id == roomId && status.equals("투숙중")) {

                // 부대 서비스도 서버에서 불러오기
                List<ExtraChargeInfo> extras = loadExtraServicesFromServer(roomId);

                LocalDate checkIn = LocalDate.now().minusDays(1);
                LocalDate plannedOut = LocalDate.now().plusDays(1);

                return new BookingInfo(roomId, "고객" + roomId, checkIn, plannedOut, baseRate, 0, extras);
            }
        }

        return null;
    }

    // =====================================================
    // ===== 서버에서 service_usage.txt 읽기 (부대서비스) =====
    // =====================================================
    private List<ExtraChargeInfo> loadExtraServicesFromServer(int roomId) {
        List<ExtraChargeInfo> list = new ArrayList<>();

        String response = sendServerRequest("ROOM_SERVICE_USAGE");
        if (response == null || !response.startsWith("OK|")) {
            return list;
        }

        // "OK|방ID|서비스ID|타입|금액#방ID|서비스ID|타입|금액..."
        String data = response.substring(3);
        String[] rows = data.split("#");

        for (String row : rows) {
            String[] parts = row.split("\\|");
            if (parts.length < 4) continue;

            int rId = Integer.parseInt(parts[0]);
            if (rId != roomId) continue;

            int serviceType = Integer.parseInt(parts[2]);
            double amount = Double.parseDouble(parts[3]);

            String name;
            switch (serviceType) {
                case 1: name = "룸 서비스"; break;
                case 2: name = "미니바"; break;
                case 3: name = "세탁"; break;
                case 4: name = "식당"; break;
                default: name = "기타"; break;
            }

            list.add(new ExtraChargeInfo(name, amount));
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
