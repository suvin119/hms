package Pay;
 
import java.util.*;

public class BillingService {
    private static BillingService instance;
    
    private Map<Integer, BookingInfo> bookings = new HashMap<>();
    private Map<Integer, List<ExtraChargeInfo>> serviceMap = new HashMap<>();
    
    private BillingService() { }

    public static BillingService getInstance() {
        if (instance == null) instance = new BillingService();
        return instance;
    }

    public BookingInfo getBookingInfo(int roomId) {
        return bookings.get(roomId);
    }

    public List<ExtraChargeInfo> getServiceList(int roomId) {
        return serviceMap.getOrDefault(roomId, new ArrayList<>());
    }

    public boolean addServiceCharge(int roomId, String serviceName, double amount) {
        serviceMap.computeIfAbsent(roomId, k -> new ArrayList<>())
                  .add(new ExtraChargeInfo(serviceName, amount));
        return true;
    }

    public double calculateServiceTotal(int roomId) {
        return getServiceList(roomId).stream().mapToDouble(ExtraChargeInfo::getAmount).sum();
    }
    
    
    
    public boolean processCheckout(int roomId, java.time.LocalDate checkOutDate, double finalBill) {
        bookings.remove(roomId);
        serviceMap.remove(roomId);
        return true;
    }
    
    public double calculateTotalBill(int roomId, java.time.LocalDate checkOutDate) {
        BookingInfo info = getBookingInfo(roomId);
        if (info == null) return -1;
        double serviceTotal = calculateServiceTotal(roomId);
        return info.getRoomId() + serviceTotal;
    }

    
}
