<<<<<<< Updated upstream
package main;

/**
 * @author subin
 */



import checkIn.CheckInController; // 체크인 임포트
import checkOut.CheckOutView; //체크아웃

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JPanel;

public class MainMenuController {
    
    private MainMenuView view;
    
    // 화면 전환을 위한 콜백 인터페이스 정의
    private Runnable onNavigateToReservation;
    private Runnable onNavigateToCheckIn;
    private Runnable onNavigateToCheckOut;
    private Runnable onNavigateToAdmin;

    public MainMenuController() {
        this.view = new MainMenuView();
        initListeners();
    }
    
    // 예약
    private void initListeners() {
        view.setReservationListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (onNavigateToReservation != null) {
                    onNavigateToReservation.run();
                }
            }
        });

        // 체크인
        view.setCheckInListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (onNavigateToCheckIn != null) {
                    onNavigateToCheckIn.run();
                }
            }
        });

        // 체크아웃
        view.setCheckOutListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if (onNavigateToCheckOut != null) {
                    onNavigateToCheckOut.run();
                }

                new CheckOutView(); 
            }
        });

        // 객실 관리 버튼
        view.setRoomManageListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                view.showMessage("객실 관리 기능은 개발 중입니다.");

                
            }
        });
        
        //관리 메뉴
        view.setRoomManageListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (Session.currentUser.isAdmin()) {
                    if (onNavigateToAdmin != null) {
                        onNavigateToAdmin.run();
                    }
                } else {
                    view.showMessage("접근 권한이 없습니다. (관리자 전용)");
                }
            }
        });
    }
    
    public JPanel getView() { return view; }

    public void setOnNavigateToReservation(Runnable action) { this.onNavigateToReservation = action; }
    public void setOnNavigateToCheckIn(Runnable action) { this.onNavigateToCheckIn = action; }
    public void setOnNavigateToCheckOut(Runnable action) { this.onNavigateToCheckOut = action; }
    public void setOnNavigateToAdmin(Runnable action) { this.onNavigateToAdmin = action; }
}
=======
package main;

/**
 * @author subin
 */

import checkIn.CheckInController; // 체크인 임포트
import checkOut.CheckOutView; //체크아웃
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainMenuController {
    
    private MainMenuView view;

    public MainMenuController() {
        this.view = new MainMenuView();
        initListeners();
    }

    private void initListeners() {
        
        // 예약 등록 버튼
        view.setReservationListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                view.showMessage("예약 등록 기능은 개발 중");
                //new ReservatioController();
            }
        });

        // 체크인 버튼
        view.setCheckInListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new CheckInController(); 
            }
        });

        // 체크아웃 버튼
        view.setCheckOutListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new CheckOutView(); 
            }
        });

        // 객실 관리 버튼
        view.setRoomManageListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                view.showMessage("객실 관리 기능은 개발 중입니다.");
                
            }
        });
    }
}
>>>>>>> Stashed changes
