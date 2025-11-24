package checkOut;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class Room {

    // 방 상태를 정의하는 Enum (안전한 방식)
    public enum Status {
        EMPTY, OCCUPIED, CLEANING, RESERVED
    }

    private String roomNumber;
    private String type;
    private int price;
    private Status status; // String 대신 Enum 사용

    public Room(String roomNumber, String type, int price, String status) {
        this.roomNumber = roomNumber;
        this.type = type;
        this.price = price;

        // 문자열로 들어온 상태를 Enum으로 변환 (예외 처리 포함)
        try {
            this.status = Status.valueOf(status.toUpperCase());
        } catch (Exception e) {
            this.status = Status.EMPTY; // 오타가 있거나 비어있으면 기본값 EMPTY
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
    // rooms.txt 파일 읽어오기 (안전화 버전)
    // -----------------------------
    public static List<Room> loadRooms() {
        List<Room> rooms = new ArrayList<>();

        try {
            // 파일 읽기
            InputStream is = Room.class.getResourceAsStream("/rooms.txt");

            if (is == null) {
                System.err.println("[ERROR] rooms.txt 파일을 찾을 수 없습니다.");
                return rooms;
            }

            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            String line;
            while ((line = br.readLine()) != null) {

                if (line.trim().isEmpty()) continue; // 빈 줄은 건너뜀

                String[] parts = line.split("\\|");
                if (parts.length < 4) {
                    System.err.println("[WARN] 형식이 잘못된 라인 스킵: " + line);
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
                    System.err.println("[WARN] 데이터 파싱 실패: " + line);
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