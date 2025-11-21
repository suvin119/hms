package UnitServices;

import java.util.Date;

public class ServiceUsageInfo {
    private int reservationId;
    private int serviceId;
    private int quantity;
    private int cost;
    private Date usageDate;
    private String status;

    public ServiceUsageInfo(int reservationId, int serviceId, int quantity, int cost, Date usageDate, String status) {
        this.reservationId = reservationId;
        this.serviceId = serviceId;
        this.quantity = quantity;
        this.cost = cost;
        this.usageDate = usageDate;
        this.status = status;
    }

    public int getReservationId() { return reservationId; }
    public void setReservationId(int reservationId) { this.reservationId = reservationId; }

    public int getServiceId() { return serviceId; }
    public void setServiceId(int serviceId) { this.serviceId = serviceId; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public int getCost() { return cost; }
    public void setCost(int cost) { this.cost = cost; }

    public Date getUsageDate() { return usageDate; }
    public void setUsageDate(Date usageDate) { this.usageDate = usageDate; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
