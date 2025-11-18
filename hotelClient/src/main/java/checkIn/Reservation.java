/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package checkIn;

/**
 *
 * @author subin
 */

public class Reservation {
    private String reservationId;
    private String customerName;
    private String phoneNumber;
    private String checkInDate;
    private String checkOutDate;
    private String roomType;
    private String status;

    public Reservation(String reservationId, String customerName, String phoneNumber, 
                       String checkInDate, String checkOutDate, String roomType, String status) {
        this.reservationId = reservationId;
        this.customerName = customerName;
        this.phoneNumber = phoneNumber;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.roomType = roomType;
        this.status = status;
    }

    // Getter
    public String getCustomerName() { return customerName; }
    public String getRoomType() { return roomType; }
    public String getPeriod() { return checkInDate + " ~ " + checkOutDate; }
}
