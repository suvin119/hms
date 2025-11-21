package hms.hotelserver;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CheckOutService {
    private static final String ROOM_FILE = "src/main/resources/rooms.txt";
    private static final String RES_FILE = "src/main/resources/reservations.txt";
    private ServiceServer serviceServer = new ServiceServer();

    public String findReservation(String reservationId) {
        File file = new File(RES_FILE);
        if (!file.exists()) return "FAIL|예약 데이터 없음";

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.startsWith(reservationId + "|")) {
                    return "SUCCESS|" + line;
                }
            }
        } catch (IOException e) { e.printStackTrace(); return "FAIL|파일 오류"; }

        return "FAIL|예약 없음";
    }

    public String checkOut(String reservationId, String roomNumber, String actualCheckOutDate) {
        int serviceCost = serviceServer.getTotalServiceCost(reservationId);
        int stayCost = 50000;
        int extraFee = 0;

        String resLine = null;
        try (BufferedReader br = new BufferedReader(new FileReader(RES_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.startsWith(reservationId + "|")) { resLine = line; break; }
            }
        } catch (IOException e) { e.printStackTrace(); return "FAIL|예약 읽기 실패"; }

        if (resLine == null) return "FAIL|예약 없음";

        String[] data = resLine.split("\\|");
        String plannedCheckOut = data[5];

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date planned = sdf.parse(plannedCheckOut);
            Date actual = sdf.parse(actualCheckOutDate);
            if (actual.after(planned)) extraFee = 20000;
        } catch (Exception e) { e.printStackTrace(); return "FAIL|날짜 오류"; }

        int total = stayCost + serviceCost + extraFee;
        boolean roomUpdate = updateStatus(ROOM_FILE, roomNumber, 0, 3, "청소중");
        boolean resUpdate = updateStatus(RES_FILE, reservationId, 0, 6, "체크아웃 완료");

        if (roomUpdate && resUpdate) {
            return "SUCCESS|체크아웃 완료\n숙박료: " + stayCost +
                    "\n부대서비스: " + serviceCost +
                    "\n추가요금: " + extraFee +
                    "\n총 요금: " + total;
        } else {
            return "FAIL|체크아웃 상태 업데이트 실패";
        }
    }

    private boolean updateStatus(String filePath, String targetKey, int keyIndex, int targetIndex, String newValue) {
        List<String> lines = new ArrayList<>();
        boolean found = false;
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length > targetIndex && parts[keyIndex].equals(targetKey)) {
                    parts[targetIndex] = newValue;
                    found = true;
                    line = String.join("|", parts);
                }
                lines.add(line);
            }
        } catch (IOException e) { e.printStackTrace(); return false; }

        if (found) {
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath))) {
                for (String line : lines) { bw.write(line); bw.newLine(); }
            } catch (IOException e) { e.printStackTrace(); return false; }
        }

        return found;
    }
}
