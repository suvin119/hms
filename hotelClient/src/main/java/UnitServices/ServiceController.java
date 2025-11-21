package UnitServices;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;

public class ServiceController {
    private ServiceView view;
    private final String SERVER_IP = "127.0.0.1";
    private final int SERVER_PORT = 9999;

    public ServiceController(ServiceView view) {
        this.view = view;
        initController();
    }

    private void initController() {
        view.btnAddService.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addService();
            }
        });
    }

    private void addService() {
        try {
            String reservationId = view.tfReservationId.getText();
            String serviceId = view.tfServiceId.getText();
            String quantity = view.tfQuantity.getText();
            int cost = Integer.parseInt(quantity) * 10000;

            String msg = "ADD_SERVICE|" + reservationId + "|" + serviceId + "|" + quantity + "|" + cost;
            String response = sendRequest(msg);

            view.taInfo.append(response + "\n");
        } catch (NumberFormatException ex) {
            view.taInfo.append("숫자를 정확히 입력하세요.\n");
        }
    }

    private String sendRequest(String msg) {
        try (Socket socket = new Socket(SERVER_IP, SERVER_PORT);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            out.println(msg);
            return in.readLine();

        } catch (IOException e) {
            e.printStackTrace();
            return "FAIL|서버 연결 실패";
        }
    }
}
