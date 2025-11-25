package hms.hotelserver;

/**
 * @author subin
 */
import java.io.*;

public class ReservationService {
    private static final String RES_FILE = "src/main/resources/reservations.txt";

    //1. 예약 등록
    public String registerReservation(String name, String phone, String inDate, String outDate, String type, String count) {
        
        String newId = generateNextId();
        
        String line = String.format("%s|%s|%s|%s|%s|%s|예약|NULL|%s", newId, name, phone, inDate, outDate, count, type);

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(RES_FILE, true))) {
            bw.write(line);
            bw.newLine();
            return "SUCCESS|예약이 확정되었습니다. (예약번호: " + newId + ")";
        } catch (IOException e) {
            e.printStackTrace();
            return "ERROR|서버 저장 중 오류 발생";
        }
    }

    private String generateNextId() {
        File file = new File(RES_FILE);
        String lastId = "R1000"; // 파일이 없으면 1001번부터

        if (file.exists()) {
            try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = br.readLine()) != null) {
                    if (!line.trim().isEmpty()) {
                        String[] data = line.split("\\|");
                        if (data.length > 0) {
                            lastId = data[0];
                        }
                    }
                }
            } catch (IOException e) {
            }
        }

        try {
            int num = Integer.parseInt(lastId.substring(1)); // R 제외
            return "R" + (num + 1);
        } catch (NumberFormatException e) {
            return "R1001"; // 파싱 실패 시 초기값
        }
    }
}