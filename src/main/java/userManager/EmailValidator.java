package src.main.java.userManager;

public class EmailValidator implements Validator {
    @Override
    public boolean Valid(String email) {
        // Simple email pattern validation
        String emailPattern = "^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$";
        return email.matches(emailPattern);
    }
}
