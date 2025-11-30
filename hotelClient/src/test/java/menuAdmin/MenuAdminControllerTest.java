package menuAdmin;

import java.io.*;
import java.net.Socket;

public class MenuAdminControllerTest {
    private static final String SERVER_IP = "127.0.0.1";
    private static final int SERVER_PORT = 9999;

    public static void main(String[] args) {
        System.out.println("============== [식음료 컨트롤러 테스트] ==============");

        System.out.println("1. MENU_LIST");
        String listResponse = sendRequest("MENU_LIST");
        System.out.println("   결과: " + listResponse);

        System.out.println("\n2. MENU_ADD");
        String addCommand = "MENU_ADD|999|테스트커피|3000"; 
        System.out.println("   >> 보냄: " + addCommand);
        String addResponse = sendRequest(addCommand);
        System.out.println("   결과: " + addResponse);

        System.out.println("\n3. MENU_UPDATE");
        String updateCommand = "MENU_UPDATE|999|테스트커피|4500";
        System.out.println("   >> 보냄: " + updateCommand);
        String updateResponse = sendRequest(updateCommand);
        System.out.println("   결과: " + updateResponse);

        System.out.println("\n4. MENU_DELETE");
        String deleteCommand = "MENU_DELETE|999";
        System.out.println("   >> 보냄: " + deleteCommand);
        String deleteResponse = sendRequest(deleteCommand);
        System.out.println("   결과: " + deleteResponse);

        System.out.println("\n========================================================");
    }

    private static String sendRequest(String msg) {
        try (Socket socket = new Socket(SERVER_IP, SERVER_PORT);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

            out.println(msg);
            return in.readLine();

        } catch (IOException e) {
            return "ERROR|통신 실패 (" + e.getMessage() + ")";
        }
    }
}