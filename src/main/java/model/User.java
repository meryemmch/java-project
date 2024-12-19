package src.main.java.model;
 
public class User extends Person {
    private String username;
    private String hashedPassword;

    // Constructor
    public User(String name, String email, String phoneNumber, String username, String hashedPassword) {
        super(name, email, phoneNumber);
        this.username = username;
        this.hashedPassword = hashedPassword;
    }

    // Getters and Setters
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return hashedPassword;
    }

    public void setPassword(String hashedPassword) {
        this.hashedPassword = hashedPassword;
    }
}
