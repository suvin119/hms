package Pay;

import java.time.LocalDateTime;

public class ExtraChargeInfo {
    private String serviceName;
    private double amount;
    private LocalDateTime chargeTime;

    public ExtraChargeInfo(String serviceName, double amount) {
        this.serviceName = serviceName;
        this.amount = amount;
        this.chargeTime = LocalDateTime.now();
    }

    // Getters
    public String getServiceName() { return serviceName; }
    public double getAmount() { return amount; }
    public LocalDateTime getChargeTime() { return chargeTime; }
}