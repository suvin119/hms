/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hms.hotelserver;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author smhon
 */
public class StaffService {

    private static final String FILE_PATH = "src/main/resources/staff.txt";

    private static class Staff {
        String id;
        String pw;
        String role;

        Staff(String id, String pw, String role) {
            this.id = id;
            this.pw = pw;
            this.role = role;
        }
    }

    private List<String> loadAllLines() {
        List<String> result = new ArrayList<>();
        File f = new File(FILE_PATH);
        if (!f.exists()) return result;

        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(new FileInputStream(f), "UTF-8"))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (!line.trim().isEmpty()) result.add(line.trim());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    private void saveAllLines(List<String> lines) {
        try (BufferedWriter bw = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(FILE_PATH), "UTF-8"))) {
            for (String line : lines) {
                bw.write(line);
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Staff parseStaff(String line) {
        String[] parts = line.split("\\|");
        if (parts.length < 3) return null;
        return new Staff(parts[0].trim(), parts[1].trim(), parts[2].trim());
    }

    // 로그인
    public String login(String id, String pw) {
        for (String line : loadAllLines()) {
            Staff s = parseStaff(line);
            if (s != null && s.id.equals(id) && s.pw.equals(pw)) {
                return "SUCCESS|" + s.role;
            }
        }
        return "ERROR|INVALID_CREDENTIALS";
    }

    // 직원 추가
    public String addStaff(String id, String pw, String role) {
        List<String> lines = loadAllLines();
        for (String line : lines) {
            Staff s = parseStaff(line);
            if (s != null && s.id.equals(id)) {
                return "ERROR|DUPLICATE_ID";
            }
        }
        lines.add(id + "|" + pw + "|" + role);
        saveAllLines(lines);
        return "SUCCESS|ADDED";
    }

    // 직원 삭제
    public String deleteStaff(String id) {
        List<String> lines = loadAllLines();
        boolean removed = lines.removeIf(line -> {
            Staff s = parseStaff(line);
            return s != null && s.id.equals(id);
        });
        if (!removed) return "ERROR|NOT_FOUND";

        saveAllLines(lines);
        return "SUCCESS|DELETED";
    }
}