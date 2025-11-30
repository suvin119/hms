package Login;

import java.util.Scanner;

public class UserTest {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("============== [User 테스트 시작] ==============");
        
        System.out.print(">> 아이디를 입력하세요: ");
        String inputId = scanner.nextLine();

        System.out.print(">> 비밀번호를 입력하세요: ");
        String inputPw = scanner.nextLine();

        System.out.print(">> 직급(Role)을 입력하세요 (예: Manager, Staff): ");
        String inputRole = scanner.nextLine();

        User user = new User(inputId, inputPw, inputRole);

        System.out.println("============== [User 객체 정보 확인] ==============");
        System.out.println("1. 저장된 ID      : " + user.getId());
        System.out.println("2. 저장된 Password: " + user.getPassword());
        System.out.println("3. 저장된 Role    : " + user.getRole());
        System.out.println("==================================================");
        
        if(user.getId() != null && !user.getId().isEmpty()) {
             System.out.println(">> 결과: 데이터가 정상적으로 저장되었습니다.");
        } else {
             System.out.println(">> 결과: 데이터 저장에 실패했습니다.");
        }

        scanner.close();
    }
}