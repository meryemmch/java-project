package src.main.java.userManager;

import java.io.*;
import java.util.*;

import src.main.java.model.User;

public class UserManager implements UserOperations {

    private static final String USER_FILE = "resources/users.csv"; // Path to user data file

    // Method to load users from file
    public static List<User> loadUsers() {
        List<User> users = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(USER_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] userDetails = line.split(",");
                if (userDetails.length == 5) { // Assuming the file now has 5 columns (name, email, phoneNumber, username, password)
                    // Create User object by passing all required details to the constructor
                    users.add(new User(userDetails[0], userDetails[1], userDetails[2], userDetails[3], userDetails[4]));
                }
            }
        } catch (FileNotFoundException e) {
            System.err.println("User file not found. Creating a new file on the next save.");
        } catch (IOException e) {
            System.err.println("Error reading user file: " + e.getMessage());
        }
        return users;
    }

    // Method to save user to file
    public static boolean saveUser(User user) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(USER_FILE, true))) {
            writer.write(user.getName() + "," + user.getEmail() + "," + user.getPhoneNumber() + "," + user.getUsername() + "," + user.getPassword());
            writer.newLine();
            return true;
        } catch (IOException e) {
            System.err.println("Error saving user data: " + e.getMessage());
            return false;
        }
    }

    // Method to check if username is taken
    public static boolean isUsernameTaken(String username) {
        List<User> users = loadUsers();
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                return true;
            }
        }
        return false;
    }

    // Method to authenticate user
    public static User authenticate(String username, String password) {
        List<User> users = loadUsers();
        for (User user : users) {
            if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                return user;
            }
        }
        return null;
    }
    @Override
  public void signup() {
    Scanner scanner = new Scanner(System.in);

    System.out.print("Enter your name: ");
    String name = scanner.nextLine();

    System.out.print("Enter your email: ");
    String email = scanner.nextLine();
    Validator emailValidator = new EmailValidator();
    if (!emailValidator.Valid(email)) {
        System.out.println("Invalid email format. Please try again.");
        return;
    }

    System.out.print("Enter your phone number: ");
    String phoneNumber = scanner.nextLine();
    Validator phoneValidator = new PhoneNumberValidator();
    if (!phoneValidator.Valid(phoneNumber)) {
        System.out.println("Invalid phone number. Please enter 10-15 digits only.");
        return;
    }

    System.out.print("Enter a username: ");
    String username = scanner.nextLine();
    Validator usernameValidator = new UserNameValidator();
    if (!usernameValidator.Valid(username)) {
        System.out.println("Invalid username. Use 3-20 characters: letters, digits, or underscores.");
        return;
    }

    // Check if username is taken
    if (UserManager.isUsernameTaken(username)) {
        System.out.println("Username already taken, try another one.");
        return;
    }

    System.out.print("Enter a password (must contain both letters and digits): ");
    String password = scanner.nextLine();
    Validator passwordValidator = new PasswordValidator();
    if (!passwordValidator.Valid(password)) {
        System.out.println("Password must contain both letters and digits.");
        return;
    }

    // Create and save new user
    User newUser = new User(name, email, phoneNumber, username, password);
    if (UserManager.saveUser(newUser)) {
        System.out.println("Sign up successful! You can now log in.");
    } else {
        System.out.println("Error while signing up.");
    }
}

   
    @Override
    public boolean login() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter your username: ");
        String username = scanner.nextLine();

        System.out.print("Enter your password: ");
        String password = scanner.nextLine();

        User user = UserManager.authenticate(username, password);
        if (user != null) {
            System.out.println("Welcome " + user.getUsername() + "!");
            return true;
        } else {
            System.out.println("Invalid username or password.");
            return false;
        }
    }

    @Override
    public List<User> getAllUsers() {
        return loadUsers();  // Return the loaded users from the file
    }

}
