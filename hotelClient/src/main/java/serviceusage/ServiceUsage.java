package serviceusage;

public class ServiceUsage {

    public String roomNumber;
    public String menuId;
    public String menuName;
    public int quantity;
    public int totalPrice;
    public String usedAt;

    public ServiceUsage(String roomNumber, String menuId,
                        String menuName, int quantity,
                        int totalPrice, String usedAt) {
        this.roomNumber = roomNumber;
        this.menuId = menuId;
        this.menuName = menuName;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
        this.usedAt = usedAt;
    }

    public static ServiceUsage fromLine(String line) {
        String[] p = line.split("\\|");
        return new ServiceUsage(
                p[0],        // roomNumber
                p[1],        // menuId
                p[2],        // menuName
                Integer.parseInt(p[3]),  // qty
                Integer.parseInt(p[4]),  // total
                p[5]         // usedAt
        );
    }

    public String toLine() {
        return String.join("|",
                roomNumber,
                menuId,
                menuName,
                String.valueOf(quantity),
                String.valueOf(totalPrice),
                usedAt
        );
    }
    
    public String getRoomNumber() {
        return roomNumber;
    }

    public String getMenuId() {
        return menuId;
    }

    public String getMenuName() {
        return menuName;
    }

    public int getQuantity() {
        return quantity;
    }

    public int getTotalPrice() {
        return totalPrice;
    }
}