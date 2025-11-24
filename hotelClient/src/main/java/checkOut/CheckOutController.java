package checkOut;

<<<<<<< Updated upstream
<<<<<<< Updated upstream
import UnitServices.ServiceDAO;

import java.awt.*;
=======
>>>>>>> Stashed changes
=======
>>>>>>> Stashed changes
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class CheckOutController {

    private CheckOutView view;
    private List<Room> rooms;
<<<<<<< Updated upstream
<<<<<<< Updated upstream
    private ServiceDAO serviceDAO;
    private CheckOutInfo currentInfo;

    public CheckOutController(CheckOutView view) {
        this.view = view;
        rooms = Room.loadRooms();
        serviceDAO = new ServiceDAO();
=======
    private Room currentRoom;

    public CheckOutController(CheckOutView view) {
        this.view = view;
        this.rooms = Room.loadRooms();
>>>>>>> Stashed changes
=======
    private Room currentRoom;

    public CheckOutController(CheckOutView view) {
        this.view = view;
        this.rooms = Room.loadRooms();
>>>>>>> Stashed changes

        view.setSearchListener(new SearchListener());
        view.setCalculateListener(new CalculateListener());
        view.setCheckOutListener(new CheckOutListener());
<<<<<<< Updated upstream
<<<<<<< Updated upstream
        view.setBackListener(new BackListener());
    }

    // ----------------------------
    // 객실 조회
    // ----------------------------
=======
        view.setBackListener(e -> view.goBackToMain());
    }

>>>>>>> Stashed changes
    class SearchListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String roomNum = view.getRoomNumber();
<<<<<<< Updated upstream
            if (roomNum.isEmpty()) {
                view.showMessage("객실 번호를 입력하세요.");
                return;
            }

            Room room = findRoom(roomNum);
            if (room == null) {
                view.showMessage("존재하지 않는 객실입니다.");
                return;
            }

            // 부대 서비스 총액 조회
            int serviceTotal = serviceDAO.getServiceTotalByRoom(roomNum);

            currentInfo = new CheckOutInfo(
                    roomNum,
                    "고객 이름", // 실제 고객명 연동 시 변경 가능
                    room.getRoomNumber(),
                    "2025-11-20",
                    "2025-11-24",
                    serviceTotal,
                    0
            );

            view.setInfoText(currentInfo.formatInfo());
        }
    }

    // ----------------------------
    // 요금 계산
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
                    "\n총 금액 = " + total + "원");
=======
        view.setBackListener(e -> view.goBackToMain());
    }

    class SearchListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String roomNum = view.getRoomNumber();
            if (roomNum.isEmpty()) { view.showMessage("객실 번호를 입력하세요."); return; }

            currentRoom = rooms.stream()
                    .filter(r -> r.getRoomNumber().equals(roomNum))
                    .findFirst()
                    .orElse(null);

            if (currentRoom == null) {
                view.showMessage("해당 객실 정보가 없습니다.");
                view.clearFields();
                return;
            }

            view.setInfoText(
                    "객실번호: " + currentRoom.getRoomNumber() + "\n" +
                    "타입: " + currentRoom.getType() + "\n" +
                    "가격: " + currentRoom.getPrice() + "\n" +
                    "상태: " + currentRoom.getStatus()
            );
        }
    }

    class CalculateListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (currentRoom == null) { view.showMessage("먼저 객실을 조회하세요."); return; }

            // 테스트용 부대 서비스
            StringBuilder sb = new StringBuilder("【부대서비스 요금 내역】\n");
            int total = 0;

            sb.append("룸서비스 - 15000원\n");
            sb.append("세탁서비스 - 8000원\n");
            total = 15000 + 8000;

            sb.append("\n총 부대서비스 요금: ").append(total).append("원");
            view.setInfoText(view.taInfo.getText() + "\n\n" + sb.toString());
        }
    }

    class CheckOutListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (currentRoom == null) { view.showMessage("먼저 객실을 조회하세요."); return; }

            currentRoom.setStatus("빈객실");
            view.showMessage("체크아웃 완료! 객실이 비었습니다.");
            view.clearFields();
            currentRoom = null;
>>>>>>> Stashed changes
        }
    }

    // ----------------------------
    // 체크아웃 처리
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
                room.setStatus(Room.Status.EMPTY);
            }

            // 부대 서비스 초기화
            serviceDAO.clearServiceByRoom(currentInfo.getRoomNumber());

            view.showMessage(currentInfo.getRoomNumber() + " 체크아웃 완료!");
            view.clearFields();
            currentInfo = null;
=======
            if (roomNum.isEmpty()) { view.showMessage("객실 번호를 입력하세요."); return; }

            currentRoom = rooms.stream()
                    .filter(r -> r.getRoomNumber().equals(roomNum))
                    .findFirst()
                    .orElse(null);

            if (currentRoom == null) {
                view.showMessage("해당 객실 정보가 없습니다.");
                view.clearFields();
                return;
            }

            view.setInfoText(
                    "객실번호: " + currentRoom.getRoomNumber() + "\n" +
                    "타입: " + currentRoom.getType() + "\n" +
                    "가격: " + currentRoom.getPrice() + "\n" +
                    "상태: " + currentRoom.getStatus()
            );
        }
    }

    class CalculateListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (currentRoom == null) { view.showMessage("먼저 객실을 조회하세요."); return; }

            // 테스트용 부대 서비스
            StringBuilder sb = new StringBuilder("【부대서비스 요금 내역】\n");
            int total = 0;

            sb.append("룸서비스 - 15000원\n");
            sb.append("세탁서비스 - 8000원\n");
            total = 15000 + 8000;

            sb.append("\n총 부대서비스 요금: ").append(total).append("원");
            view.setInfoText(view.taInfo.getText() + "\n\n" + sb.toString());
        }
    }

    class CheckOutListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (currentRoom == null) { view.showMessage("먼저 객실을 조회하세요."); return; }

            currentRoom.setStatus("빈객실");
            view.showMessage("체크아웃 완료! 객실이 비었습니다.");
            view.clearFields();
            currentRoom = null;
>>>>>>> Stashed changes
        }
    }

    // ----------------------------
    // 뒤로가기
    // ----------------------------
    class BackListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            Container parent = view.getParent();
            while (parent != null && !(parent.getLayout() instanceof CardLayout)) {
                parent = parent.getParent();
            }
            if (parent == null) return;

            CardLayout layout = (CardLayout) parent.getLayout();
            layout.show(parent, "MAIN");
        }
    }

    private Room findRoom(String roomNum) {
        for (Room r : rooms) {
            if (r.getRoomNumber().equals(roomNum)) return r;
        }
        return null;
    }
}
