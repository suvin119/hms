package checkIn;

/**
 * @author subin
 */

public class Reservation {
    private String reservationId;
    private String customerName;
    private String phoneNumber;
    private String checkInDate;
    private String checkOutDate;
    private String numGuests;
    private String status;
    private String roomNumber;

    public Reservation(String reservationId, String customerName, String phoneNumber, String checkInDate,
                        String checkOutDate, String numGuests, String status, String roomNumber) {
        this.reservationId = reservationId;
        this.customerName = customerName;
        this.phoneNumber = phoneNumber;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        //this.roomType = roomType;
        this.numGuests = numGuests;
        this.status = status;
        this.roomNumber = roomNumber;
    }

    // Getter
    public String getCustomerName() { return customerName; }
    public String getPeriod() { return checkInDate + " ~ " + checkOutDate; }
    public String getNumGuest() { return numGuests; }
    public String getPhoneNumber() { return phoneNumber; }
    public String getCheckInDate() { return checkInDate; }
    public String getCheckOutDate() { return checkOutDate; }
    public String getRoomNumber() { return roomNumber; }
}
