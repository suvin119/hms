package main;

import Login.UserManageController;
import UnitServices.ChargeInputView;
import UnitServices.ServiceController;
//import report.ReportController;

import javax.swing.*;
import java.awt.event.ActionListener;

public class AdminMenuController {

    private AdminMenuView view;
    private Runnable onBack;
    private Runnable onNavigateToRoomAdmin;

    public AdminMenuController() {
        this.view = new AdminMenuView();
        initListeners();
    }

    private void initListeners() {
        ActionListener commonListener = e -> view.showMessage("해당 관리 기능은 개발 중입니다.");

        // 객실 관리 버튼
        view.setRoomAdminListener(e -> {
            if (onNavigateToRoomAdmin != null) {
                onNavigateToRoomAdmin.run();
            }
        });

        // 부대 서비스 버튼
        view.setServiceListener(e -> {
            //ServiceController menuCtrl = new ServiceController();
            //menuCtrl.showWindow(); 
        });
      
      // 보고서 버튼 -> 보고서 창 띄우기
        view.setReportListener(e -> {
            //ReportController reportCtrl = new ReportController();
            //reportCtrl.showWindow();
        });
        // 직원 관리 버튼 
        view.setUserListener(e -> {
            UserManageController ctrl = new UserManageController(null);
            ctrl.show();
        });
        // 기타 버튼
        view.setCustomerListener(commonListener);
        view.setReportListener(e -> {
            //ReportController reportCtrl = new ReportController();
            //reportCtrl.showWindow(); 
        });

        // 뒤로가기
        view.setBackListener(e -> {
            if (onBack != null) onBack.run();
        });
    }

    public JPanel getView() {
        return view;
    }

    public void setOnBack(Runnable onBack) {
        this.onBack = onBack;
    }

    public void setOnNavigateToRoomAdmin(Runnable action) {
        this.onNavigateToRoomAdmin = action;
    }
}
