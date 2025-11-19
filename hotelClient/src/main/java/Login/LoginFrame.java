/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Login;

import javax.swing.*;
import java.awt.*;

public class LoginFrame extends JFrame {
    private JTextField txtId;
    private JPasswordField txtPassword;
    
    private final UserService userService =
            new UserService(new FileUserRepository());
            
    public LoginFrame() {
        setTitle("HMS 로그인");
        setSize(350, 200);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null); //화면 가운데
        
        iniUI();
    }
    
    private void iniUI() {
        JPanel panel = new JPanel(new GridLayout(3, 2, 10, 10));
        
        panel.add(new JLabel("아이디"));
        txtId = new JTextField();
        panel.add(txtId);
        
        panel.add(new JLabel("비밀번호:"));
        txtPassword = new JPasswordField();
        panel.add(txtPassword);

        JButton btnLogin = new JButton("로그인");
        btnLogin.addActionListener(e -> login());

        panel.add(new JLabel()); // 빈 칸용
        panel.add(btnLogin);

        add(panel);
    }
    
    private void login() {
        String id = txtId.getText();
        String pw = new String(txtPassword.getPassword());

        // SFR-102 호출
        User user = userService.authenticate(id, pw);

        if (user == null) {
            // SFR-105: 실패 화면(메시지) 표시
            JOptionPane.showMessageDialog(
                    this,
                    "아이디 또는 비밀번호가 잘못되었습니다.",
                    "로그인 실패",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        // 로그인 성공
        JOptionPane.showMessageDialog(
                this,
                "로그인 성공! 역할: " + user.getRole()
        );

        // TODO: 여기서 관리자/직원에 따라 다음 화면 열기
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginFrame().setVisible(true));
    }
}