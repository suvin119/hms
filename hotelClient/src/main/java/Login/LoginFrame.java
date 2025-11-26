/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Login;

import main.Main; 

import javax.swing.*;
import java.awt.*;

public class LoginFrame extends JFrame {
    private JTextField txtId;
    private JPasswordField txtPassword;
    
    // 파일 기반 유저 서비스
    private final UserService userService = new UserService(new FileUserRepository());
            
    public LoginFrame() {
        setTitle("HMS 로그인");
        setSize(350, 200);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null); 
        
        iniUI();
    }
    
    private void iniUI() {
        JPanel panel = new JPanel(new GridLayout(3, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); 
        
        panel.add(new JLabel("아이디:"));
        txtId = new JTextField();
        panel.add(txtId);
        
        panel.add(new JLabel("비밀번호:"));
        txtPassword = new JPasswordField();
        panel.add(txtPassword);

        JButton btnLogin = new JButton("로그인");
        btnLogin.addActionListener(e -> login());

        panel.add(new JLabel("")); 
        panel.add(btnLogin);

        add(panel);
    }
    
   private void login() {
        String id = txtId.getText().trim();
        String pw = new String(txtPassword.getPassword()).trim();

        if (id.isEmpty() || pw.isEmpty()) {
            JOptionPane.showMessageDialog(this, "아이디와 비밀번호를 입력하세요.");
            return;
        }

        // 1. 로그인 인증 시도
        Login.User loginUser = userService.authenticate(id, pw);

        if (loginUser == null) {
            JOptionPane.showMessageDialog(this, 
                "로그인 실패!\n아이디와 비밀번호를 확인해주세요.", 
                "오류", JOptionPane.ERROR_MESSAGE);
            return;
        }

        //  로그인 성공 시 전역 세션(Session)에 정보 등록
        // Login.User -> main.User 변환이 필요할 수 있습니다.
        main.Session.currentUser = new main.User(
                loginUser.getId(), 
                loginUser.getPassword(), 
                loginUser.getRole()
        );

        JOptionPane.showMessageDialog(this, loginUser.getId() + " 환영합니다! (" + loginUser.getRole() + ")", "성공", JOptionPane.INFORMATION_MESSAGE);
        
        dispose(); // 로그인 창 닫기
        new main.Main(); // 메인 화면 열기
    }
}
