package main;

public class Session {
    // 현재 프로그램에 로그인한 사용자
    // 테스트를 위해 기본값을 '관리자'로 설정해둠 (나중에 로그인 화면에서 바꿔줘야 함)
    public static User currentUser = new Admin("관리자홍길동", "admin01");
    
    // 테스트용: 직로 로그인하고 싶으면 주석 풀고 위를 주석 처리
    // public static User currentUser = new User("일반직원", "emp01");
}
