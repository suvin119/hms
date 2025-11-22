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
