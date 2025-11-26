package Pay;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * Server Logic: 실제 DB 접근 및 복잡한 계산을 담당하는 서비스 (테스트용 더미 구현)
 */
public class BillingService {
    
    // 💡 1. BillingController.java [Line 18] 오류 해결
    /** roomId에 대한 더미 BookingInfo를 반환 */
    public BookingInfo getBookingInfo(int roomId) {
        if (roomId == 101) {
            LocalDate checkIn = LocalDate.now().minusDays(3);
            LocalDate plannedCheckOut = LocalDate.now().plusDays(1);
            
            List<ExtraChargeInfo> charges = Arrays.asList(
                new ExtraChargeInfo("미니바 이용", 15000.0),
                new ExtraChargeInfo("세탁 서비스", 25000.0)
            );
            
            return new BookingInfo(
                101, 
                "김철수", 
                checkIn, 
                plannedCheckOut, 
                100000.0, 
                10000.0, 
                charges
            );
        }
        return null;
    }

    // 💡 2. BillingController.java [Line 22] 오류 해결
    /** 최종 요금 계산 (테스트용) */
    public double calculateTotalBill(int roomId, LocalDate actualCheckOutDate) {
        BookingInfo info = getBookingInfo(roomId);
        if (info == null) return 0.0;

        long totalNights = ChronoUnit.DAYS.between(info.getCheckInDate(), info.getPlannedCheckOutDate());
        double totalBaseRate = info.getBaseRoomRate() * totalNights;
        double totalExtraCharges = info.getExtraCharges().stream()
                                    .mapToDouble(ExtraChargeInfo::getAmount).sum();
        double lateFee = 0;
        if (actualCheckOutDate.isAfter(info.getPlannedCheckOutDate())) {
            lateFee = info.getBaseRoomRate(); 
        }

        return (totalBaseRate + totalExtraCharges + lateFee) - info.getPromotionalDiscount();
    }
    
    // 💡 3. BillingController.java [Line 26] 오류 해결
    /** 최종 체크아웃 처리 (테스트용) */
    public boolean processCheckout(int roomId, LocalDate actualCheckOutDate, double finalBill) {
        System.out.println("DEBUG: Room " + roomId + " 최종 결제 금액 " + finalBill + "원 처리 완료.");
        return true; 
    }
    
    // 💡 4. UnitServices/ServiceController.java 오류 해결을 위한 메서드 추가 (이전 오류 로그 기반)
    /** 부대 서비스 요금 추가 처리 (테스트용) */
    public boolean addServiceCharge(int roomId, String serviceName, double amount) {
        System.out.println("DEBUG: Room " + roomId + "에 서비스 '" + serviceName + "' (" + amount + "원) 추가.");
        return true;
    }
}