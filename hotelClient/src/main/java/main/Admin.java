package main;

/**
 *
 * @author subin
 */
public class Admin extends User {
    
    public Admin(String name, String id) {
        super(name, id);
    }

    @Override
    public boolean isAdmin() {
        return true;
    }
}
