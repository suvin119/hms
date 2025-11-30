package serviceusage;

public class ServiceUsageTest {

    public static void main(String[] args) {
        System.out.println("============== [ServiceUsage 모델 테스트] ==============");

        System.out.println("\n1. 객체 생성 및 정보 조회 테스트");
        ServiceUsage usage1 = new ServiceUsage("201", "2", "스테이크", 2, 400000, "2025-11-30");
        
        System.out.println("   객실 번호 : " + usage1.getRoomNumber());
        System.out.println("   메뉴 이름 : " + usage1.getMenuName());
        System.out.println("   주문 수량 : " + usage1.getQuantity());
        System.out.println("   총 금액   : " + usage1.getTotalPrice());

        System.out.println("\n2. toLine() 테스트 (객체 -> 문자열 변환)");
        String line = usage1.toLine();
        System.out.println("   변환 결과 : " + line);
        
        if (line.equals("201|2|스테이크|2|40000|2025-11-30")) {
            System.out.println("   >> 결과: 포맷이 정확합니다.");
        } else {
            System.out.println("   >> 결과: 포맷이 예상과 다릅니다.");
        }

        System.out.println("\n3. fromLine() 테스트 (문자열 -> 객체 변환)");
        String inputData = "305|5|콜라|3|9000|2025-12-01";
        System.out.println("   입력 데이터: " + inputData);

        try {
            ServiceUsage usage2 = ServiceUsage.fromLine(inputData);
            System.out.println("   >> 파싱(Parsing) 성공!");
            System.out.println("   객실 번호 : " + usage2.getRoomNumber());
            System.out.println("   메뉴 ID   : " + usage2.getMenuId());
            System.out.println("   총 금액   : " + usage2.getTotalPrice());

            if(usage2.getTotalPrice() == 9000 && usage2.getQuantity() == 3) {
                 System.out.println("   >> 결과: 데이터가 완벽하게 복원되었습니다.");
            }
            
        } catch (Exception e) {
            System.err.println("   >> [오류] 파싱 중 문제가 발생했습니다: " + e.getMessage());
            e.printStackTrace();
        }
        
        System.out.println("\n========================================================");
    }
}