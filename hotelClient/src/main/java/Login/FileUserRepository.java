/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Login;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileUserRepository implements UserRepository {

    // 파일경로
    private static final String FILE_PATH = "user.txt";   // id|pw|role

    // ID로 찾기
    @Override
    public User findById(String id) {
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split("\\|");
                if (data.length < 3) continue;
                if (data[0].equals(id)) {
                    return new User(data[0], data[1], data[2]);
                }
            }
        } catch (FileNotFoundException e) {
            return null;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    // 전체 조회
    public List<User> findAll() {
        List<User> list = new ArrayList<>();
        File file = new File(FILE_PATH);
        if (!file.exists()) return list;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split("\\|");
                if (data.length < 3) continue;
                list.add(new User(data[0], data[1], data[2]));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    // 추가
    public boolean addUser(String id, String pw, String role) {
        if (findById(id) != null) return false;

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_PATH, true))) {
            String newLine = id + "|" + pw + "|" + role;
            bw.write(newLine);
            bw.newLine();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    // 삭제
    public boolean deleteUser(String targetId) {
        List<String> lines = new ArrayList<>();
        boolean found = false;

        try (BufferedReader br = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split("\\|");
                if (data.length > 0 && data[0].equals(targetId)) {
                    found = true;
                } else {
                    lines.add(line);
                }
            }
        } catch (IOException e) {
            return false;
        }

        if (found) {
            saveAllLines(lines);
            return true;
        }
        return false;
    }

    // 수정
    public boolean updateUser(String id, String newPw, String newRole) {
        List<String> lines = new ArrayList<>();
        boolean found = false;

        try (BufferedReader br = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split("\\|");

                if (data.length > 0 && data[0].equals(id)) {
                    String newLine = id + "|" + newPw + "|" + newRole;
                    lines.add(newLine);
                    found = true;
                } else {
                    lines.add(line);
                }
            }
        } catch (IOException e) {
            return false;
        }

        if (found) {
            saveAllLines(lines);
            return true;
        }
        return false;
    }

    // 전체 저장
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
}
