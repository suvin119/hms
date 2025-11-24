package main;

/**
 * 호텔 관리 시스템 메인 클래스
 * @author subin
 */

import checkIn.CheckInController;
//import checkOut.CheckOutController;
//import checkOut.CheckOutView;
import reservation.ReservationController;
//import UnitServices.ServiceDAO; 
import javax.swing.*;
import java.awt.*;

public class Main extends JFrame {
    
    private CardLayout cardLayout;
    private JPanel mainContainer;

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

        // [관리자 메뉴] 로직
        adminCtrl.setOnBack(() -> cardLayout.show(mainContainer, "MAIN"));


        // 5. 메인 컨테이너에 화면 등록 ("이름표" 붙이기)
        mainContainer.add(mainMenuCtrl.getView(), "MAIN");
        mainContainer.add(reservationCtrl.getView(), "RESERVATION");
        mainContainer.add(checkInCtrl.getView(), "CHECK_IN");
        //체크아웃
        mainContainer.add(adminCtrl.getView(), "ADMIN_MENU");

        
        // 6. 화면 출력
        add(mainContainer);
        setVisible(true);
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