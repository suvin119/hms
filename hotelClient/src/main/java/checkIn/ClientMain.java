package checkIn;

/**
 *
 * @author subin
 */
public class ClientMain {
    public static void main(String[] args) {
        // UI 스레드 안전 실행
        javax.swing.SwingUtilities.invokeLater(() -> {
            CheckInView view = new CheckInView();
            new ClientController(view);
        });
    }
}