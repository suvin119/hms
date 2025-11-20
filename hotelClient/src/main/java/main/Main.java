package main;

/**
 *
 * @author subin
 */

import checkIn.CheckInController;
import checkIn.CheckInView;
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

        // 3. 카드 레이아웃에 화면 등록 
        mainContainer.add(mainMenuView, "MAIN");     // "MAIN" 이름표
        mainContainer.add(checkInView, "CHECK_IN");  // "CHECK_IN" 이름표

        // 4. 메인 메뉴의 버튼 동작 정의
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
        
        add(mainContainer);
        setVisible(true);
    }

    public static void main(String[] args) {
        new Main();
    }
}
