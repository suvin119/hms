package roomAdmin;

/**
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

    public RoomAdminController() {
        this.view = new RoomAdminView();
        initListeners();
        refreshData();
    }

    public RoomAdminView getView() {
        return view;
    }

    private void refreshData() {
        List<Room> rooms = new ArrayList<>();

        String response = sendRequest("ROOM_LIST");

        if (response != null && !response.equals("EMPTY") && !response.startsWith("ERROR")) {
            String[] roomStrs = response.split("/"); 
            for (String str : roomStrs) {
                if (str.trim().isEmpty()) continue;
                try {
                    String[] data = str.split("\\|");
                    rooms.add(new Room(
                        data[0],
                        Room.Type.fromKor(data[1]),
                        Double.parseDouble(data[2]),
                        Room.Status.fromKor(data[3])
                    ));
                } catch (Exception e) {
                    System.out.println("데이터 파싱 오류: " + str);
                }
            }
        } else if (response != null && response.startsWith("ERROR")) {
            System.out.println(response);
        }

        view.setRoomInfo(rooms);
    }

    
    private void initListeners() {
        
        // 1. 객실 등록
        view.addAddButtonListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String num = view.getRoomNum();
                    String type = view.getSelectedType().getLabel();
                    String price = view.getPriceStr();
                    String status = view.getSelectedStatus().getLabel();

                    String msg = String.format("ROOM_ADD|%s|%s|%s|%s", num, type, price, status);

                    String response = sendRequest(msg);

                    if (response != null && response.startsWith("TRUE")) {
                        view.showMessage("등록 성공");
                        refreshData();
                    } else {
                        view.showMessage("등록 실패: " + response);
                    }
                } catch (Exception ex) {
                    view.showMessage("입력 오류: " + ex.getMessage());
                }
            }
        });

        // 2. 요금 수정
        view.addPriceButtonListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String msg = "ROOM_UPDATE_PRICE|" + view.getRoomNum() + "|" + view.getPriceStr();

                    String response = sendRequest(msg);

                    if (response != null && response.startsWith("TRUE")) {
                        view.showMessage("요금 수정 완료");
                        refreshData();
                    } else {
                        view.showMessage("수정 실패: " + response);
                    }
                } catch (Exception ex) {
                    view.showMessage("입력 오류: " + ex.getMessage());
                }
            }
        });

        // 3. 상태 수정
        view.addStatusButtonListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String statusKorean = view.getSelectedStatus().getLabel();
                    String msg = "ROOM_UPDATE_STATUS|" + view.getRoomNum() + "|" + statusKorean;
   
                    String response = sendRequest(msg);

                    if (response != null && response.startsWith("TRUE")) {
                        view.showMessage("상태 변경 완료");
                        refreshData();
                    } else {
                        view.showMessage("변경 실패: " + response);
                    }
                } catch (Exception ex) {
                    view.showMessage("오류 발생: " + ex.getMessage());
                }
            }
        });

        // 4. 새로고침
        view.addRefreshButtonListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                refreshData();
            }
        });
    }
    
    private String sendRequest(String msg) {
        try (Socket socket = new Socket(SERVER_IP, SERVER_PORT);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true); // 전송용 스트림
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) { // 수신용 스트림
            
            out.println(msg); // 서버로 메세지 전송
            return in.readLine(); // 서버로부터 응답 대기
            
        } catch (IOException ex) {
            System.out.println("통신 에러 발생: " + ex.getMessage());
            return "ERROR|서버 연결 실패";
        }
    }
}