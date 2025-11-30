package UnitServices;

import Pay.BillingService;

public class ServiceController {
    private BillingService server;
    
    

    public ServiceController() {
        this.server = BillingService.getInstance();
    }

    public boolean addCharge(int roomId, String serviceName, double amount) {
        if (amount <= 0) return false;
        return server.addServiceCharge(roomId, serviceName, amount);
    }

    public double getServiceTotal(int roomId) {
        return server.calculateServiceTotal(roomId);
    }
    

}
