package Pay;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;

/**
 * Server 역할: 결제/회계 계산 및 DB 처리 로직 담당 (Client에서 호출됨)
 */
public class BillingService {
    
    // DB에서 BookingInfo를 불러오는 메서드 (임시 데이터 반환)
    public BookingInfo getBookingInfo(int roomId) {
        if (roomId == 101) {
            LocalDate checkIn = LocalDate.now().minusDays(2);
            LocalDate plannedCheckOut = LocalDate.now().plusDays(1);
            List<ExtraChargeInfo> charges = Arrays.asList(
                new ExtraChargeInfo("Room Service", 20.0),
                new ExtraChargeInfo("Mini Bar", 10.0),
                new ExtraChargeInfo("Laundry", 5.0)
            );
            // 2박 예정 (100원/박 * 2) - 10원 할인 + 부대비용. (총 190원 숙박료)
            return new BookingInfo(101, "김철수", checkIn, plannedCheckOut, 100.0, 10.0, charges);
        }
        return null;
    }

    // 시즌/프로모션을 반영한 숙박 요금 계산
    private double calculateRoomStayCharge(BookingInfo booking) {
        long nights = ChronoUnit.DAYS.between(booking.getCheckInDate(), booking.getPlannedCheckOutDate());
        
        // **시즌 요금 로직 (가정)**: 현재는 '성수기'로 1.2배 할증
        double seasonMultiplier = 1.2;
        
        double totalBaseRate = booking.getBaseRoomRate() * nights * seasonMultiplier;
        
        // 프로모션 할인 반영
        totalBaseRate -= booking.getPromotionalDiscount();
        
        return Math.max(0, totalBaseRate);
    }
    
    // 부대비용 합계 계산
    private double calculateExtraCharges(BookingInfo booking) {
        return booking.getExtraCharges().stream()
                .mapToDouble(ExtraChargeInfo::getAmount)
                .sum();
    }
    
    // 예정일 이후 체크아웃 시 추가 연장 요금 계산
    private double calculateLateCheckOutFee(BookingInfo booking, LocalDate actualCheckOutDate) {
        if (actualCheckOutDate.isAfter(booking.getPlannedCheckOutDate())) {
            long lateDays = ChronoUnit.DAYS.between(booking.getPlannedCheckOutDate(), actualCheckOutDate);
            // 연장 요금: 기본 요금의 1.5배 부과
            return lateDays * booking.getBaseRoomRate() * 1.5; 
        }
        return 0.0;
    }

    /**
     * 최종 청구 금액을 계산하고 반환합니다.
     */
    public double calculateTotalBill(int roomId, LocalDate actualCheckOutDate) {
        BookingInfo booking = getBookingInfo(roomId);
        if (booking == null) return -1.0; 

        double roomCharge = calculateRoomStayCharge(booking);
        double extraChargeTotal = calculateExtraCharges(booking);
        double lateFee = calculateLateCheckOutFee(booking, actualCheckOutDate);

        return roomCharge + extraChargeTotal + lateFee;
    }
    
    /**
     * 부대 서비스 요금을 DB에 추가합니다.
     */
    public boolean addServiceCharge(int roomId, String serviceName, double amount) {
        // 실제로는 DB 트랜잭션: 해당 객실의 예약 정보에 ExtraChargeInfo를 추가합니다.
        System.out.printf("[Server] Room %d에 %s (%.2f원) 부대 비용 추가됨.%n", roomId, serviceName, amount);
        return true; 
    }
    
    /**
     * 체크아웃을 완료하고 DB 상태를 업데이트합니다.
     */
    public boolean processCheckout(int roomId, LocalDate actualCheckOutDate, double finalBill) {
        // 1. 예약 정보의 실제 체크아웃 날짜 업데이트
        // 2. 객실 상태를 '공실'로 변경
        // 3. 회계 장부에 최종 결제 기록 저장
        System.out.printf("[Server] Room %d 체크아웃 완료. 최종 정산 금액: %.2f원. 실제 체크아웃 날짜: %s%n", 
            roomId, finalBill, actualCheckOutDate);
        return true;
    }
}