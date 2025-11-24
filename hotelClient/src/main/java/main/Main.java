<<<<<<< Updated upstream
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
        CheckOutController checkOutCtrl = new CheckOutController(coView);
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
        CheckOutController checkOutCtrl = new CheckOutController(coView);
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
=======
package main;

/**
 *
 * @author subin
 */

import checkIn.CheckInController;
import checkIn.CheckInView;

import reservation.ReservationController;
import reservation.ReservationView;

import checkOut.CheckOutController;
import checkOut.CheckOutView;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Main extends JFrame {
    
    private CardLayout cardLayout;
    private JPanel mainContainer; // 화면들이 담길 통

    public Main() {
        setTitle("호텔 관리 시스템");
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        cardLayout = new CardLayout();
        mainContainer = new JPanel(cardLayout);

        // 메인 메뉴 화면
        MainMenuView mainMenuView = new MainMenuView();
        
        // 체크인 화면 (컨트롤러랑 연결)
        CheckInController checkInCtrl = new CheckInController(); 
        CheckInView checkInView = checkInCtrl.getView(); 
        
        // 예약 화면
        ReservationController reservationCtrl = new ReservationController();
        ReservationView reservationview = reservationCtrl.getView();

        // 3. 카드 레이아웃에 화면 등록 
        mainContainer.add(mainMenuView, "MAIN");     // "MAIN" 이름표
        mainContainer.add(reservationview, "RESERVATION");
        mainContainer.add(checkInView, "CHECK_IN");  // "CHECK_IN" 이름표
        mainContainer.add(checkOutView, "CHECK_OUT"); // "CHECK_OUT" 이름표

        // 4. 메인 메뉴의 버튼 동작 정의
        // 메인 -> 예약
        mainMenuView.setReservationListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(mainContainer, "RESERVATION"); // RESERVATION으로 이동
            }
        });
        
        // 예약 확정 시 -> 메인으로 이동하도록 콜백 연결
        reservationCtrl.setOnSuccess(() -> {
            cardLayout.show(mainContainer, "MAIN");
        });
        
        // 예약 화면에서 뒤로가기 시 -> 메인 (뒤로가기 버튼 리스너 연결)
        reservationview.addBackListener(e -> {
            cardLayout.show(mainContainer, "MAIN");
        });
        
        // 메인 -> 체크인
        mainMenuView.setCheckInListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(mainContainer, "CHECK_IN");
            }
        });
        
        // 체크인 완료 시 -> 메인
        checkInCtrl.setOnSuccess(() -> {
            cardLayout.show(mainContainer, "MAIN"); // 메인 화면으로 전환
        });
        
        // 메인 -> 체크아웃
        mainMenuView.setCheckOutListener(e -> cardLayout.show(mainContainer, "CHECK_OUT"));
        
         // 체크아웃 화면 뒤로가기 -> 메인
        checkOutView.setBackListener(e -> cardLayout.show(mainContainer, "MAIN"));
        
        add(mainContainer);
        setVisible(true);
    }

    public static void main(String[] args) {
        new Main();
    }
}
>>>>>>> Stashed changes
