package roomAdmin;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Room 모델 및 Enum 테스트 (값 출력 확인용)
 */
public class RoomTest {
    
    public RoomTest() {
    }
    
    @BeforeEach
    public void setUp() {
    }
    
    @AfterEach
    public void tearDown() {
        System.out.println("------------------------------------------");
    }

    /**
     * 1. Room 객체가 제대로 생성되고 값이 저장되는지 확인
     */
    @Test
    public void testRoomCreation() {
        System.out.println("======== [1. Room 객체 생성 테스트] ========");
        
        Room room = new Room("505", Room.Type.DELUXE, 150000.0, Room.Status.AVAILABLE);
        
        System.out.println("[입력값] 방번호: 505, 타입: DELUXE, 가격: 150000, 상태: AVAILABLE");
        System.out.println(" >> 객체 생성 완료");
        
        System.out.println("1) 저장된 방번호 : " + room.getRoomNumber());
        assertEquals("505", room.getRoomNumber());
        
        System.out.println("2) 저장된 타입   : " + room.getType());
        assertEquals(Room.Type.DELUXE, room.getType());
        
        System.out.println("3) 저장된 가격   : " + room.getPrice());
        assertEquals(150000.0, room.getPrice());
        
        System.out.println("4) 저장된 상태   : " + room.getStatus());
        assertEquals(Room.Status.AVAILABLE, room.getStatus());
    }

    /**
     * 2. 타입 한글 변환 ("스탠다드" -> STANDARD)
     */
    @Test
    public void testTypeFromKor() {
        System.out.println("======== [2. 타입 한글 변환 테스트] ========");

        String input1 = "스탠다드";
        Room.Type result1 = Room.Type.fromKor(input1);
        System.out.println("입력: " + input1 + " \t-> 변환결과: " + result1);
        assertEquals(Room.Type.STANDARD, result1);

        String input2 = "디럭스";
        Room.Type result2 = Room.Type.fromKor(input2);
        System.out.println("입력: " + input2 + " \t-> 변환결과: " + result2);
        assertEquals(Room.Type.DELUXE, result2);
        
        // 실패 테스트
        String input3 = "실패";
        Room.Type result3 = Room.Type.fromKor(input3);
        System.out.println("입력: " + input3 + " \t\t-> 변환결과: " + result3);
        assertNull(result3);
    }

    /**
     * 3. 상태 한글 변환 ("사용가능" -> AVAILABLE)
     */
    @Test
    public void testStatusFromKor() {
        System.out.println("======== [3. 상태 한글 변환 테스트] ========");
        
        String input1 = "사용가능";
        Room.Status result1 = Room.Status.fromKor(input1);
        System.out.println("입력: " + input1 + " \t-> 변환결과: " + result1);
        assertEquals(Room.Status.AVAILABLE, result1);

        String input2 = "투숙중";
        Room.Status result2 = Room.Status.fromKor(input2);
        System.out.println("입력: " + input2 + " \t-> 변환결과: " + result2);
        assertEquals(Room.Status.OCCUPIED, result2);

        String input3 = "청소중";
        Room.Status result3 = Room.Status.fromKor(input3);
        System.out.println("입력: " + input3 + " \t-> 변환결과: " + result3);
        assertEquals(Room.Status.CLEANING, result3);
        
        String input4 = "공사중";
        Room.Status result4 = Room.Status.fromKor(input4);
        System.out.println("입력: " + input4 + " \t-> 변환결과: " + result4);
        assertNull(result4);
    }
}