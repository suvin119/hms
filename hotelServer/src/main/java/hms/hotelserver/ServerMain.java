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
            CheckInService checkInService = new CheckInService();
            ReservationService reservationService = new ReservationService();
            CheckOutService checkOutService = new CheckOutService();
            RoomAdminService roomService = new RoomAdminService();
            CustomersService customerService = new CustomersService();
            ServiceUsageService usageService = new ServiceUsageService();
            MenuAdminService menuService = new MenuAdminService();
            StaffService staffService = new StaffService();
            
            String response = "ERROR|잘못된 요청";

            if (request != null) {
                String[] parts = request.split("\\|");
                String command = parts[0];
                
                if (command.equals("LOGIN")) {
                    if (parts.length >= 3) { response = staffService.login(parts[1], parts[2]); }
                    else { response = "ERROR|INVALID_FORMAT"; }
                 }
                else if (command.equals("STAFF_ADD")) {
                    if (parts.length >= 4) {response = staffService.addStaff(parts[1], parts[2], parts[3]); }
                    else { response = "ERROR|INVALID_FORMAT"; }
                }
                else if (command.equals("STAFF_DELETE")) {
                    if (parts.length >= 2) { response = staffService.deleteStaff(parts[1]); }
                    else { response = "ERROR|INVALID_FORMAT"; }
                }
                else if (command.equals("FIND_RESERVATION")) {
                    response = checkInService.findReservation(parts[1]);
                }
                else if (command.equals("CONFIRM_CHECKIN")) {
                    response = checkInService.confirmCheckIn(parts[1], parts[2], parts[3]);
                }
                else if (command.equals("REGISTER_RESERVATION")) {
                    response = reservationService.registerReservation( parts[1], parts[2], parts[3], parts[4], parts[5], parts[6]);
                }
                else if (command.startsWith("ROOM_")) {
                    response = roomService.processRequest(command, parts);
                }
                else if (command.equals("CHECKOUT")) {
                    response = checkOutService.checkoutRoom(Integer.parseInt(parts[1]));
}
                else if (command.equals("ROOMS_LOAD")){
                    response = checkOutService.loadRoom(Integer.parseInt(parts[1]));
                }
                else if (command.equals("ROOMS_SERVICE_USAGE")){
                    response = checkOutService.loadRoomServices(Integer.parseInt(parts[1]));
                }
                else if(command.equals("SYNC_NEW_CUSTOMERS")){
                    response = customerService.syncNewCustomers();
                }
                else if(command.equals("UPDATE_GRADE")){
                    response = customerService.updateCustomerGrade(parts[1], parts[2], parts[3]);
                }
                else if(command.equals("CUSTOMERS_LIST")){
                    response = customerService.getAllCustomers();
                }
                else if (command.startsWith("SERVICE_")) {
                    response = usageService.process(command, parts);
                }
                else if (request.startsWith("MENU_")) {
                    response = menuService.processRequest(request);
                }
                
            }
            
            out.println(response); 
            System.out.println("[Server] 응답 전송: " + response);

        } catch (IOException e) {
            System.out.println("[Server] 클라이언트 연결 끊김");
        }
    }
}