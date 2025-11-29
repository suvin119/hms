package hms.hotelserver;

/**
 *
 * @author subin
 */

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class MenuAdminService {
    private static final String FILE_PATH = "src/main/resources/menu.txt";

    public MenuAdminService() {
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            try {
                file.getParentFile().mkdirs();
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public String processRequest(String request) {
        String[] parts = request.split("\\|");
        String command = parts[0];

        synchronized (this) {
            switch (command) {
                case "MENU_ADD":
                    return addMenu(parts);
                case "MENU_LIST":
                    return getMenuList();
                case "MENU_UPDATE":
                    return updateMenu(parts);
                case "MENU_DELETE":
                    return deleteMenu(parts);
                default:
                    return "FAIL|알 수 없는 명령어";
            }
        }
    }

    // 1. 메뉴 등록
    private String addMenu(String[] data) {
        if (data.length < 4) return "FAIL|데이터 부족";

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_PATH, true))) {
            String id = data[1];
            String name = data[2];
            String price = data[3];

            String line = id + "|" + name + "|" + price;
            bw.write(line);
            bw.newLine(); // 줄바꿈
            return "SUCCESS";
        } catch (IOException e) {
            e.printStackTrace();
            return "FAIL|파일 쓰기 오류";
        }
    }

    // 2. 메뉴 목록 조회
    private String getMenuList() {
        StringBuilder sb = new StringBuilder(); 
        
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    sb.append(line).append("/"); 
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return "FAIL|파일 읽기 오류";
        }
        
        return sb.length() > 0 ? sb.toString() : "SUCCESS|데이터 없음";
    }

    // 3. 메뉴 수정
    private String updateMenu(String[] data) {
        if (data.length < 4) return "FAIL|데이터 부족";
        
        String targetId = data[1];
        String newName = data[2];
        String newPrice = data[3];
        
        List<String> lines = new ArrayList<>();
        boolean found = false;

        try (BufferedReader br = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] row = line.split("\\|");
                if (row.length > 0 && row[0].equals(targetId)) {
                    lines.add(targetId + "|" + newName + "|" + newPrice);
                    found = true;
                } else {
                    lines.add(line);
                }
            }
        } catch (IOException e) {
            return "FAIL|파일 읽기 오류";
        }

        if (!found) return "FAIL|해당 ID의 메뉴를 찾을 수 없음";
        
        return rewriteFile(lines);
    }

    // 4. 메뉴 삭제
    private String deleteMenu(String[] data) {
        if (data.length < 2) return "FAIL|데이터 부족";

        String targetId = data[1];
        List<String> lines = new ArrayList<>();
        boolean found = false;

        try (BufferedReader br = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] row = line.split("\\|");
                if (row.length > 0 && row[0].equals(targetId)) {
                    found = true;
                } else {
                    lines.add(line);
                }
            }
        } catch (IOException e) {
            return "FAIL|파일 읽기 오류";
        }

        if (!found) return "FAIL|삭제할 ID가 존재하지 않음";

        return rewriteFile(lines);
    }

    
    private String rewriteFile(List<String> lines) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_PATH))) {
            for (String line : lines) {
                bw.write(line);
                bw.newLine();
            }
            return "SUCCESS";
        } catch (IOException e) {
            return "FAIL|파일 저장 오류";
        }
    }
}
