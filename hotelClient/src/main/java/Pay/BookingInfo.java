package Pay;

import java.time.LocalDate;
import java.util.List;

public class BookingInfo {
    private int roomId;
    private String guestName;
    private LocalDate checkInDate;
    private LocalDate plannedCheckOutDate;
    private double baseRoomRate; // 1박 기준 요금
    private double promotionalDiscount; // 전체 숙박 기간에 대한 고정 할인 금액
    private List<ExtraChargeInfo> extraCharges;

    public BookingInfo(int roomId, String guestName, LocalDate checkInDate, LocalDate plannedCheckOutDate, double baseRoomRate, double promotionalDiscount, List<ExtraChargeInfo> extraCharges) {
        this.roomId = roomId;
        this.guestName = guestName;
        this.checkInDate = checkInDate;
        this.plannedCheckOutDate = plannedCheckOutDate;
        this.baseRoomRate = baseRoomRate;
        this.promotionalDiscount = promotionalDiscount;
        this.extraCharges = extraCharges;
    }

    // Getters
    public int getRoomId() { return roomId; }
    public String getGuestName() { return guestName; }
    public LocalDate getCheckInDate() { return checkInDate; }
    public LocalDate getPlannedCheckOutDate() { return plannedCheckOutDate; }
    public double getBaseRoomRate() { return baseRoomRate; }
    public double getPromotionalDiscount() { return promotionalDiscount; }
    public List<ExtraChargeInfo> getExtraCharges() { return extraCharges; }
}