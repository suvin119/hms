package service;

import UnitServices.ServiceController;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ServiceControllerTest {

    private ServiceController controller;
    private final int roomId = 101;

    @BeforeAll
    public void initAll() {
        System.out.println("=== [부대서비스 테스트 시작] ===");
    }

    @AfterAll
    public void tearDownAll() {
        System.out.println("=== [부대서비스 테스트 종료] ===");
    }

    @BeforeEach
    public void init() {
        controller = new ServiceController();
    }

    @AfterEach
    public void tearDown() {
        System.out.println("------------------------------------");
    }

    @Test
    @DisplayName("부대 서비스 요금 추가 및 총액 확인")
    public void testAddChargeAndTotal() {
        System.out.println("[1] 부대 서비스 비용 추가");
        boolean r1 = controller.addCharge(roomId, "룸서비스", 15000);
        boolean r2 = controller.addCharge(roomId, "미니바", 8000);
        boolean r3 = controller.addCharge(roomId, "식당", 38000);

        assertTrue(r1, "룸서비스 추가 실패");
        assertTrue(r2, "미니바 추가 실패");
        assertTrue(r3, "식당 추가 실패");

        System.out.println("[추가 완료]");

        System.out.println("[2] 총 부대 서비스 금액 조회");
        double total = controller.getServiceTotal(roomId);
        System.out.println("총 부대 서비스 금액: " + total + "원");

        assertEquals(61000.0, total, "총 부대 서비스 금액이 예상과 다릅니다.");
    }
}
