package checkIn;

/**
 * CheckInController 클래스
 * 역할: MVC 패턴의 제어자
 * 1. 사용자의 입력(View에서의 버튼 클릭)을 감지
 * 2. 입력된 데이터를 가지고 서버(Server)와 통신하여 로직을 처리
 * 3. 서버로부터 받은 결과로 Model 생성하여 다시 View에 화면 갱신을 요청
 * * @author subin
 */

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;

public class CheckInController {
    private CheckInView view;
    private final String SERVER_IP = "127.0.0.1";
    private final int SERVER_PORT = 9999;
    
    private Runnable onSuccessCallback;

    public CheckInController() {
        this.view = new CheckInView();
        initListeners(); // 버튼 초기화 동작
    }
    
    public void setOnSuccess(Runnable callback) {
        this.onSuccessCallback = callback;
    }
    
    public CheckInView getView() { return view; }

    private void initListeners() {
        
        // 1. 예약 조회 버튼
        view.addSearchListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String id = view.getReservationId();
                if (id.isEmpty()) {
                    view.showMessage("예약번호를 입력하세요.");
                    return;
                }
                
                String response = sendRequest("FIND_RESERVATION|" + id); // 서버에 요청
                
                if (response.startsWith("SUCCESS")) { // 응답 처리
                    // SUCCESS|예약번호|고객 이름|연락처|체크인날짜|체크아웃날짜|인원 수|예약상태|객실번호
                    String[] p = response.split("\\|");
                    
                    Reservation res = new Reservation(p[1], p[2], p[3], p[4], p[5], p[6], p[7], p[8]);

                    view.setReservationInfo(res);
                } else {
                    view.showMessage("오류: " + response);
                }
            }
        });

        // 2. 체크인 버튼
        view.addCheckInListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String roomNum = view.getRoomNumber();
                String resId = view.getReservationId();
                String newCheckOutDate = view.getCheckOutDate();
                
                if (roomNum.isEmpty() || newCheckOutDate.isEmpty()) {
                    view.showMessage("객실 번호와 체크아웃 날짜를 확인하세요.");
                    return;
                }
                
                // 서버에 체크인 확정 요청
                String msg = String.format("CONFIRM_CHECKIN|%s|%s|%s", resId, roomNum, newCheckOutDate);
                
                String response = sendRequest(msg);
                view.showMessage(response);
                
                if (response.startsWith("SUCCESS") && onSuccessCallback != null) {
                    onSuccessCallback.run();
                }
            }
        });
        
    }

    // 서버 통신 메소드
    //private
    String sendRequest(String msg) {
        try (Socket socket = new Socket(SERVER_IP, SERVER_PORT);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true); // 전송용 스트림
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) { // 수신용 스트림
            
            out.println(msg); // 서버로 메세지 전송
            return in.readLine(); // 서버로부터 응답 대기
            
        } catch (IOException ex) {
            return "ERROR|서버 연결 실패";
        }
    }
}
