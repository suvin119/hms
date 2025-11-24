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
    private CheckOutInfo currentInfo;

    public CheckOutController(CheckOutView view) {
        this.view = view;
        rooms = Room.loadRooms();
        serviceDAO = new ServiceDAO();

        view.setSearchListener(new SearchListener());
        view.setCalculateListener(new CalculateListener());
        view.setCheckOutListener(new CheckOutListener());
        view.setBackListener(new BackListener());
    }

    // ----------------------------
    // 객실 조회
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
