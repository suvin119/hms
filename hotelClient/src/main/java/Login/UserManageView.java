/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/*
직원 관리 화면 구현
*/
package Login;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class UserManageView extends JDialog {

    private JTextField txtId = new JTextField(15);
    private JPasswordField txtPw = new JPasswordField(15);
    private JComboBox<String> cbRole =
            new JComboBox<>(new String[] { "ADMIN", "STAFF" });

    private JButton btnAdd = new JButton("등록");
    private JButton btnDelete = new JButton("삭제");
    private JButton btnClose = new JButton("닫기");

    public UserManageView(Frame owner) {
        super(owner, "직원 관리", true); // modal dialog
        setSize(400, 250);
        setLocationRelativeTo(owner);
        setLayout(new BorderLayout());

        JPanel form = new JPanel(new GridLayout(3, 2, 5, 5));
        form.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        form.add(new JLabel("사번(ID):"));
        form.add(txtId);
        form.add(new JLabel("비밀번호:"));
        form.add(txtPw);
        form.add(new JLabel("권한(Role):"));
        form.add(cbRole);

        JPanel buttons = new JPanel();
        buttons.add(btnAdd);
        buttons.add(btnDelete);
        buttons.add(btnClose);

        add(form, BorderLayout.CENTER);
        add(buttons, BorderLayout.SOUTH);
    }

    // 입력 값 getter
    public String getIdInput() {
        return txtId.getText().trim();
    }

    public String getPwInput() {
        return new String(txtPw.getPassword());
    }

    public String getRoleInput() {
        return (String) cbRole.getSelectedItem();
    }

    // 버튼 리스너 등록
    public void setAddListener(ActionListener l) { btnAdd.addActionListener(l); }
    public void setDeleteListener(ActionListener l) { btnDelete.addActionListener(l); }
    public void setCloseListener(ActionListener l) { btnClose.addActionListener(l); }

    public void showMessage(String msg) {
        JOptionPane.showMessageDialog(this, msg);
    }
}
