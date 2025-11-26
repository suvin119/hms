package Pay;

/**
 * DTO: 부대 서비스 요금 정보를 담는 클래스
 */
public class ExtraChargeInfo {
    private String serviceName;
    private double amount;

    public ExtraChargeInfo(String serviceName, double amount) {
        this.serviceName = serviceName;
        this.amount = amount;
    }

    public String getServiceName() { return serviceName; }
    public double getAmount() { return amount; }
}