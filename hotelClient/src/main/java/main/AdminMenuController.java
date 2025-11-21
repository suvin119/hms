package main;

/**
 *
 * @author subin
 */

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JPanel;

public class AdminMenuController {
    
    private AdminMenuView view;
    private Runnable onBack;

    public AdminMenuController() {
        this.view = new AdminMenuView();
        initListeners();
    }

    private void initListeners() {
        ActionListener commonListener = e -> view.showMessage("해당 관리 기능은 개발 중입니다.");

        view.setRoomListener(commonListener);
        view.setCustomerListener(commonListener);
        view.setReportListener(commonListener);
        view.setUserListener(commonListener);
        view.setServiceListener(commonListener);

        view.setBackListener(e -> {
            if (onBack != null) onBack.run();
        });
    }

    public JPanel getView() { return view; }
    public void setOnBack(Runnable onBack) { this.onBack = onBack; }
}