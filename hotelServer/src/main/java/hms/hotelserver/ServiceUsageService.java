package hms.hotelserver;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class ServiceUsageService {

    private static final String FILE_PATH = "src/main/resources/service_usage.txt";
    private static final String MENU_PATH = "src/main/resources/menu.txt";

    // ==========================
    // 내부 도메인 클래스 (DTO)
    // ==========================
    public static class ServiceUsage {
        public String roomNumber;
        public String menuId;
        public int quantity;
        public int totalPrice;
        public String usedAt;

        public ServiceUsage(String roomNumber, String menuId,
                            int quantity, int totalPrice, String usedAt) {
            this.roomNumber = roomNumber;
            this.menuId = menuId;
            this.quantity = quantity;
            this.totalPrice = totalPrice;
            this.usedAt = usedAt;
        }

        public static ServiceUsage fromLine(String line) {
            String[] p = line.split("\\|");
            return new ServiceUsage(
                    p[0],                   // roomNumber
                    p[1],                   // menuId
                    Integer.parseInt(p[2]), // quantity
                    Integer.parseInt(p[3]), // totalPrice
                    p[4]                    // usedAt
            );
        }

        public String toLine() {
            return String.join("|",
                    roomNumber,
                    menuId,
                    String.valueOf(quantity),
                    String.valueOf(totalPrice),
                    usedAt
            );
        }
    }

    // ==================================
    // 식음료 메뉴 정보 읽기
    // ==================================
    private String getMenuName(String menuId) {
        try (BufferedReader br = new BufferedReader(new FileReader(MENU_PATH))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] p = line.split("\\|");
                if (p.length >= 2 && p[0].equals(menuId)) return p[1];
            }
        } catch (Exception ignored) {}
        return "알수없음(" + menuId + ")";
    }

    private int getMenuPrice(String menuId) {
        try (BufferedReader br = new BufferedReader(new FileReader(MENU_PATH))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] p = line.split("\\|");
                if (p.length >= 3 && p[0].trim().equals(menuId.trim())) {
                    return Integer.parseInt(p[2].trim()); 
                }
            }
        } catch (IOException e) { 
            System.out.println("메뉴 가격 조회 실패: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.out.println("가격 데이터 오류: " + e.getMessage());
        }
        
        return 0;
    }

    // ==================================
    // 사용 내역 저장
    // ==================================
    public String process(String command, String[] parts) {

        switch (command) {

            case "SERVICE_ADD_USAGE": {
                if (parts.length < 4) return "ERROR|INVALID_ARGS";
                
                String room = parts[1];
                String menuId = parts[2];
                int qty = Integer.parseInt(parts[3]);

                addUsage(room, menuId, qty);
                return "OK|USAGE_ADDED";
            }

            case "SERVICE_LIST_BY_ROOM": {
                String room = parts[1];
                List<ServiceUsage> list = listByRoom(room);

                if (list.isEmpty()) return "OK|EMPTY";

                StringBuilder sb = new StringBuilder("OK|");

                for (ServiceUsage u : list) {
                    String realName = getMenuName(u.menuId);
                    sb.append(realName).append("|")
                      .append(u.totalPrice).append("#");
                }

                return sb.toString();
            }

            case "SERVICE_TOTAL_BY_ROOM": {
                String room = parts[1];
                int sum = sumByRoom(room);
                return "OK|TOTAL|" + room + "|" + sum;
            }
        }

        return "ERROR|UNKNOWN_COMMAND";
    }

    // -----------------------------
    // addUsage()
    // -----------------------------
    public void addUsage(String roomNumber, String menuId, int qty) {

        int price = getMenuPrice(menuId);
        int total = price * qty;

        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String usedAt = LocalDateTime.now().format(fmt);

        ServiceUsage usage = new ServiceUsage(roomNumber, menuId, qty, total, usedAt);

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_PATH, true))) {
            bw.write(usage.toLine());
            bw.newLine();
        } catch (Exception ignored) {}
    }

    // -----------------------------
    // listByRoom()
    // -----------------------------
    public List<ServiceUsage> listByRoom(String room) {
        List<ServiceUsage> list = new ArrayList<>();

        for (ServiceUsage u : loadAll()) {
            if (u.roomNumber.equals(room)) list.add(u);
        }
        return list;
    }

    // -----------------------------
    // sumByRoom()
    // -----------------------------
    public int sumByRoom(String room) {
        return listByRoom(room).stream()
                .mapToInt(u -> u.totalPrice)
                .sum();
    }

    // -----------------------------
    // loadAll()
    // -----------------------------
    private List<ServiceUsage> loadAll() {
        List<ServiceUsage> list = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = br.readLine()) != null) {
                list.add(ServiceUsage.fromLine(line));
            }
        } catch (Exception ignored) {}

        return list;
    }
}