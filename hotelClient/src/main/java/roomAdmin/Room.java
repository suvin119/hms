package roomAdmin;

/**
 * 관리자 객실 업데이트 기능
 * @author subin
 */
import java.io.Serializable;

public class Room implements Serializable {
    
    public enum Type {
        STANDARD("스탠다드"), DELUXE("디럭스"), SUITE("스위트");

        private final String label;
        Type(String label) { this.label = label; }
        public String getLabel() { return label; }
    }
    
    
    public enum Status {
        AVAILABLE("사용가능"), RESERVED("예약중"), CLEANING("청소중"), OCCUPIED("투숙중");

        private final String label;
        Status(String label) { this.label = label; }
        public String getLabel() { return label; }
    }
    
    
    private String roomNumber;
    private Type type;
    private double price;
    private Status status;

    public Room(String roomNumber, Type type, double price, Status status) {
        this.roomNumber = roomNumber;
        this.type = type;
        this.price = price;
        this.status = status;
    }

    // Getters
    public String getRoomNumber() { return roomNumber; }
    public double getPrice() { return price; }
    public Status getStatus() { return status; }
    
    // Setters
    public void setPrice(double price) { this.price = price; }
    public void setStatus(Status status) { this.status = status; }

}
    