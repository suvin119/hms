/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package checkIn;

/**
 *
 * @author subin
 */
public class NewMain {
    public static void main(String[] args) {
        // UI는 이벤트 스레드에서 실행하는 것이 안전합니다.
        javax.swing.SwingUtilities.invokeLater(() -> {
            ConsoleView view = new ConsoleView();
            new HotelController(view);
        });
    }
}
