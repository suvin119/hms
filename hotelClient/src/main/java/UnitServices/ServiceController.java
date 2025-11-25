package UnitServices;

import Pay.BillingService; // Service Controller는 BillingService를 호출하여 부대 비용 추가 요청

/**
 * Controller: 부대 서비스(룸서비스, 미니바 등) 금액 추가 로직 조정
 */
public class ServiceController {
    
    private BillingService server; 

    public ServiceController() {
        // 실제로는 Server에 연결하는 통신 클라이언트 초기화
        this.server = new BillingService(); 
    }

    /**
     * 부대 서비스 요금 추가 요청을 서버에 전달
     */
    public boolean addCharge(int roomId, String serviceName, double amount) {
        if (amount <= 0) {
            System.err.println("추가 금액은 0보다 커야 합니다.");
            return false;
        }
        return server.addServiceCharge(roomId, serviceName, amount);
    }
}