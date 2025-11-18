/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package checkIn;

/**
 *
 * @author subin
 */

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;

public class CheckInController {
    private CheckInView view;
    private final String SERVER_IP = "127.0.0.1";
    private final int SERVER_PORT = 9999;

    public CheckInController() {
        this.view = new CheckInView();
        initListeners();
    }

    private void initListeners() {
        // '조회' 버튼 동작
        view.addSearchListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String id = view.getReservationId();
                if (id.isEmpty()) {
                    view.showMessage("예약번호를 입력하세요.");
                    return;
                }
                
                // 서버에 요청
                String response = sendRequest("FIND_RESERVATION|" + id);
                
                // 응답 처리
                if (response.startsWith("SUCCESS")) {
                    // 파싱: SUCCESS|R1001|홍길동|번호|체크인|체크아웃|타입|상태
                    String[] p = response.split("\\|");
                    
                    // Model 객체 생성 (여기가 MVC의 핵심!)
                    Reservation res = new Reservation(p[1], p[2], p[3], p[4], p[5], p[6], p[7]);
                    
                    // View 업데이트
                    view.setReservationInfo(res);
                } else {
                    view.showMessage("오류: " + response);
                }
            }
        });

        // '체크인' 버튼 동작
        view.addCheckInListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String roomNum = view.getRoomNumber();
                String resId = view.getReservationId();
                
                if (roomNum.isEmpty()) {
                    view.showMessage("객실 번호를 입력하세요.");
                    return;
                }
                
                String response = sendRequest("CONFIRM_CHECKIN|" + resId + "|" + roomNum);
                view.showMessage(response);
            }
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
