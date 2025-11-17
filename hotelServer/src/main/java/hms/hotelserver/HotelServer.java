package hms.hotelserver;

/**
 *
 * @author subin
 */
import java.io.*;
import java.net.*;
import java.util.*;

public class HotelServer {
    private static final int PORT = 5000;
    private static final String DB_FILE = "reservations.txt";

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("[서버] 호텔 예약 서버가 시작되었습니다. (Port: " + PORT + ")");

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("[서버] 클라이언트 접속: " + clientSocket.getInetAddress());
                new Thread(() -> handleClient(clientSocket)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void handleClient(Socket socket) {
        try (
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true)
        ) {
            String request;
            while ((request = in.readLine()) != null) {
                // 프로토콜 분석 (명령어|데이터)
                String[] parts = request.split("\\|");
                String command = parts[0];

                if ("SEARCH".equals(command)) {
                    // 요청: SEARCH|예약번호
                    String result = findReservation(parts[1]);
                    out.println(result); // 응답: FOUND|데이터... 또는 NOT_FOUND
                } else if ("CHECKIN".equals(command)) {
                    // 요청: CHECKIN|예약번호|방번호|체크아웃날짜
                    boolean success = updateCheckIn(parts[1], parts[2], parts[3]);
                    out.println(success ? "SUCCESS" : "FAIL");
                }
            }
        } catch (IOException e) {
            System.out.println("[서버] 클라이언트 연결 종료");
        }
    }

    // 1. 예약 정보 검색 (txt 파일 읽기)
    private synchronized static String findReservation(String resId) {
        try (BufferedReader br = new BufferedReader(new FileReader(DB_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data[0].equals(resId)) {
                    return "FOUND|" + line; // 전체 데이터 반환
                }
            }
        } catch (IOException e) { e.printStackTrace(); }
        return "NOT_FOUND";
    }

    // 2. 체크인 정보 업데이트 (txt 파일 수정)
    private synchronized static boolean updateCheckIn(String resId, String roomNum, String date) {
        List<String> lines = new ArrayList<>();
        boolean found = false;

        // 파일 읽어서 메모리에 저장 및 수정
        try (BufferedReader br = new BufferedReader(new FileReader(DB_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data[0].equals(resId)) {
                    // 기존 데이터 + 방번호 + 날짜로 덮어쓰기
                    lines.add(data[0] + "," + data[1] + "," + data[2] + "," + roomNum + "," + date);
                    found = true;
                } else {
                    lines.add(line);
                }
            }
        } catch (IOException e) { return false; }

        if (!found) return false;

        // 파일 다시 쓰기
        try (PrintWriter pw = new PrintWriter(new FileWriter(DB_FILE))) {
            for (String l : lines) pw.println(l);
            return true;
        } catch (IOException e) { return false; }
    }
}