/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/*
직원 관리 컨트롤러 추가
*/
package Login;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class UserManageController {

    private final UserService service;
    private final UserManageView view;

    public UserManageController(Frame owner) {
        this.service = new UserService();
        this.view = new UserManageView(owner);
        initListeners();
    }

    private void initListeners() {

        // 등록
        view.setAddListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String id = view.getIdInput();
                String pw = view.getPwInput();
                String role = view.getRoleInput();

                if (id.isEmpty() || pw.isEmpty()) {
                    view.showMessage("ID와 비밀번호를 입력하세요.");
                    return;
                }

                boolean ok = service.registerUser(id, pw, role);
                if (ok) {
                    view.showMessage("직원 등록 완료");
                } else {
                    view.showMessage("이미 존재하는 ID입니다.");
                }
            }
        });

        // 삭제
        view.setDeleteListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String id = view.getIdInput();
                if (id.isEmpty()) {
                    view.showMessage("삭제할 ID를 입력하세요.");
                    return;
                }

                int confirm = JOptionPane.showConfirmDialog(
                        view,
                        "정말 삭제하시겠습니까?",
                        "확인",
                        JOptionPane.YES_NO_OPTION
                );
                if (confirm != JOptionPane.YES_OPTION) return;

                boolean ok = service.removeUser(id);
                if (ok) {
                    view.showMessage("삭제 완료");
                } else {
                    view.showMessage("해당 ID의 직원이 없습니다.");
                }
            }
        });

        // 닫기
        view.setCloseListener(e -> view.dispose());
    }

    public void show() {
        view.setVisible(true);
    }
}

