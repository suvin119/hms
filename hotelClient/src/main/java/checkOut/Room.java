<<<<<<< Updated upstream
package checkOut;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class Room {

    public enum Status {
        EMPTY, OCCUPIED, CLEANING, RESERVED
    }

    private String roomNumber;
    private String type;
    private int price;
    private Status status;

    public Room(String roomNumber, String type, int price, String status) {
        this.roomNumber = roomNumber;
        this.type = type;
        this.price = price;

        try {
            this.status = Status.valueOf(status.toUpperCase());
        } catch (Exception e) {
            this.status = Status.EMPTY; // 기본값
        }
    }

    // Getter / Setter
    public String getRoomNumber() { return roomNumber; }
    public String getType() { return type; }
    public int getPrice() { return price; }
    public Status getStatus() { return status; }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return String.format("%s | %s | %d | %s",
                roomNumber, type, price, status);
    }

    // -----------------------------
    // rooms.txt 파일 읽어오기 (안정화)
    // -----------------------------
    public static List<Room> loadRooms() {
        List<Room> rooms = new ArrayList<>();

        try {
            InputStream is = Room.class.getResourceAsStream("/rooms.txt");

            if (is == null) {
                System.err.println("[ERROR] rooms.txt 파일을 찾을 수 없습니다.");
                return rooms;
            }

            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            String line;
            while ((line = br.readLine()) != null) {

                if (line.trim().isEmpty()) continue; // 빈 줄 스킵

                String[] parts = line.split("\\|");
                if (parts.length < 4) {
                    System.err.println("[WARN] 형식이 잘못된 라인: " + line);
                    continue;
                }

                try {
                    rooms.add(new Room(
                            parts[0].trim(),
                            parts[1].trim(),
                            Integer.parseInt(parts[2].trim()),
                            parts[3].trim()
                    ));
                } catch (Exception e) {
                    System.err.println("[WARN] 라인 파싱 실패: " + line);
                }
            }

            br.close();

        } catch (Exception e) {
            System.err.println("[ERROR] rooms.txt 읽기 중 문제 발생");
            e.printStackTrace();
        }

        return rooms;
    }
}
=======
package checkOut;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class Room {
    private String roomNumber;
    private String type;
    private int price;
    private String status;

    public Room(String roomNumber, String type, int price, String status) {
        this.roomNumber = roomNumber;
        this.type = type;
        this.price = price;
        this.status = status;
    }

    public String getRoomNumber() { return roomNumber; }
    public String getType() { return type; }
    public int getPrice() { return price; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    @Override
    public String toString() {
        return roomNumber + " | " + type + " | " + price + " | " + status;
    }

    // rooms.txt 읽어오기
    public static List<Room> loadRooms() {
        List<Room> rooms = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(Room.class.getResourceAsStream("/rooms.txt")))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split("\\|");
                rooms.add(new Room(
                        parts[0].trim(),
                        parts[1].trim(),
                        Integer.parseInt(parts[2].trim()),
                        parts[3].trim()
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rooms;
    }
}
>>>>>>> Stashed changes
