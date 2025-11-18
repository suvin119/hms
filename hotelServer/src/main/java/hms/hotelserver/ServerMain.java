package hms.hotelserver;

/**
 *
 * @author subin
 */

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerMain {
    public static void main(String[] args) {
        
        // ============================================================
        // [1] 기초 데이터 파일 자동 생성 로직 (오류 해결용)
        // ============================================================
        initDataFiles(); 
        // ============================================================

        try (ServerSocket serverSocket = new ServerSocket(9999)) {
            System.out.println("[Server] 호텔 관리 서버가 시작되었습니다 (포트: 9999)");

            while (true) {
                Socket clientSocket = serverSocket.accept();
                new Thread(() -> handleClient(clientSocket)).start(); 
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 파일이 없으면 자동으로 만들어주는 메소드
    private static void initDataFiles() {
        try {
            // 1. 'data' 폴더 생성
            File folder = new File("data");
            if (!folder.exists()) {
                folder.mkdirs(); 
                System.out.println(">> [초기화] 'data' 폴더 생성됨");
            }

            // 2. 'reservations.txt' 파일 생성 (예약 더미 데이터)
            File resFile = new File("data/reservations.txt");
            if (!resFile.exists()) {
                try (BufferedWriter writer = new BufferedWriter(new FileWriter(resFile))) {
                    // 예약번호|이름|전화번호|체크인|체크아웃|객실타입|상태
                    writer.write("R1001|홍길동|010-1234-5678|2025-11-20|2025-11-22|Standard|예약중\n");
                    writer.write("R1002|이순신|010-9876-5432|2025-12-01|2025-12-03|Deluxe|예약중\n");
                }
                System.out.println(">> [초기화] 'reservations.txt' 파일 및 더미 데이터 생성됨");
            }
            
            // 3. 'rooms.txt' 파일 생성 (객실 더미 데이터)
            File roomFile = new File("data/rooms.txt");
            if (!roomFile.exists()) {
                try (BufferedWriter writer = new BufferedWriter(new FileWriter(roomFile))) {
                    // 객실번호|타입|가격|상태
                    writer.write("101|Standard|80000|빈객실\n");
                    writer.write("102|Standard|80000|빈객실\n");
                    writer.write("201|Deluxe|120000|빈객실\n");
                }
                System.out.println(">> [초기화] 'rooms.txt' 파일 생성됨");
            }

        } catch (IOException e) {
            System.out.println(">> [에러] 파일 생성 중 문제가 발생했습니다.");
            e.printStackTrace();
        }
    }

    // 클라이언트 요청 처리 핸들러
    private static void handleClient(Socket socket) {
        try (
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true)
        ) {
            String request = in.readLine(); 
            System.out.println("[Server] 수신된 요청: " + request);

            // 서비스 로직 연결
            CheckInService service = new CheckInService(); 
            String response = "ERROR|잘못된 요청";

            if (request != null) {
                String[] parts = request.split("\\|");
                String command = parts[0];

                if (command.equals("FIND_RESERVATION")) {
                    response = service.findReservation(parts[1]);
                } else if (command.equals("CONFIRM_CHECKIN")) {
                    response = service.confirmCheckIn(parts[1], parts[2]);
                }
            }

            out.println(response); 
            System.out.println("[Server] 응답 전송: " + response);

        } catch (IOException e) {
            System.out.println("[Server] 클라이언트 연결 끊김");
        }
    }
}