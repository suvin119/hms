/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/*
직원관리 기능 (등록/삭제) 구현
*/
package Login;

public class UserService {

    private final UserRepository repo;

    public UserService(UserRepository repo) {
        this.repo = repo;
    }
        public User authenticate(String id, String password) {
        User user = repo.findById(id);
        if (user == null) {
            return null;
        }
        if (!user.getPassword().equals(password)) {
            return null;
        }
        return user; //로그인 성공
    }

    // 직원 등록
    public boolean registerUser(String id, String password, String role) {
        return repo.addUser(id, password, role);
    }

    // 직원 삭제
    public boolean removeUser(String id) {
        return repo.deleteUser(id);
    }
}

