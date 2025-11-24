package checkOut;

import UnitServices.ServiceDAO;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class CheckOutController {

    private CheckOutView view;
    private List<Room> rooms;
    private ServiceDAO serviceDAO;
    private CheckOutInfo currentInfo; // 체크아웃 정보를 담을 객체

    public CheckOutController(CheckOutView view) {
        this.view = view;
        this.rooms = Room.loadRooms(); // Room 클래스에서 파일 읽어오기
        this.serviceDAO = new ServiceDAO(); // 서비스 데이터 관리 객체

        // 뷰의 버튼들과 리스너 연결
        view.setSearchListener(new SearchListener());
        view.setCalculateListener(new CalculateListener());
        view.setCheckOutListener(new CheckOutListener());
        view.setBackListener(new BackListener());
    }

    // ----------------------------
    // 1. 객실 조회 리스너
    // ----------------------------
    class SearchListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String roomNum = view.getRoomNumber();
            if (roomNum.isEmpty()) {
                view.showMessage("객실 번호를 입력하세요.");
                return;
            }

            Room room = findRoom(roomNum);
            if (room == null) {
                view.showMessage("존재하지 않는 객실입니다.");
                return;
            }

            // 부대 서비스 총액 조회 (ServiceDAO 이용)
            int serviceTotal = 0;
            if (serviceDAO != null) {
                serviceTotal = serviceDAO.getServiceTotalByRoom(roomNum);
            }

            // 체크아웃 정보 객체 생성 (데이터가 없다면 가짜 데이터라도 넣어서 에러 방지)
            currentInfo = new CheckOutInfo(
                    roomNum,
                    "고객", // 나중에 고객 이름 연동 필요
                    room.getRoomNumber(),
                    "2025-11-20", // 체크인 날짜 (임시)
                    "2025-11-24", // 체크아웃 날짜 (임시)
                    serviceTotal,
                    0 // 할인 금액
            );

            view.setInfoText(currentInfo.formatInfo());
        }
    }

    // ----------------------------
    // 2. 요금 계산 리스너
    // ----------------------------
    class CalculateListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (currentInfo == null) {
                view.showMessage("먼저 객실 조회를 해주세요.");
                return;
            }

            int total = currentInfo.getTotalFee();
            view.setInfoText(currentInfo.formatInfo() +
                    "\n----------------\n총 결제 금액 = " + total + "원");
        }
    }

    // ----------------------------
    // 3. 체크아웃 처리 리스너
    // ----------------------------
    class CheckOutListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (currentInfo == null) {
                view.showMessage("먼저 객실 조회를 해주세요.");
                return;
            }

            Room room = findRoom(currentInfo.getRoomNumber());
            if (room != null) {
                // Room.java에서 만든 Enum Status 사용
                room.setStatus(Room.Status.EMPTY);
            }

            // 부대 서비스 내역 초기화
            if (serviceDAO != null) {
                serviceDAO.clearServiceByRoom(currentInfo.getRoomNumber());
            }

            view.showMessage(currentInfo.getRoomNumber() + "호 체크아웃 완료!");
            view.clearFields();
            currentInfo = null;
        }
    }

    // ----------------------------
    // 4. 뒤로가기 리스너 (메인 화면 찾기)
    // ----------------------------
    class BackListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            // 현재 패널의 부모들을 거슬러 올라가며 CardLayout을 찾음
            Container parent = view.getParent();
            while (parent != null && !(parent.getLayout() instanceof CardLayout)) {
                parent = parent.getParent();
            }
            
            if (parent != null) {
                CardLayout layout = (CardLayout) parent.getLayout();
                layout.show(parent, "MAIN"); // Main.java에서 설정한 이름 "MAIN"
            }
        }
    }

    // 방 번호로 Room 객체 찾기
    private Room findRoom(String roomNum) {
        for (Room r : rooms) {
            if (r.getRoomNumber().equals(roomNum)) return r;
        }
        return null;
    }
}