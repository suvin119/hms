/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package serviceusage;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;

public class ServiceUsageController {

    private ServiceUsageView view;
    private final String SERVER_IP = "127.0.0.1";
    private final int SERVER_PORT = 9999;
    
    private Runnable onBack;

    public ServiceUsageController() {
        this.view = new ServiceUsageView();
        initListeners();
    }

    public ServiceUsageView getView() { return view; }
    
    public void setOnBack(Runnable action) {
        this.onBack = action;
    }

    private void initListeners() {

        // ▷ 1. 부대 서비스 추가 버튼
        view.addAddUsageListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String room = view.getRoomNumber();
                String menuId = view.getMenuId();
                String qtyStr = view.getQuantity();

                if (room.isEmpty() || menuId.isEmpty() || qtyStr.isEmpty()) {
                    view.showMessage("모든 입력값을 입력해주세요.");
                    return;
                }

                try {
                    int qty = Integer.parseInt(qtyStr);

                    String msg = "SERVICE_ADD_USAGE|" + room + "|" + menuId + "|" + qty;
                    String response = sendRequest(msg);

                    view.showMessage(response);

                } catch (NumberFormatException ex) {
                    view.showMessage("수량은 숫자만 입력 가능합니다.");
                }
            }
        });

        // ▷ 2. 객실별 사용 내역 조회 버튼
        view.addSearchUsageListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String room = view.getRoomNumber();

                if (room.isEmpty()) {
                    view.showMessage("객실 번호를 입력하세요.");
                    return;
                }

                String msg = "SERVICE_LIST_BY_ROOM|" + room;
                String response = sendRequest(msg);

                if (response.startsWith("OK|EMPTY")) {
                    view.showMessage("사용 기록이 없습니다.");
                    return;
                }

                if (!response.startsWith("OK|")) {
                    view.showMessage("에러: " + response);
                    return;
                }

                String data = response.substring(3);
                String[] rows = data.split("#");

                view.clearUsageTable();

                for (String row : rows) {
                    if (row.isBlank()) continue;

                    String[] p = row.split("\\|");
                    if (p.length < 3) continue;

                    String menuName = p[1];
                    String amount = p[2];

                    view.addUsageRow(menuName, amount);
                }
            }
        });
        
        view.addBackListener(e -> {
            if (onBack != null) onBack.run();
        });
    }

    // 서버 통신 메소드
    private String sendRequest(String msg) {
        try (Socket socket = new Socket(SERVER_IP, SERVER_PORT);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            out.println(msg);
            return in.readLine();

        } catch (IOException ex) {
            return "ERROR|서버 연결 실패";
        }
    }
}