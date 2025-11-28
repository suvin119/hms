package customers;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Customers 모델 테스트
 */
public class CustomersTest {
    
    public CustomersTest() {
    }
    
    @BeforeEach
    public void setUp() {
    }
    
    @AfterEach
    public void tearDown() {
        System.out.println("----------------------------------------");
    }

    // 1. 기본 생성자 테스트 (int 방문횟수)
    @Test
    public void testStandardConstructor() {
        System.out.println("======== [1. 기본 생성자(int) 테스트] ========");

        String name = "홍상민";
        String phone = "010-1111-2222";
        String grade = "VIP";
        int count = 15;
        
        Customers customer = new Customers(name, phone, grade, count);
        
        System.out.println("입력값: " + name + ", " + phone + ", " + grade + ", " + count);
        
        // 검증
        assertEquals(name, customer.getName());
        assertEquals(grade, customer.getGrade());
        assertEquals(count, customer.getVisitCount());
        
        System.out.println(">> 결과: 객체 생성 및 값 저장 성공");
    }


    //  2. 보조 생성자 테스트 (String 방문횟수 -> int 변환)
    @Test
    public void testStringConstructorSuccess() {
        System.out.println("======== [2. 보조 생성자(String) 테스트] ========");
        
        String countStr = "10";
        Customers customer = new Customers("손수빈", "010-3333-4444", "일반", countStr);
        
        System.out.println("입력된 방문횟수(String): \"" + countStr + "\"");
        System.out.println("변환된 방문횟수(int)   : " + customer.getVisitCount());

        assertEquals(10, customer.getVisitCount());
    }

    // 4. toRowData 테스트 (JTable용 배열 변환)
    @Test
    public void testToRowData() {
        System.out.println("======== [4. 테이블 행 데이터 변환 테스트] ========");
        
        Customers customer = new Customers("김민경", "010-7777-8888", "블랙리스트", 100);
        Object[] row = customer.toRowData();
        
        assertEquals(4, row.length);
        
        System.out.print("변환된 배열: [ ");
        for (Object o : row) {
            System.out.print(o + " ");
        }
        System.out.println("]");

        assertEquals("김민경", row[0]);
        assertEquals("010-7777-8888", row[1]);
        assertEquals("블랙리스트", row[2]);
        assertEquals(100, row[3]);
    }
}