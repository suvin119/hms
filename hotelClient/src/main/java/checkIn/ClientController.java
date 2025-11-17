package checkIn;

/**
 *
 * @author subin
 */
import java.awt.event.ActionEvent;
import java.io.*;
import java.net.Socket;

public class ClientController {
    private CheckInView view;
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private customer currentCustomer; // 현재 조회된 고객 정보 임시 저장

    public ClientController(CheckInView view) {
        this.view = view;
        connectToServer();

        // 리스너 등록
        view.addSearchListener(this::handleSearch);
        view.addCheckInListener(this::handleCheckIn);
    }

    private void connectToServer() {
        try {
            socket = new Socket("localhost", 5000);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException e) {
            view.showMessage("서버 연결 실패! 서버가 켜져있나요?");
        }
    }

    // 1. 검색 버튼 핸들러
    private void handleSearch(ActionEvent e) {
        String id = view.getSearchId();
        if (id.isEmpty()) return;

        out.println("SEARCH|" + id); // 서버로 전송

        try {
            String response = in.readLine(); // 서버 응답 대기
            if (response != null && response.startsWith("FOUND")) {
                // 응답형식: FOUND|1001,김철수,010...,null,null
                String[] data = response.split("\\|")[1].split(",");
                
                // Model 생성 및 View 업데이트
                currentCustomer = new customer(data[0], data[1], data[2]);
                view.setCustomerInfo(currentCustomer.getName(), currentCustomer.getPhone());
            } else {
                view.showMessage("예약 정보를 찾을 수 없습니다.");
            }
        } catch (IOException ex) { ex.printStackTrace(); }
    }

    // 2. 체크인 확정 버튼 핸들러
    private void handleCheckIn(ActionEvent e) {
        if (currentCustomer == null) return;
        
        String room = view.getRoomNum();
        String date = view.getCheckoutDate();

        if(room.isEmpty() || date.isEmpty()) {
            view.showMessage("방 번호와 날짜를 입력하세요.");
            return;
        }

        // 서버로 체크인 데이터 전송
        // 형식: CHECKIN|예약번호|방번호|날짜
        out.println("CHECKIN|" + currentCustomer.getReservationId() + "|" + room + "|" + date);

        try {
            String response = in.readLine();
            if ("SUCCESS".equals(response)) {
                view.showMessage("체크인이 완료되었습니다!");
                // (선택사항) 입력창 초기화 등 후처리
            } else {
                view.showMessage("체크인 처리에 실패했습니다.");
            }
        } catch (IOException ex) { ex.printStackTrace(); }
    }
}
