package menuAdmin;

import java.util.Arrays;

public class MenuTest { 

    public static void main(String[] args) {
        System.out.println("============== [Menu 모델 클래스 테스트] ==============");

        System.out.println("\n1. 메뉴 객체 생성");
        String inputId = "10";
        String inputName = "안심 스테이크";
        int inputPrice = 55000;

        Menu menu = new Menu(inputId, inputName, inputPrice);
        System.out.println(">> Menu 객체 생성");

        System.out.println("\n2. 데이터 조회 (Getter) 테스트");
        System.out.println("   ID   : " + menu.getMenuId());
        System.out.println("   이름 : " + menu.getMenuName());
        System.out.println("   가격 : " + menu.getPrice() + "원");

        if (menu.getMenuId().equals(inputId) && menu.getPrice() == inputPrice) {
            System.out.println("   >> 결과: 데이터가 저장한 값과 일치");
        } else {
            System.err.println("   >> 결과: 데이터가 일치X");
        }

        System.out.println("\n3. 테이블 행 데이터 변환 테스트");
        Object[] rowData = menu.toRowData();
        
        System.out.println("   변환된 배열: " + Arrays.toString(rowData));

        if (rowData.length == 3 && rowData[0].equals(inputId)) {
             System.out.println("   >> 결과: 배열 형태로 변환");
        } else {
             System.out.println("   >> 결과: 배열 변환 실패");
        }

        System.out.println("\n=====================================================");
    }
}