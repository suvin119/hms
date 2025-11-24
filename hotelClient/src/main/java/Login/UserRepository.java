/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/*
직원등록/삭제 인터페이스
*/
package Login;

public interface UserRepository {
    // 로그인 / 조회용
    User findById(String id);

    // 직원 등록
    boolean addUser(String id, String password, String role);

    // 직원 삭제
    boolean deleteUser(String id);
}
