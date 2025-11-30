package checkOut;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * CheckOutController 원본 수정 없이 테스트용 Mock
 */
public class CheckOutControllerTest {

    private CheckOutController controller;

    @BeforeAll
    public static void setUpClass() {
        System.out.println("======== [체크아웃 테스트 시작] ========");
    }

    @AfterAll
    public static void tearDownClass() {
        System.out.println("======== [체크아웃 테스트 종료] ========");
    }

    @BeforeEach
    public void setUp() {
        // Mock 컨트롤러 생성
        controller = new CheckOutController() {
            public String sendRequest(String msg) {
                // 메시지 유형별로 가짜 응답 반환
                if(msg.startsWith("FIND_RESERVATION")) return "SUCCESS|예약 정보 예시";
                if(msg.startsWith("CALCULATE_BILL")) return "SUCCESS|요금 계산 완료|총액: 120000";
                if(msg.startsWith("CONFIRM_CHECKOUT")) return "SUCCESS|체크아웃 완료";
                return "ERROR|알 수 없는 명령";
            }
        };
    }

    @AfterEach
    public void tearDown() {
        System.out.println("------------------------------------");
    }

    @Test
    public void testFindReservation() {
        System.out.println("[1. 예약 조회 테스트]");
        String msg = "FIND_RESERVATION|R1005";
        String response = controller.sendRequest(msg);
        System.out.println("[Client] 서버 응답: " + response);
    }

    @Test
    public void testCalculateBill() {
        System.out.println("[2. 요금 계산 테스트]");
        String msg = "CALCULATE_BILL|R1005";
        String response = controller.sendRequest(msg);
        System.out.println("[Client] 서버 응답: " + response);
    }

    @Test
    public void testConfirmCheckOut() {
        System.out.println("[3. 체크아웃 확정 테스트]");
        String msg = "CONFIRM_CHECKOUT|R1005|103|2025-12-31";
        String response = controller.sendRequest(msg);
        System.out.println("[Client] 서버 응답: " + response);
    }
}
