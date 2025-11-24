package roomAdmin;

/**
 *
 * @author subin
 */
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class RoomAdminController {
    private RoomAdminView view;
    private final String SERVER_IP = "127.0.0.1";
    private final int SERVER_PORT = 9999;


    public RoomAdminController(RoomAdminView view) {
        this.view = view;

        view.setAddButtonListener(new AddAction());
        view.setPriceButtonListener(new PriceAction());
        view.setStatusButtonListener(new StatusAction());
        view.setRefreshButtonListener(new RefreshAction());

        refreshData();
    }

    // 목록 가져오기
    private void refreshData() {
    List<Room> rooms = new ArrayList<>();
    try (Socket socket = new Socket(SERVER_IP, SERVER_PORT);
         PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
         BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

        out.println("ROOM_LIST");
        // 문자열 응답 수신 (예: "101,STD,100,OK/102,DLX,200,OK")
        String response = in.readLine();
        
        if (response != null && !response.equals("EMPTY") && !response.startsWith("ERROR")) {
            String[] roomStrs = response.split("/");
            for (String str : roomStrs) {
                String[] data = str.split("|"); // 콤마로 데이터 분리
                rooms.add(new Room(
                    data[0], 
                    Room.Type.valueOf(data[1]), 
                    Double.parseDouble(data[2]), 
                    Room.Status.valueOf(data[3]) 
                ));
            }
        }
    } catch (Exception e) {
        System.out.println("통신 오류: " + e.getMessage());
    }
    view.updateTable(rooms);
}


    // 1. 등록 버튼
    class AddAction implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
        try (Socket socket = new Socket(SERVER_IP, SERVER_PORT);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
            
            // 데이터 준비
            String num = view.getRoomNum();
            String type = view.getSelectedType().name(); // Enum -> String
            String price = view.getPriceStr();
            String status = view.getSelectedStatus().name(); // Enum -> String
            
            // 문자열 조립해서 전송 (ROOM_ADD|101|STD|10000|OK)
            String msg = String.format("ROOM_ADD|%s|%s|%s|%s", num, type, price, status);
            out.println(msg);

            // 응답 확인
            String response = in.readLine(); // "TRUE|..."
            if (response.startsWith("TRUE")) {
                view.showMessage("등록 성공!");
                refreshData();
            } else {
                view.showMessage("등록 실패");
            }
        } catch (Exception ex) { ex.printStackTrace(); }
        }
    }

    // 2. 가격 수정 버튼
    class PriceAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try (Socket socket = new Socket(SERVER_IP, SERVER_PORT);
                 ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
                 ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {

                out.writeObject("UPDATE_PRICE");
                out.writeObject(view.getRoomNum());
                out.writeObject(Double.parseDouble(view.getPriceStr()));
                out.flush();

                if ((boolean) in.readObject()) {
                    view.showMessage("수정 완료");
                    refreshData();
                } else {
                    view.showMessage("수정 실패 (객실 없음)");
                }
            } catch (Exception ex) {
                view.showMessage("통신 오류 또는 입력 오류");
            }
        }
    }

    // 3. 상태 수정 버튼
    class StatusAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try (Socket socket = new Socket(SERVER_IP, SERVER_PORT);
                 ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
                 ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {

                out.writeObject("UPDATE_STATUS");
                out.writeObject(view.getRoomNum());
                out.writeObject(view.getSelectedStatus());
                out.flush();

                if ((boolean) in.readObject()) {
                    view.showMessage("상태 변경 완료");
                    refreshData();
                } else {
                    view.showMessage("변경 실패");
                }
            } catch (Exception ex) {
                view.showMessage("통신 오류");
            }
        }
    }

    // 4. 새로고침 버튼
    class RefreshAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            refreshData();
        }
    }
}