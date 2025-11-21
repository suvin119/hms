package checkOut;

public class CheckOutInfo {
    private String reservationId;
    private String customerName;
    private String roomNumber;
    private String checkInDate;
    private String checkOutDate;
    private int serviceTotal; // 부대 서비스 금액
    private int extraFee;     // 예정일 초과 시 추가 요금

    public CheckOutInfo(String reservationId, String customerName, String roomNumber,
                        String checkInDate, String checkOutDate,
                        int serviceTotal, int extraFee) {
        this.reservationId = reservationId;
        this.customerName = customerName;
        this.roomNumber = roomNumber;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.serviceTotal = serviceTotal;
        this.extraFee = extraFee;
    }

    public String getReservationId() { return reservationId; }
    public String getCustomerName() { return customerName; }
    public String getRoomNumber() { return roomNumber; }
    public String getCheckInDate() { return checkInDate; }
    public String getCheckOutDate() { return checkOutDate; }
    public int getServiceTotal() { return serviceTotal; }
    public int getExtraFee() { return extraFee; }
}
