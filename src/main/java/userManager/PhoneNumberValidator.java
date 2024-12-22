package src.main.java.userManager;

public class PhoneNumberValidator implements Validator {
    @Override
    public boolean Valid(String phoneNumber) {
        // Validate phone number: digits only, 10-15 characters
        return phoneNumber.matches("^\\d{8,12}$");
    }
}
