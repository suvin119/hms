package reservation;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


public class ReservationControllerTest {
    
    public ReservationControllerTest() {
    }

    @Test
    public void testSendRequestToRealServer() {
        System.out.println("======== [서버 통신 테스트] ========");
        
        ReservationController controller = new ReservationController();
        
        String realMsg = "REGISTER_RESERVATION|테스트유저|010-9999-9999|2025-12-25|2025-12-26|스탠다드|1";
        System.out.println("[Client] 서버로 전송: " + realMsg);

        String response = controller.sendRequest(realMsg);
        
        System.out.println("[Client] 서버 응답: " + response);
        
        assertNotNull(response);
        assertNotEquals("ERROR|서버 연결 실패", response);
    }
}