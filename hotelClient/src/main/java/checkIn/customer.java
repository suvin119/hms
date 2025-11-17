package checkIn;

/**
 *
 * @author subin
 */
public class customer {
    private String reservationId;
    private String name;
    private String phone;
    
    // 생성자
    public customer(String reservationId, String name, String phone) {
        this.reservationId = reservationId;
        this.name = name;
        this.phone = phone;
    }

    public String getReservationId() { return reservationId; }
    public String getName() { return name; }
    public String getPhone() { return phone; }
}
