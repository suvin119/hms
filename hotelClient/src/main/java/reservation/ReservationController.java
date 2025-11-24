package reservation;

/**
 *
 * @author subin
 */

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;

public class ReservationController {
    private ReservationView view;
    private final String SERVER_IP = "127.0.0.1"; 
    private final int SERVER_PORT = 9999;
    private Runnable onSuccessCallback;

    public ReservationController() {
        this.view = new ReservationView();
        initListeners();
    }

    public ReservationView getView() { return view; }
    public void setOnSuccess(Runnable callback) { this.onSuccessCallback = callback; }

    private void initListeners() {
        
        // 예약 확정
        view.addRegisterListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // View에서 데이터 수집
                String name = view.getName();
                String phone = view.getPhone();
                String inDate = view.getInDate();
                String outDate = view.getOutDate();
                String type = view.getRoomType();
                String count = view.getGuestCount();

                if (name.isEmpty() || phone.isEmpty() || inDate.isEmpty()) {
                    view.showMessage("필수 정보를 입력하세요.");
                    return;
                }

                // 3. 서버에 요청 (프로토콜: REGISTER_RESERVATION|데이터1|데이터2|...)
                String request = String.format("REGISTER_RESERVATION|%s|%s|%s|%s|%s|%s",
                                                name, phone, inDate, outDate, type, count);
                
                String response = sendRequest(request);
                view.showMessage(response);

                // 4. 성공 시 메인으로 이동
                if (response.startsWith("SUCCESS") && onSuccessCallback != null) {
                    onSuccessCallback.run(); 
                }
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