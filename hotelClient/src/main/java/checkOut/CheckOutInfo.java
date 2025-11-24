package checkOut;

public class CheckOutInfo {
    private String reservationId;
    private String customerName;
    private String roomNumber;
    private String checkInDate;
    private String checkOutDate;
    private int serviceTotal;
    private int extraFee;

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
    public int getServiceTotal() { return serviceTotal; }
    public int getExtraFee() { return extraFee; }

    public int getTotalFee() {
        // 총액 = 기본 서비스 금액 + 추가 요금
        return serviceTotal + extraFee;
    }

    public String formatInfo() {
        return "예약 ID: " + reservationId +
               "\n고객 이름: " + customerName +
               "\n객실 번호: " + roomNumber +
               "\n체크인: " + checkInDate +
               "\n체크아웃: " + checkOutDate +
               "\n부대 서비스 금액: " + serviceTotal +
               "\n초과 요금: " + extraFee;
    }
}
