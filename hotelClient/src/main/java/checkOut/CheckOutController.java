package checkOut;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class CheckOutController {
    private CheckOutView view;
    private List<Room> rooms;
    private Room currentRoom;

    public CheckOutController(CheckOutView view) {
        this.view = view;
        this.rooms = Room.loadRooms();

        view.setSearchListener(new SearchListener());
        view.setCalculateListener(new CalculateListener());
        view.setCheckOutListener(new CheckOutListener());
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
        }
    }
}
