package menuAdmin;

/**
 *
 * @author subin
 */

public class Menu {
    private String menuId;
    private String menuName;
    private int price;

    public Menu(String menuId, String menuName, int price) {
        this.menuId = menuId;
        this.menuName = menuName;
        this.price = price;
    }

    // Getters
    public String getMenuId() { return menuId; }
    public String getMenuName() { return menuName; }
    public int getPrice() { return price; }

    public Object[] toRowData() {
        return new Object[]{menuId, menuName, price};
    }
}