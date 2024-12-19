package src.main.java.userManager;

public class UserNameValidator implements Validator {
    @Override
    public boolean Valid(String username) {
        // Validate username: 3-20 characters, letters, digits, underscores
        return username.matches("^[a-zA-Z0-9_]{3,20}$");
    }
}