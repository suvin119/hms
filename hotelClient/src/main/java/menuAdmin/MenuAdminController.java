package menuAdmin;

/**
 *
 * @author subin
 */

import java.io.*;
import java.net.Socket;

public class MenuAdminController {
    private MenuAdminView view;
    private static final String SERVER_IP = "127.0.0.1";
    private static final int SERVER_PORT = 9999;
    private Runnable onSuccessCallback;

    public MenuAdminController() {
        this.view = new MenuAdminView();
        initListeners();
        refreshData();
    }
    
    public MenuAdminView getView() { return view; }
    public void setOnSuccess(Runnable callback) { this.onSuccessCallback = callback; }

    private void initListeners() {
        // 1. 메뉴 등록 버튼
        view.addAddListener(e -> {
            String name = view.getMenuName();
            String price = view.getPrice();

            if (name.isEmpty() || price.isEmpty()) {
                view.showMessage("이름과 가격을 입력하세요.");
                return;
            }

            String nextId = generateNextId();

            // 프로토콜 : MENU_ADD|ID|이름|가격
            String request = "MENU_ADD|" + nextId + "|" + name + "|" + price;
            String response = sendRequest(request);

            if (response.startsWith("SUCCESS")) {
                view.showMessage("메뉴가 등록되었습니다. (ID: " + nextId + ")");
                view.clearInputFields();
                refreshData();
            } else {
                view.showMessage("등록 실패: " + response);
            }
        });

        // 2. 정보 수정 버튼
        view.addUpdateListener(e -> {
            String id = view.getSelectedMenuId();
            String name = view.getMenuName();
            String price = view.getPrice();

            if (id == null) {
                view.showMessage("수정할 메뉴를 목록에서 선택해주세요.");
                return;
            }
            
            if(name.isEmpty() || price.isEmpty()) {
                 view.showMessage("수정할 이름과 가격을 입력하세요.");
                 return;
            }

            String request = "MENU_UPDATE|" + id + "|" + name + "|" + price;
            if (sendRequest(request).startsWith("SUCCESS")) {
                view.showMessage("수정 완료");
                view.clearInputFields();
                refreshData();
            } else {
                view.showMessage("수정 실패");
            }
        });

        view.addDeleteListener(e -> {
            String id = view.getSelectedMenuId();
            if (id == null) {
                view.showMessage("삭제할 메뉴를 목록에서 선택해주세요.");
                return;
            }

            if (sendRequest("MENU_DELETE|" + id).startsWith("SUCCESS")) {
                view.showMessage("삭제 완료");
                refreshData();
            } else {
                view.showMessage("삭제 실패");
            }
        });

        view.addRefreshListener(e -> refreshData());
    }

    private String generateNextId() {
        String lastIdStr = view.getLastIdFromTable();
        
        if (lastIdStr == null || lastIdStr.isEmpty()) {
            return "1";
        }

        try {
            int lastId = Integer.parseInt(lastIdStr);
            return String.valueOf(lastId + 1);
        } catch (NumberFormatException e) {
            return "1"; 
        }
    }

    private void refreshData() {
        view.clearTable();
        String response = sendRequest("MENU_LIST");

        if (response == null || response.startsWith("FAIL")) return;

        String[] rows = response.split("/");
        for (String row : rows) {
            if (row.trim().isEmpty()) continue;
            String[] data = row.split("\\|");
            if (data.length >= 3) {
                view.addRow(new Object[]{data[0], data[1], data[2]});
            }
        }
    }

    private String sendRequest(String msg) {
        try (Socket socket = new Socket(SERVER_IP, SERVER_PORT);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {
            out.println(msg);
            return in.readLine();
        } catch (IOException e) {
            return "FAIL|통신 오류";
        }
    }
}
