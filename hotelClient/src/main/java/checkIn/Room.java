/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package checkIn;

/**
 *
 * @author subin
 */
// Room.java
public class Room {
    private int roomNumber;
    private String guestName;
    private boolean isOccupied;

    public Room(int roomNumber) {
        this.roomNumber = roomNumber;
        this.guestName = null;
        this.isOccupied = false;
    }

    // 체크인 처리
    public void checkIn(String guestName) {
        this.guestName = guestName;
        this.isOccupied = true;
    }

    // 체크아웃 처리 (확장성을 위해 추가)
    public void checkOut() {
        this.guestName = null;
        this.isOccupied = false;
    }

    // Getters
    public int getRoomNumber() { return roomNumber; }
    public String getGuestName() { return guestName; }
    public boolean isOccupied() { return isOccupied; }
}
