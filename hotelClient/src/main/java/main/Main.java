package main;

/**
 * 호텔 관리 시스템 메인 클래스
 * @author subin
 */

import checkIn.CheckInController;
import checkOut.CheckOutController;
import checkOut.CheckOutView;
import reservation.ReservationController;
import roomAdmin.RoomAdminController;
import roomAdmin.RoomAdminView;
import javax.swing.*;
import java.awt.*;
//---------------
import roomAdmin.Room; // Room 객체 사용을 위해 추가
import java.util.ArrayList; // 임시 Room 목록 생성을 위해 추가
import java.util.List;
//---------------

public class Main extends JFrame {
    
    private CardLayout cardLayout;
    private JPanel mainContainer;
    private CheckOutView checkOutView;
    private CheckOutController checkOutCtrl;

    public Main() {
        setTitle("호텔 관리 시스템");
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        cardLayout = new CardLayout();
        mainContainer = new JPanel(cardLayout);
     
        MainMenuController mainMenuCtrl = new MainMenuController();
        ReservationController reservationCtrl = new ReservationController();
        CheckInController checkInCtrl = new CheckInController(); 
        //체크아웃추가
        AdminMenuController adminCtrl = new AdminMenuController();
        RoomAdminController roomCtrl = new RoomAdminController();
        
        // [메인 메뉴] -> 각 화면으로 이동
        mainMenuCtrl.setOnNavigateToReservation(() -> cardLayout.show(mainContainer, "RESERVATION"));
        mainMenuCtrl.setOnNavigateToCheckIn(() -> cardLayout.show(mainContainer, "CHECK_IN"));
        mainMenuCtrl.setOnNavigateToCheckOut(() -> cardLayout.show(mainContainer, "CHECK_OUT"));
        mainMenuCtrl.setOnNavigateToAdmin(() -> cardLayout.show(mainContainer, "ADMIN_MENU"));

        // [예약 화면]
        reservationCtrl.setOnSuccess(() -> cardLayout.show(mainContainer, "MAIN")); // 예약 성공 시 메인
        reservationCtrl.getView().addBackListener(e -> cardLayout.show(mainContainer, "MAIN")); // 뒤로가기

        // [체크인 화면]
        checkInCtrl.setOnSuccess(() -> cardLayout.show(mainContainer, "MAIN")); // 체크인 성공 시 메인
        checkInCtrl.getView().addBackListener(e -> cardLayout.show(mainContainer, "MAIN")); // 뒤로가기
        
        // [체크아웃 화면] 로직 (혹시 체크아웃 컨트롤러에 리스너 설정 메소드가 있다면 여기에 추가)
        checkOutCtrl.setOnSuccess(() -> cardLayout.show(mainContainer, "MAIN")); // 체크인 성공 시 메인
        checkOutCtrl.getView().addBackListener(e -> cardLayout.show(mainContainer, "MAIN")); // 뒤로가기

        // [관리자 메뉴]
        adminCtrl.setOnBack(() -> cardLayout.show(mainContainer, "MAIN"));
        
        // [관리자 메뉴] -> 객실 관리 화면
        adminCtrl.setOnNavigateToRoomAdmin(() -> cardLayout.show(mainContainer, "ROOM_ADMIN"));
        roomCtrl.getView().addBackListener(e -> cardLayout.show(mainContainer, "ADMIN_MENU"));


        // 5. 메인 컨테이너에 화면 등록
        mainContainer.add(mainMenuCtrl.getView(), "MAIN");
        mainContainer.add(reservationCtrl.getView(), "RESERVATION");
        mainContainer.add(checkInCtrl.getView(), "CHECK_IN");
        mainContainer.add(checkOutCtrl.getView(), "CHECK_OUT");//체크아웃
        mainContainer.add(adminCtrl.getView(), "ADMIN_MENU");
        
        // 관리
        mainContainer.add(roomCtrl.getView(), "ROOM_ADMIN");
        
        // 6. 화면 출력
        add(mainContainer);
        setVisible(true);
        
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    
    /** CheckOutController 및 RoomAdminController 테스트를 위한 더미 Room 목록 생성 */
    private List<Room> createDummyRooms() {
        List<Room> rooms = new ArrayList<>();
        // Room 클래스가 roomAdmin.Room에 있고, Status, Type enum이 정의되어 있다고 가정
        rooms.add(new Room("101", Room.Type.STANDARD, 100000.0, Room.Status.OCCUPIED));
        rooms.add(new Room("205", Room.Type.DELUXE, 150000.0, Room.Status.AVAILABLE));
        return rooms;
    }
    
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                new Main();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}