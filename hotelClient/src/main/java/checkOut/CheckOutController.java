package checkOut;

import Pay.BookingInfo;
import Pay.ExtraChargeInfo;

import javax.swing.*;
import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class CheckOutController {

    private CheckOutView view;
    private BookingInfo currentBooking;
    private Runnable onSuccessCallback;

    // 로컬 파일 경로
    private final String ROOMS_FILE = "hotelServer/src/main/java/hms/resources/rooms.txt";
    private final String SERVICES_FILE = "hotelServer/src/main/java/hms/resources/service_usage.txt";

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

    /** 리스너 초기화 */
    private void initListeners() {

        /** 🔍 Search 버튼 */
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
                view.showMessage("방 번호는 숫자만 입력 가능합니다.", "오류", JOptionPane.ERROR_MESSAGE);
            }
        });

        /** ✔ Checkout 버튼 */
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

            /** 🔥 체크아웃 완료 → rooms.txt 상태 변경 */
            updateRoomStatus(currentBooking.getRoomId(), "사용가능");

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

    /** rooms.txt에서 체크인된 방인지 확인하고 BookingInfo 생성 */
    private BookingInfo loadBookingFromRooms(int roomId) {
        try (BufferedReader br = new BufferedReader(new FileReader(ROOMS_FILE))) {

            String line;
            while ((line = br.readLine()) != null) {

                String[] parts = line.split("\\|");

                if (parts.length < 4) continue;

                int id = Integer.parseInt(parts[0]);
                double baseRate = Double.parseDouble(parts[2]);
                String status = parts[3];

                // 🔍 방 번호 일치 + 투숙중인지 확인
                if (id == roomId && status.equals("투숙중")) {

                    // 부대 서비스 불러오기
                    List<ExtraChargeInfo> extras = loadServicesForRoom(roomId);

                    // 체크인/체크아웃 날짜는 예시 값
                    LocalDate checkIn = LocalDate.now().minusDays(1);
                    LocalDate plannedCheckOut = LocalDate.now().plusDays(1);

                    return new BookingInfo(
                            roomId,
                            "고객" + roomId,
                            checkIn,
                            plannedCheckOut,
                            baseRate,
                            0,
                            extras
                    );
                }
            }

        } catch (Exception e) {
            System.err.println("rooms.txt 읽기 오류: " + e.getMessage());
        }
        return null;
    }

    /** 🔗 service_usage.txt에서 해당 방의 서비스 읽기 */
    private List<ExtraChargeInfo> loadServicesForRoom(int roomId) {

        List<ExtraChargeInfo> serviceList = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(SERVICES_FILE))) {

            String line;
            while ((line = br.readLine()) != null) {

                String[] parts = line.split("\\|");
                if (parts.length < 4) continue;

                int rId = Integer.parseInt(parts[0]);
                if (rId != roomId) continue;

                int serviceType = Integer.parseInt(parts[2]);
                double amount = Double.parseDouble(parts[3]);

                String serviceName = switch (serviceType) {
                    case 1 -> "룸 서비스";
                    case 2 -> "미니바";
                    case 3 -> "세탁";
                    case 4 -> "식당";
                    default -> "기타";
                };

                serviceList.add(new ExtraChargeInfo(serviceName, amount));
            }

        } catch (Exception e) {
            System.err.println("service_usage.txt 읽기 오류: " + e.getMessage());
        }

        return serviceList;
    }

    /** 체크아웃 후 rooms.txt 상태를 "사용가능"으로 변경 */
    private void updateRoomStatus(int roomId, String newStatus) {
        try {

            List<String> updatedLines = new ArrayList<>();

            try (BufferedReader br = new BufferedReader(new FileReader(ROOMS_FILE))) {

                String line;
                while ((line = br.readLine()) != null) {

                    String[] parts = line.split("\\|");
                    if (parts.length < 4) continue;

                    int id = Integer.parseInt(parts[0]);

                    if (id == roomId) {
                        updatedLines.add(id + "|" + parts[1] + "|" + parts[2] + "|" + newStatus);
                    } else {
                        updatedLines.add(line);
                    }
                }
            }

            try (BufferedWriter bw = new BufferedWriter(new FileWriter(ROOMS_FILE))) {
                for (String l : updatedLines) {
                    bw.write(l + "\n");
                }
            }

        } catch (Exception e) {
            System.err.println("rooms.txt 저장 오류: " + e.getMessage());
        }
    }
}
