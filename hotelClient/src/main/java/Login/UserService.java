/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Login;

/*
직원관리 기능 (등록/삭제) 구현
*/

import java.io.*;
import java.net.Socket;

public class UserService {
    private final String SERVER_IP = "127.0.0.1";
    private final int SERVER_PORT = 9999;

    // 생성자
    public UserService() { }

    // 로그인 요청
    public User authenticate(String id, String password) {
        String msg = "LOGIN|" + id + "|" + password;
        String res = sendRequest(msg); // 서버로 전송

        if (res != null && res.startsWith("SUCCESS")) {
            // 응답 예: "SUCCESS|ADMIN"
            String[] parts = res.split("\\|");
            String role = (parts.length > 1) ? parts[1] : "STAFF";
            return new User(id, password, role);
        }
        return null; // 로그인 실패
    }

    // 직원 등록 요청
    public boolean registerUser(String id, String password, String role) {
        String msg = "STAFF_ADD|" + id + "|" + password + "|" + role;
        String res = sendRequest(msg);
        return res != null && res.startsWith("SUCCESS");
    }

    // 직원 삭제 요청
    public boolean removeUser(String id) {
        String msg = "STAFF_DELETE|" + id;
        String res = sendRequest(msg);
        return res != null && res.startsWith("SUCCESS");
    }

    // 소켓 통신 공통 메소드
    private String sendRequest(String msg) {
        try (Socket s = new Socket(SERVER_IP, SERVER_PORT);
             PrintWriter out = new PrintWriter(new OutputStreamWriter(s.getOutputStream(), "UTF-8"), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream(), "UTF-8"))) {
            
            out.println(msg);
            return in.readLine();
            
        } catch (IOException e) {
            e.printStackTrace();
            return "ERROR|통신 실패";
        }
    }
}