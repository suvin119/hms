package Pay;

import java.time.LocalDate;
import java.util.Optional;

/**
 * Controller: View의 요청을 받아 Server Logic을 호출하고 결과를 View에 전달
 */
public class BillingController {
    
    private BillingService server; // Server Logic 호출

    public BillingController() {
        this.server = new BillingService(); 
    }

    public Optional<BookingInfo> getBookingDetails(int roomId) {
        return Optional.ofNullable(server.getBookingInfo(roomId));
    }

    public double calculateFinalBill(int roomId, LocalDate actualCheckOutDate) {
        return server.calculateTotalBill(roomId, actualCheckOutDate);
    }
    
    public boolean processFinalCheckout(int roomId, LocalDate actualCheckOutDate, double finalBill) {
        return server.processCheckout(roomId, actualCheckOutDate, finalBill);
    }
}