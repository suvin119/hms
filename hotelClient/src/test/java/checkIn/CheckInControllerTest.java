package checkIn;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * 체크인 컨트롤러 - 실제 서버 통신 테스트
 */
public class CheckInControllerTest {
    
    private CheckInController controller;

    public CheckInControllerTest() {
    }
    
    @BeforeAll
    public static void setUpClass() {
        System.out.println("======== [체크인 테스트 시작] ========");
    }
    
    @AfterAll
    public static void tearDownClass() {
        System.out.println("======== [체크인 테스트 종료] ========");
    }
    
    @BeforeEach
    public void setUp() {
        controller = new CheckInController();
    }
    
    @AfterEach
    public void tearDown() {
        System.out.println("------------------------------------");
    }

    /**
     * 1. 예약 조회 테스트 (FIND_RESERVATION)
     */
    @Test
    public void testFindReservation() {
        System.out.println("[1. 예약 조회 테스트]");

        String testId = "R1005"; 
        
        System.out.println("[Client] 예약 조회 요청: " + testId);
        
        String msg = "FIND_RESERVATION|" + testId;
        String response = controller.sendRequest(msg);
        
        System.out.println("[Client] 서버 응답: " + response);

        assertNotNull(response, "서버 응답이 없습니다.");
        assertNotEquals("ERROR|서버 연결 실패", response);

        if(response.startsWith("SUCCESS")) {
            System.out.println(">> 결과: 조회 성공");
        } else {
            System.out.println(">> 결과: 조회 실패 (없는 번호이거나 오류)");
        }
    }

    /**
     * 2. 체크인 확정 테스트 (CONFIRM_CHECKIN)
     */
    @Test
    public void testConfirmCheckIn() {
        System.out.println("[2. 체크인 확정 테스트]");
        
        String testId = "R1005";     
        String testRoom = "103";     
        String testDate = "2025-12-30";
        
        String msg = String.format("CONFIRM_CHECKIN|%s|%s|%s", testId, testRoom, testDate);
        
        System.out.println("[Client] 체크인 요청 전송: " + msg);
        
        String response = controller.sendRequest(msg);
        
        System.out.println("[Client] 서버 응답: " + response);
        
        assertNotNull(response);
        
        if(response.startsWith("SUCCESS")) {
            System.out.println(">> 결과: 체크인 확정 성공");
        } else {
            System.out.println(">> 결과: 체크인 실패 (이미 체크인된 방이거나 오류)");
        }
    }
}