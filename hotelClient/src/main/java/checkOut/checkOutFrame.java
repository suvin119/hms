package checkOut;

import UnitServices.ServiceDAO;
import javax.swing.*;

public class checkOutFrame extends JFrame {
    public checkOutFrame() {
        setTitle("체크아웃");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        CheckOutView view = new CheckOutView();
        ServiceDAO dao = new ServiceDAO();
        new CheckOutController(view, dao);
        add(view);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public static void main(String[] args) {
        new checkOutFrame();
    }
}
