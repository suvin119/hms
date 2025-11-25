package main;

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
        
        // 예약 등록 버튼
        view.setReservationListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // new ReservatioController();
                if (onNavigateToReservation != null) {
                    onNavigateToReservation.run();
                }
            }
        });

        // 체크인 버튼
        view.setCheckInListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (onNavigateToCheckIn != null) {
                    onNavigateToCheckIn.run();
                }
            }
        });

        // 체크아웃 버튼
        view.setCheckOutListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (onNavigateToCheckOut != null) {
                    onNavigateToCheckOut.run();
                }
            }
        });

        // 관리 메뉴 (기존 객실 관리 버튼)
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