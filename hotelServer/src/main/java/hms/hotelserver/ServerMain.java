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
        //서버 소켓 생성
        try (ServerSocket serverSocket = new ServerSocket(9999)) {
            System.out.println("[Server] 호텔 관리 서버가 시작되었습니다 (포트: 9999)");

            while (true) {
                Socket userSocket = serverSocket.accept();
                System.out.println("[Server] 사용자 접속함: " + userSocket.getInetAddress());
                //스레드 생성, 시작
                new Thread(() -> handleUser(userSocket)).start(); 
            }
        } catch (IOException e) {
            System.out.println("[Server] 서버 시작 실패");
            e.printStackTrace();
        }
    }

    // 사용자 요청 처리 핸들러
    private static void handleUser(Socket socket) {
        try (
            // 입력 스트림
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            // 출력 스트림
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true)
        ) {
            String request = in.readLine(); 
            System.out.println("[Server] 수신된 요청: " + request);

            // 서비스 로직 연결
            CheckInService service = new CheckInService();
            RoomAdminService roomService = new RoomAdminService();
            String response = "ERROR|잘못된 요청";

            if (request != null) {
                String[] parts = request.split("\\|");
                String command = parts[0];

                if (command.equals("FIND_RESERVATION")) {
                    response = service.findReservation(parts[1]);
                }
                else if (command.equals("CONFIRM_CHECKIN")) {
                    response = service.confirmCheckIn(parts[1], parts[2]);
                }
                else if (command.startsWith("ROOM_")) {
                    response = roomService.processRequest(command, parts);
                }
            }

            out.println(response); 
            System.out.println("[Server] 응답 전송: " + response);

        } catch (IOException e) {
            System.out.println("[Server] 클라이언트 연결 끊김");
        }
    }
}