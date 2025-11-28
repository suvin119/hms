package roomAdmin;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * 객실 관리 컨트롤러 - 실제 서버 통신 테스트
 */
public class RoomAdminControllerTest {
    
    private RoomAdminController controller;

    public RoomAdminControllerTest() {
    }
    
    @BeforeEach
    public void setUp() {
        controller = new RoomAdminController();
    }
    
    @AfterEach
    public void tearDown() {
        System.out.println("----------------------------------------");
    }


    // 1. 객실 목록 조회 테스트 (ROOM_LIST)
    @Test
    public void testGetRoomList() {
        System.out.println("[1. 객실 목록 조회 테스트]");
        
        String response = controller.sendRequest("ROOM_LIST");
        
        System.out.println("[Client] 서버 응답: " + response);

        assertNotNull(response);
        assertFalse(response.startsWith("ERROR"), "서버 연결 실패 또는 에러 발생");
        
        if (!response.equals("EMPTY")) {
            System.out.println(">> 결과: 목록 가져오기 성공");
        } else {
            System.out.println(">> 결과: 데이터 없음 (EMPTY)");
        }
    }

     // 2. 객실 추가 테스트 (ROOM_ADD)
    @Test
    public void testAddRoom() {
        System.out.println("[2. 객실 추가 테스트]");
        
        String roomNum = "999";
        String type = "스탠다드";
        String price = "50000";
        String status = "사용가능";
        
        String msg = String.format("ROOM_ADD|%s|%s|%s|%s", roomNum, type, price, status);
        
        System.out.println("[Client] 전송: " + msg);
        
        String response = controller.sendRequest(msg);
        System.out.println("[Client] 응답: " + response);
        
        assertNotNull(response);
    }

    // 3. 요금 수정 테스트 (ROOM_UPDATE_PRICE)
    @Test
    public void testUpdatePrice() {
        System.out.println("[3. 요금 수정 테스트]");
        
        String roomNum = "999";
        String newPrice = "88000"; // 가격 변경
        
        String msg = "ROOM_UPDATE_PRICE|" + roomNum + "|" + newPrice;
        
        System.out.println("[Client] 전송: " + msg);
        
        String response = controller.sendRequest(msg);
        System.out.println("[Client] 응답: " + response);
        
        assertNotNull(response);
    }

     //4. 상태 수정 테스트 (ROOM_UPDATE_STATUS)
    @Test
    public void testUpdateStatus() {
        System.out.println("[4. 상태 수정 테스트]");
        
        String roomNum = "999";
        String newStatus = "청소중";
        
        String msg = "ROOM_UPDATE_STATUS|" + roomNum + "|" + newStatus;
        
        System.out.println("[Client] 전송: " + msg);
        
        String response = controller.sendRequest(msg);
        System.out.println("[Client] 응답: " + response);
        
        assertNotNull(response);
    }
}