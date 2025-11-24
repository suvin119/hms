package main;

/**
 * 메인 메뉴 컨트롤러
 * @author subin
 */

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JPanel;

public class MainMenuController {
    
    private MainMenuView view;
    
    private Runnable onNavigateToReservation;
    private Runnable onNavigateToCheckIn;
    private Runnable onNavigateToCheckOut;
    private Runnable onNavigateToAdmin;

    public MainMenuController() {
        this.view = new MainMenuView();
        initListeners();
    }
    
    private void initListeners() {
        
        // 1. 예약 버튼 리스너
        view.setReservationListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (onNavigateToReservation != null) {
                    onNavigateToReservation.run(); // Main.java로 화면 전환 요청
                }
            }
        });

        // 2. 체크인 버튼 리스너
        view.setCheckInListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (onNavigateToCheckIn != null) {
                    onNavigateToCheckIn.run();
                }
            }
        });

        // 3. 체크아웃 버튼 리스너
        view.setCheckOutListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (onNavigateToCheckOut != null) {
                    onNavigateToCheckOut.run();
                }
            }
        });
        
        //5. 관리자 메뉴 버튼 리스너
        view.setAdminButtonListener(new ActionListener() { 
            @Override
            public void actionPerformed(ActionEvent e) {
                if (Session.currentUser != null && Session.currentUser.isAdmin()) {
                    if (onNavigateToAdmin != null) {
                        onNavigateToAdmin.run();
                    }
                } else {
                    view.showMessage("접근 권한이 없습니다. (관리자 전용)");
                }
            }
        });
  
    }
    
    // 뷰 반환 (Main.java에서 사용)
    public JPanel getView() { return view; }

    // Main.java에서 화면 전환 기능을 연결해주는 통로들
    public void setOnNavigateToReservation(Runnable action) { this.onNavigateToReservation = action; }
    public void setOnNavigateToCheckIn(Runnable action) { this.onNavigateToCheckIn = action; }
    public void setOnNavigateToCheckOut(Runnable action) { this.onNavigateToCheckOut = action; }
    public void setOnNavigateToAdmin(Runnable action) { this.onNavigateToAdmin = action; }
}