package main;

import checkIn.CheckInController;
import checkOut.CheckOutController;
import checkOut.CheckOutView;
import reservation.ReservationController;
import customers.CustomersController;
import roomAdmin.RoomAdminController;
import javax.swing.*;
import java.awt.*;

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

        // 메인 컨테이너
        cardLayout = new CardLayout();
        mainContainer = new JPanel(cardLayout);

        // 각 화면 컨트롤러 생성
        MainMenuController mainMenuCtrl = new MainMenuController();
        ReservationController reservationCtrl = new ReservationController();
        CheckInController checkInCtrl = new CheckInController();
        AdminMenuController adminCtrl = new AdminMenuController();
        RoomAdminController roomCtrl = new RoomAdminController();
        CustomersController customerCtrl = new CustomersController();

        // 체크아웃 화면 생성 (더미 방 없음)
        checkOutView = new CheckOutView();
        checkOutCtrl = new CheckOutController(checkOutView);

        // -------------------
        // 화면 전환 이벤트
        // -------------------
        mainMenuCtrl.setOnNavigateToReservation(() -> cardLayout.show(mainContainer, "RESERVATION"));
        mainMenuCtrl.setOnNavigateToCheckIn(() -> cardLayout.show(mainContainer, "CHECK_IN"));
        mainMenuCtrl.setOnNavigateToCheckOut(() -> cardLayout.show(mainContainer, "CHECK_OUT"));
        mainMenuCtrl.setOnNavigateToAdmin(() -> cardLayout.show(mainContainer, "ADMIN_MENU"));

        // 예약 화면 뒤로가기
        reservationCtrl.setOnSuccess(() -> cardLayout.show(mainContainer, "MAIN"));
        reservationCtrl.getView().addBackListener(e -> cardLayout.show(mainContainer, "MAIN"));

        // 체크인 화면
        checkInCtrl.setOnSuccess(() -> cardLayout.show(mainContainer, "MAIN"));
        checkInCtrl.getView().addBackListener(e -> cardLayout.show(mainContainer, "MAIN"));


        // 체크아웃 화면
        checkOutCtrl.setOnSuccess(() -> cardLayout.show(mainContainer, "MAIN"));
        checkOutView.addBackListener(e -> cardLayout.show(mainContainer, "MAIN"));

        // 관리자 메뉴
        adminCtrl.setOnBack(() -> cardLayout.show(mainContainer, "MAIN"));
        adminCtrl.setOnNavigateToRoomAdmin(() -> cardLayout.show(mainContainer, "ROOM_ADMIN"));
        adminCtrl.setOnNavigateToCustomer(() -> cardLayout.show(mainContainer, "CUSTOMER_ADMIN"));
        roomCtrl.getView().addBackListener(e -> cardLayout.show(mainContainer, "ADMIN_MENU"));
        customerCtrl.getView().addBackListener(e -> cardLayout.show(mainContainer, "ADMIN_MENU"));

        // 화면 등록
        mainContainer.add(mainMenuCtrl.getView(), "MAIN");
        mainContainer.add(reservationCtrl.getView(), "RESERVATION");
        mainContainer.add(checkInCtrl.getView(), "CHECK_IN");
        mainContainer.add(checkOutView, "CHECK_OUT");
        mainContainer.add(adminCtrl.getView(), "ADMIN_MENU");
        mainContainer.add(roomCtrl.getView(), "ROOM_ADMIN");
        mainContainer.add(customerCtrl.getView(), "CUSTOMER_ADMIN");

        // 메인 표시
        add(mainContainer);
        cardLayout.show(mainContainer, "MAIN");

        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                System.out.println("Application starting on EDT...");
                new Main();
            } catch (Exception e) {
                System.err.println("FATAL ERROR during UI initialization:");
                e.printStackTrace();
            }
        });
    }
}
