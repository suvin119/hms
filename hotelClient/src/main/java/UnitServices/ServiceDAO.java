package UnitServices;

import java.io.*;
import java.net.Socket;

public class ServiceDAO {
    private final String SERVER_IP = "127.0.0.1";
    private final int SERVER_PORT = 9999;

    // -------------------------------------------------
    // reservationId 기준: 부대 서비스 총액 조회
    // -------------------------------------------------
    public int getTotalServiceCost(int reservationId) {
        String msg = "FIND_SERVICE|" + reservationId;

        try (Socket socket = new Socket(SERVER_IP, SERVER_PORT);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            out.println(msg);
            String response = in.readLine();
            if (response == null || response.isEmpty()) return 0;

            // 서버 응답 예: "FIND_SERVICE|총액"
            String[] parts = response.split("\\|");
            return Integer.parseInt(parts[1].trim());

        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    // -------------------------------------------------
    // roomNum 기준: 부대 서비스 총액 조회 (CheckOutController용)
    // -------------------------------------------------
    public int getServiceTotalByRoom(String roomNum) {
        // 실제 매핑 필요: roomNum → reservationId
        int reservationId = Integer.parseInt(roomNum); // 임시 예제
        return getTotalServiceCost(reservationId);
    }

    // -------------------------------------------------
    // 체크아웃 시 부대 서비스 초기화
    // -------------------------------------------------
    public void clearServiceByRoom(String roomNum) {
        try {
            int reservationId = Integer.parseInt(roomNum); // 임시 예제
            String msg = "CLEAR_SERVICE|" + reservationId;

            try (Socket socket = new Socket(SERVER_IP, SERVER_PORT);
                 PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {
                out.println(msg);
                // 서버에서 확인 응답 필요 시 읽기 가능
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
