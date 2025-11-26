package main;

/**
 *
 * @author subin
 */

public class User {
    private String id;
    private String password;
    private String role; // "ADMIN" or "STAFF"

    public User(String id, String password, String role) {
        this.id = id;
        this.password = password;
        this.role = role;
    }

    public String getId() { return id; }
    public String getPassword() { return password; }
    public String getRole() { return role; }

    // 권한 체크 로직 수정: role 문자열 확인
    public boolean isAdmin() {
        return "ADMIN".equalsIgnoreCase(this.role);
    }
}