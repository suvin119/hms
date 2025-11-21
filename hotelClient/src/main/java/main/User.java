package main;

/**
 *
 * @author subin
 */
public class User {
    protected String name;
    protected String id;

    public User(String name, String id) {
        this.name = name;
        this.id = id;
    }
    
    // 직원은 관리자 권한이 없음
    public boolean isAdmin() {
        return false;
    }
    
    public String getName() { return name; }
}
