package reservation;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


public class ReservationTest {
    
    private Reservation instance;

    public ReservationTest() {
    }
    
    @BeforeAll
    public static void setUpClass() {
        System.out.println("======== [테스트 시작] ========");
    }
    
    @AfterAll
    public static void tearDownClass() {
        System.out.println("======== [테스트 종료] ========");
    }
    
    @BeforeEach
    public void setUp() {
        instance = new Reservation(
                "R1004",           // ID (출력 제외)
                "손수빈",           // 이름
                "010-7777-7777",   // 연락처
                "2025-11-28",      // 체크인
                "2025-12-01",      // 체크아웃
                "3",               // 인원수
                "예약",             // 상태 (출력 제외)
                "NULL"             // 방번호
        );
    }
    
    @AfterEach
    public void tearDown() {
        System.out.println("-------------------------------");
    }

    @Test
    public void testGetCustomerName() {
        String result = instance.getCustomerName();
        
        System.out.println("[1. 이름 확인]");
        System.out.println("입력한 값 : 손수빈");
        System.out.println("나온 값   : " + result);
    }

    @Test
    public void testGetNumGuest() {
        String result = instance.getNumGuest();
        
        System.out.println("[2. 인원수 확인]");
        System.out.println("입력한 값 : 3");
        System.out.println("나온 값   : " + result);
    }

    @Test
    public void testGetPhoneNumber() {
        String result = instance.getPhoneNumber();
        
        System.out.println("[3. 전화번호 확인]");
        System.out.println("입력한 값 : 010-7777-7777");
        System.out.println("나온 값   : " + result);
    }

    @Test
    public void testGetCheckInDate() {
        String result = instance.getCheckInDate();
        
        System.out.println("[4. 체크인 날짜 확인]");
        System.out.println("입력한 값 : 2025-11-28");
        System.out.println("나온 값   : " + result);
    }

    @Test
    public void testGetCheckOutDate() {
        String result = instance.getCheckOutDate();
        
        System.out.println("[5. 체크아웃 날짜 확인]");
        System.out.println("입력한 값 : 2025-12-01");
        System.out.println("나온 값   : " + result);
    }

    @Test
    public void testGetRoomNumber() {
        String result = instance.getRoomNumber();
        
        System.out.println("[6. 방 번호 확인]");
        System.out.println("입력한 값 : NULL");
        System.out.println("나온 값   : " + result);
    }
}