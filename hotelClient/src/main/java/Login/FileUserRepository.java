/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/*
 staff.txt 파일에서 계정을 읽고 등록/삭제하는 파일 기반 저장소 구현
*/
package Login;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileUserRepository implements UserRepository {

    // staff.txt 포맷: id|pw|role
    private static final String FILE_PATH = "staff.txt";

    // ---------- 유틸 ----------

    private List<String> loadAllLines() {
        List<String> result = new ArrayList<>();
        File f = new File(FILE_PATH);

        // 파일 없으면 빈 리스트 반환
        if (!f.exists()) {
            return result;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    result.add(line.trim());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    private void saveAllLines(List<String> lines) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_PATH))) {
            for (String line : lines) {
                bw.write(line);
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // id|pw|role → User 객체로 변환
    private User parseUser(String line) {
        String[] parts = line.split("\\|");
        if (parts.length < 3) return null;

        String id = parts[0];
        String pw = parts[1];
        String role = parts[2];

        return new User(id, pw, role);
    }

    // User → 파일 한 줄 문자열
    private String toLine(User u) {
        return u.getId() + "|" + u.getPassword() + "|" + u.getRole();
    }

    // ---------- 구현 ----------

    @Override
    public User findById(String id) {
        for (String line : loadAllLines()) {
            User u = parseUser(line);
            if (u != null && u.getId().equals(id)) {
                return u;
            }
        }
        return null;
    }
    
    //등록
    @Override
    public boolean addUser(String id, String password, String role) {
        // 이미 존재하면 실패
        if (findById(id) != null) return false;

        List<String> lines = loadAllLines();
        User u = new User(id, password, role);
        lines.add(toLine(u));
        saveAllLines(lines);
        return true;
    }
    
    //삭제
    @Override
    public boolean deleteUser(String id) {
        List<String> lines = loadAllLines();
        boolean removed = lines.removeIf(line -> {
            User u = parseUser(line);
            return u != null && u.getId().equals(id);
        });

        if (removed) {
            saveAllLines(lines);
        }
        return removed;
    }
}

