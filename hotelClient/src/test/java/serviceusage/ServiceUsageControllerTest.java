package serviceusage;

import java.io.*;
import java.net.Socket;

public class ServiceUsageControllerTest {

    private static final String SERVER_IP = "127.0.0.1";
    private static final int SERVER_PORT = 9999;

    public static void main(String[] args) {

        System.out.println("\n[Test 1] SERVICE_ADD_USAGE)");
        String addCommand = "SERVICE_ADD_USAGE|201|1|2"; 
        sendAndPrint(addCommand);

        System.out.println("\n[Test 2] SERVICE_LIST_BY_ROOM");
        String listCommand = "SERVICE_LIST_BY_ROOM|201";
        sendAndPrint(listCommand);
        
    }

    public static void sendAndPrint(String command) {
        System.out.println(">> [보냄] : " + command);

        try (Socket socket = new Socket(SERVER_IP, SERVER_PORT);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            out.println(command);

            String response = in.readLine();
            System.out.println("<< [받음] : " + response);

            if (response != null && !response.startsWith("ERROR")) {
                System.out.println("   └─ 결과: 성공");
            } else {
                System.out.println("   └─ 결과: 에러");
            }

        } catch (IOException e) {
            System.out.println("<< [오류] : 서버 연결 불가 (" + e.getMessage() + ")");
        }
    }
}