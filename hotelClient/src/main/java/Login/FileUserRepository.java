/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Login;

import java.util.Arrays;
import java.util.List;

public class FileUserRepository implements UserRepository {
    
    //임시 유저 목록 (나중에 파일 연동으로 바꿔줄 수 있음.)
    private static final List<User> users = Arrays.asList(
            new User("admin", "1234", "admin"),
            new User("staff", "1111", "staff")
    );
    
    @Override
    public User findById(String id) {
        return users.stream()
                .filter(u -> u.getId().equals(id))
                .findFirst()
                .orElse(null);
    }
}
