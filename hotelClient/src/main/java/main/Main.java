package main;

/**
 *
 * @author subin
 */

import checkIn.CheckInController;
import checkOut.CheckOutController;
import checkOut.CheckOutView;
import UnitServices.ServiceDAO;
import reservation.ReservationController;
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
        CheckOutView coView = new CheckOutView(); 
        UnitServices.ServiceDAO coService = new UnitServices.ServiceDAO(); 
        CheckOutController checkOutCtrl = new CheckOutController(coView, coService);
        AdminMenuController adminCtrl = new AdminMenuController();

        
        // [메인 메뉴 -> 예약 화면]
        mainMenuCtrl.setOnNavigateToReservation(() -> {
            cardLayout.show(mainContainer, "RESERVATION");
        });

        // [메인 메뉴 -> 체크인 화면]
        mainMenuCtrl.setOnNavigateToCheckIn(() -> {
            cardLayout.show(mainContainer, "CHECK_IN");
        });
        
        // [메인 메뉴 -> 체크아웃 화면]
        mainMenuCtrl.setOnNavigateToCheckOut(() -> {
            cardLayout.show(mainContainer, "CHECK_OUT");
        });
        
        // [메인 메뉴 -> 관리 메뉴 화면]
        mainMenuCtrl.setOnNavigateToAdmin(() -> {
            cardLayout.show(mainContainer, "ADMIN_MENU");
        });

        // [예약 완료 -> 메인 화면]
        reservationCtrl.setOnSuccess(() -> {
            cardLayout.show(mainContainer, "MAIN");
        });
        
        // [예약 화면에서 뒤로가기 -> 메인 화면]
        reservationCtrl.getView().addBackListener(e -> {
            cardLayout.show(mainContainer, "MAIN");
        });
        
        // [체크인 완료 -> 메인 화면]
        checkInCtrl.setOnSuccess(() -> {
             cardLayout.show(mainContainer, "MAIN");
        });
        
        // [관리 메뉴 -> 메인 메뉴]
        adminCtrl.setOnBack(() -> {
            cardLayout.show(mainContainer, "MAIN");
        });

        
        mainContainer.add(mainMenuCtrl.getView(), "MAIN");
        mainContainer.add(reservationCtrl.getView(), "RESERVATION");
        mainContainer.add(checkInCtrl.getView(), "CHECK_IN");
        mainContainer.add(coView, "CHECK_OUT");
        mainContainer.add(adminCtrl.getView(), "ADMIN_MENU");

        
        add(mainContainer);
        setVisible(true);
    }

    public static void main(String[] args) {
        new Main();
    }
}