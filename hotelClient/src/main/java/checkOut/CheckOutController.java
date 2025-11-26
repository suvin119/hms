package checkOut;

import Pay.BookingInfo;
import Pay.ExtraChargeInfo;

import javax.swing.*;
import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class CheckOutController {

    private CheckOutView view;
    private BookingInfo currentBooking;
    private Runnable onSuccessCallback;

    // 서버 IP
    private final String SERVER_IP = "127.0.0.1";
    private final int SERVER_PORT = 9999;

    // rooms.txt & service_usage.txt 파일 경로
    private final String ROOMS_FILE = "src/main/java/hms/resources/rooms.txt";
    private final String SERVICES_FILE = "src/main/java/hms/resources/service_usage.txt";

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
        // Search 버튼 클릭
        view.addSearchListener(e -> {
            String roomStr = view.getRoomNumber();
            if (roomStr.isEmpty()) {
                view.showMessage("방 번호를 입력하세요.", "오류", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                int roomId = Integer.parseInt(roomStr);
                BookingInfo booking = loadBookingFromRooms(roomId);
                if (booking != null) {
                    setCurrentBooking(booking);
                } else {
                    view.showMessage("해당 방에 체크인 정보가 없습니다.", "알림", JOptionPane.INFORMATION_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                view.showMessage("방 번호는 숫자만 가능합니다.", "오류", JOptionPane.ERROR_MESSAGE);
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

    /** rooms.txt에서 체크인 정보 조회 */
    private BookingInfo loadBookingFromRooms(int roomId) {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(ROOMS_FILE), "UTF-8"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length < 4) continue;

                int id = Integer.parseInt(parts[0]);
                double baseRate = Double.parseDouble(parts[2]);
                String status = parts[3];

                if (id == roomId && "투숙중".equals(status)) {
                    List<ExtraChargeInfo> extras = loadServicesForRoom(roomId);
                    LocalDate checkIn = LocalDate.now().minusDays(1); // 예시 체크인
                    LocalDate plannedCheckOut = LocalDate.now().plusDays(1); // 예시 체크아웃

                    return new BookingInfo(roomId, "고객" + roomId, checkIn, plannedCheckOut, baseRate, 0, extras);
                }
            }
        } catch (Exception e) {
            System.err.println("rooms.txt 읽기 오류: " + e.getMessage());
        }
        return null;
    }

    /** service_usage.txt에서 해당 방 부대 서비스 읽기 */
    private List<ExtraChargeInfo> loadServicesForRoom(int roomId) {
        List<ExtraChargeInfo> services = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(SERVICES_FILE), "UTF-8"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length < 4) continue;

                int rId = Integer.parseInt(parts[0]);
                if (rId != roomId) continue;

                int serviceType = Integer.parseInt(parts[2]);
                double amount = Double.parseDouble(parts[3]);
                String serviceName;
                switch (serviceType) {
                    case 1: serviceName = "룸 서비스"; break;
                    case 2: serviceName = "미니바"; break;
                    case 3: serviceName = "세탁"; break;
                    case 4: serviceName = "식당"; break;
                    default: serviceName = "기타"; break;
                }
                services.add(new ExtraChargeInfo(serviceName, amount));
            }
        } catch (Exception e) {
            System.err.println("service_usage.txt 읽기 오류: " + e.getMessage());
        }
        return services;
    }
}
