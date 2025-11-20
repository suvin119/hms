package main;

/**
 * @author subin
 */

import checkIn.CheckInController; // 체크인 임포트
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
                view.showMessage("체크아웃 기능은 개발 중입니다.");
                //new CheckOutController(); 
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
