package Pay;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class BillingController {

    private BillingService server;

    public BillingController() {
        this.server = BillingService.getInstance();
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

    /** 부대 서비스 목록 가져오기 */
    public List<ExtraChargeInfo> getServiceList(int roomId) {
        return server.getServiceList(roomId);
    }
}
