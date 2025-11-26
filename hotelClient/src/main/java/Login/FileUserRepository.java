/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Login;

/*
 staff.txt 파일에서 계정을 읽고 등록/삭제하는 파일 기반 저장소 구현
*/

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileUserRepository implements UserRepository {

    private static final String FILE_PATH = "staff.txt";

    private List<String> loadAllLines() {
        List<String> result = new ArrayList<>();
        File f = new File(FILE_PATH);

        if (!f.exists()) return result;

        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(f), "UTF-8"))) {
            String line;
            while ((line = br.readLine()) != null) {
                // 빈 줄은 무시
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
        try (BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(FILE_PATH), "UTF-8"))) {
            for (String line : lines) {
                bw.write(line);
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 파일에서 읽은 한 줄을 User 객체로 변환
    private User parseUser(String line) {
        String[] parts = line.split("\\|");
        if (parts.length < 3) return null;

        String id = parts[0].trim();
        String pw = parts[1].trim();
        String role = parts[2].trim();

        return new User(id, pw, role);
    }

    private String toLine(User u) {
        return u.getId() + "|" + u.getPassword() + "|" + u.getRole();
    }

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
    
    @Override
    public boolean addUser(String id, String password, String role) {
        if (findById(id) != null) return false;

        List<String> lines = loadAllLines();
        User u = new User(id, password, role);
        lines.add(toLine(u));
        saveAllLines(lines);
        return true;
    }
    
    @Override
    public boolean deleteUser(String id) {
        List<String> lines = loadAllLines();
        boolean removed = lines.removeIf(line -> {
            User u = parseUser(line);
            return u != null && u.getId().equals(id);
        });
        
        if (removed) {
            saveAllLines(lines);
            return true;
        }
        return false;
    }
}
