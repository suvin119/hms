package Pay;

import java.time.LocalDate;
import java.util.List;

public class BookingInfo {
    private int roomId;
    private String guestName;
    private LocalDate checkInDate;
    private LocalDate plannedCheckOutDate;
    private double baseRoomRate;
    private double promotionalDiscount;
    private List<ExtraChargeInfo> extraCharges;

    // 기존 생성자 (체크인/예약 정보 기반)
    public BookingInfo(int roomId, String guestName, LocalDate checkInDate, LocalDate plannedCheckOutDate,
                       double baseRoomRate, double promotionalDiscount, List<ExtraChargeInfo> extraCharges) {
        this.roomId = roomId;
        this.guestName = guestName;
        this.checkInDate = checkInDate;
        this.plannedCheckOutDate = plannedCheckOutDate;
        this.baseRoomRate = baseRoomRate;
        this.promotionalDiscount = promotionalDiscount;
        this.extraCharges = extraCharges;
    }

    // ✅ 간편 생성자: 체크아웃 화면에서 최소 정보만으로 생성 가능
    public BookingInfo(String guestName, LocalDate plannedCheckOutDate, List<ExtraChargeInfo> extraCharges) {
        this.roomId = 0; // 방 ID 미지정
        this.guestName = guestName;
        this.checkInDate = LocalDate.now(); // 체크인 날짜 기본값: 현재
        this.plannedCheckOutDate = plannedCheckOutDate;
        this.baseRoomRate = 0; // 기본 요금 미지정
        this.promotionalDiscount = 0; // 할인 없음
        this.extraCharges = extraCharges;
    }

    // Getter
    public int getRoomId() { return roomId; }
    public String getGuestName() { return guestName; }
    public LocalDate getCheckInDate() { return checkInDate; }
    public LocalDate getPlannedCheckOutDate() { return plannedCheckOutDate; }
    public double getBaseRoomRate() { return baseRoomRate; }
    public double getPromotionalDiscount() { return promotionalDiscount; }
    public List<ExtraChargeInfo> getExtraCharges() { return extraCharges; }

    // Setter: 부대 서비스 목록 업데이트
    public void setExtraCharges(List<ExtraChargeInfo> extraCharges) {
        this.extraCharges = extraCharges;
    }
}
