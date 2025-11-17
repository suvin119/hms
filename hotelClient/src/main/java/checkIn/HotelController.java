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
import java.util.ArrayList;
import java.util.List;

public class HotelController {
    private List<Room> rooms;
    private ConsoleView view;

    public HotelController(ConsoleView view) {
        this.view = view;
        this.rooms = new ArrayList<>();
        
        // 데이터 초기화
        rooms.add(new Room(101));
        rooms.add(new Room(102));
        rooms.add(new Room(103));

        // 초기 화면 갱신
        view.updateRoomList(rooms);

        // 버튼에 기능 연결 (이벤트 리스너)
        view.setCheckInButtonListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleCheckIn();
            }
        });
    }

    // 체크인 로직
    private void handleCheckIn() {
        try {
            String roomNumStr = view.getRoomNumInput();
            String name = view.getNameInput();

            if (roomNumStr.isEmpty() || name.isEmpty()) {
                view.showMessage("방 번호와 이름을 모두 입력해주세요.");
                return;
            }

            int roomNum = Integer.parseInt(roomNumStr);
            Room selectedRoom = findRoom(roomNum);

            if (selectedRoom == null) {
                view.showMessage("존재하지 않는 방입니다.");
            } else if (selectedRoom.isOccupied()) {
                view.showMessage("이미 사용 중인 방입니다.");
            } else {
                // 체크인 성공 처리
                selectedRoom.checkIn(name);
                view.updateRoomList(rooms); // 화면 갱신
                view.clearInputs(); // 입력창 비우기
                view.showMessage(name + "님 체크인 완료!");
            }

        } catch (NumberFormatException ex) {
            view.showMessage("방 번호는 숫자만 입력해주세요.");
        }
    }

    private Room findRoom(int roomNum) {
        for (Room r : rooms) {
            if (r.getRoomNumber() == roomNum) return r;
        }
        return null;
    }
}