package customers;

import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * CustomersController - 실제 서버 통신 테스트
 */
public class CustomersControllerTest {
    
    private CustomersController controller;

    public CustomersControllerTest() {
    }
    
    @BeforeEach
    public void setUp() {
        controller = new CustomersController();
    }
    
    @AfterEach
    public void tearDown() {
        System.out.println("----------------------------------------");
    }


    // 1. 고객 리스트 조회 및 파싱 테스트 (CUSTOMERS_LIST)
    @Test
    public void testGetCustomerList() {
        System.out.println("[1. 고객 목록 조회 테스트]");
        
        List<String> result = controller.sendRequestForList("CUSTOMERS_LIST");
        
        System.out.println("[Client] 파싱된 데이터 개수: " + result.size());
        
        if (result.size() > 0) {
            System.out.println("[Client] 첫 번째 데이터: " + result.get(0));
        } else {
            System.out.println("[Client] 데이터가 없거나(0명), 응답 형식이 다릅니다.");
        }

        assertNotNull(result);
    }

    // 2. 신규 고객 동기화 테스트 (SYNC_NEW_CUSTOMERS)
    @Test
    public void testSyncNewCustomers() {
        System.out.println("[2. 신규 고객 동기화 테스트]");
        
        String response = controller.sendRequest("SYNC_NEW_CUSTOMERS");
        
        System.out.println("[Client] 서버 응답: " + response);
        
        assertNotNull(response);
        assertFalse(response.startsWith("ERROR"));
    }

    // 3. 등급 변경 테스트 (UPDATE_GRADE)
    @Test
    public void testUpdateGrade() {
        System.out.println("[3. 등급 변경 테스트]");

        String testName = "홍상민";
        String testPhone = "010-7777-7777";
        String newGrade = "일반";
        
        String msg = "UPDATE_GRADE|" + testName + "|" + testPhone + "|" + newGrade;
        
        System.out.println("[Client] 전송: " + msg);
        
        String response = controller.sendRequest(msg);
        
        System.out.println("[Client] 응답: " + response);
        
        assertNotNull(response);
        
        if (response.startsWith("SUCCESS")) {
            System.out.println(">> 결과: 등급 변경 성공");
        } else {
            System.out.println(">> 결과: 변경 실패");
        }
    }
}