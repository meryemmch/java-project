package src.main.java.userManager;

import java.io.*;
import java.util.*;

import src.main.java.model.User;

public class UserManager implements UserOperations {

    private static final String USER_FILE = "resources/users.csv"; 

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
    
        String name;
        do {
            System.out.print("Enter your name: ");
            name = scanner.nextLine();
            if (name.isEmpty()) {
                System.out.println("Name cannot be empty. Please try again.");
            }
        } while (name.isEmpty());
    
        String email;
        Validator emailValidator = new EmailValidator();
        do {
            System.out.print("Enter your email: ");
            email = scanner.nextLine();
            if (!emailValidator.Valid(email)) {
                System.out.println("Invalid email format. Please try again.");
            }
        } while (!emailValidator.Valid(email));
    
        String phoneNumber;
        Validator phoneValidator = new PhoneNumberValidator();
        do {
            System.out.print("Enter your phone number: ");
            phoneNumber = scanner.nextLine();
            if (!phoneValidator.Valid(phoneNumber)) {
                System.out.println("Invalid phone number. Enter a 8-12 digits phone number ");
            }
        } while (!phoneValidator.Valid(phoneNumber));
    
        String username;
        Validator usernameValidator = new UserNameValidator();
        do {
            System.out.print("Enter a username: ");
            username = scanner.nextLine();
            if (!usernameValidator.Valid(username)) {
                System.out.println("Invalid username. Use 3-20 characters: letters, digits, or underscores.");
            } else if (UserManager.isUsernameTaken(username)) {
                System.out.println("Username already taken, try another one.");
                username = null; // Reset to force re-prompt
            }
        } while (username == null);
    
        String password;
        Validator passwordValidator = new PasswordValidator();
        do {
            System.out.print("Enter a password (must contain both letters and digits): ");
            password = scanner.nextLine();
            if (!passwordValidator.Valid(password)) {
                System.out.println("Password must contain both letters and digits.");
            }
        } while (!passwordValidator.Valid(password));
    
        // Create and save new user
        User newUser = new User(name, email, phoneNumber, username, password);
        if (UserManager.saveUser(newUser)) {
            System.out.println("Sign up successful! You can now log in.");
        } else {
            System.out.println("Error while signing up. Please try again later.");
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
            System.out.println("\nWelcome " + user.getUsername() + "!");
            return true;
        } else {
            System.out.println("\nInvalid username or password.");
            return false;
        }
    }

    @Override
    public List<User> getAllUsers() {
        return loadUsers();  // Return the loaded users from the file
    }

}
