package UnitServices;

import java.io.*;
import java.net.Socket;

public class ServiceDAO {
    private final String SERVER_IP = "127.0.0.1";
    private final int SERVER_PORT = 9999;

    public int getTotalServiceCost(int reservationId) {
        String msg = "FIND_SERVICE|" + reservationId;
        try (Socket socket = new Socket(SERVER_IP, SERVER_PORT);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            out.println(msg);
            String response = in.readLine();
            return Integer.parseInt(response.split("\\|")[1]);

        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
}
