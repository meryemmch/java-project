package src.main.java;

import java.util.List;
import java.util.Scanner;
import src.main.java.userManager.*;
import src.main.java.dataprocessingtest.CustomerDataProcessor;
import src.main.java.dataprocessingtest.OrderDataProcessor;
import src.main.java.dataprocessingtest.ProductDataProcessor;
import src.main.java.model.*;

public class Main {

    public static void main(String[] args) {
        // Define file paths for the data files
        String customerFilePath = "resources/customers.csv";
        String orderFilePath = "resources/order.csv";
        String productFilePath = "resources/products.csv";
        String outputCustomerFilePath = "resources/outputcustomer.csv"; // File path for storing processed customer data
        String outputOrderFilePath = "resources/outputorder.csv";
        String outputProductFilePath = "resources/outputproduct.csv";
        
        // Start the user authentication menu
        userAuthenticationMenu(customerFilePath, orderFilePath, productFilePath, outputCustomerFilePath, outputOrderFilePath, outputProductFilePath);
        dataManagementMenu(customerFilePath,orderFilePath, productFilePath,outputCustomerFilePath, outputOrderFilePath, outputProductFilePath);
    }
    
    // User authentication menu (sign-up, log-in, or exit)
    private static void userAuthenticationMenu(String customerFilePath, String orderFilePath, String productFilePath,
                                               String outputCustomerFilePath, String outputOrderFilePath, String outputProductFilePath) {
        Scanner scanner = new Scanner(System.in);
        
        while (true) {
            System.out.println("1. Sign Up");
            System.out.println("2. Log In");
            System.out.println("3. Exit");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume the newline character

            UserManager userManager = new UserManager();

            switch (choice) {
                case 1:
                    // Sign up process
                    userManager.signup();
                    break;
                case 2:
                    // Log in process
                    if (userManager.login()) {
                        System.out.println("Login successful!");
                        dataManagementMenu(customerFilePath, orderFilePath, productFilePath, outputCustomerFilePath, outputOrderFilePath, outputProductFilePath);
                    } else {
                        System.out.println("Login failed. Try again.");
                    }
                    break;
                case 3:
                    // Exit the program
                    System.out.println("Exiting...");
                    scanner.close();
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    // Method to process and store customer data
     // Method to process and store customer data
private static void processAndStoreCustomerData(String customerFilePath, String outputCustomerFilePath) {
    CustomerDataProcessor customerManager = new CustomerDataProcessor();
    
    // Step 1: Read the customer data
    List<Customer> customerData = customerManager.readData(customerFilePath);
    System.out.println("Data read from file: " + customerData.size() + " customers");
    
    // Step 2: Remove duplicates
    customerManager.removeDuplicates(customerData); // Modify in place
    System.out.println("After removing duplicates: " + customerData.size() + " customers");
    
    // Step 3: Remove rows with missing data
    customerData = customerManager.removeRowsWithMissingData(customerData); // Returns new list
    System.out.println("After removing rows with missing data: " + customerData.size() + " customers");
    
    // Step 4: Get a random sample (returns a sublist)
    customerData = customerManager.getRandomSample(customerData, 200); // Make sure the sample is returned
    System.out.println("After sampling: " + customerData.size() + " customers");
    
    // Step 5: Analyze the data
    customerManager.analyzeData(customerData);
    
    // Step 6: Store the processed data
    customerManager.storeData(customerData, outputCustomerFilePath);
}

// Method to process and store order data
private static void processAndStoreOrderData(String orderFilePath, String outputOrderFilePath) {
    OrderDataProcessor orderManager = new OrderDataProcessor();
    
    // Step 1: Read the order data
    List<Order> orderData = orderManager.readData(orderFilePath);
    System.out.println("Data read from file: " + orderData.size() + " orders");
    
    // Step 2: Remove duplicates
    orderManager.removeDuplicates(orderData);
    System.out.println("After removing duplicates: " + orderData.size() + " orders");
    
    // Step 3: Remove rows with missing data
    orderData = orderManager.removeRowsWithMissingData(orderData);
    System.out.println("After removing rows with missing data: " + orderData.size() + " orders");
    
    // Step 4: Get a random sample (returns a sublist)
    orderData = orderManager.getRandomSample(orderData, 200);
    System.out.println("After sampling: " + orderData.size() + " orders");
    
    // Step 5: Analyze the data
    orderManager.analyzeData(orderData);
    
    // Step 6: Store the processed data
    orderManager.storeData(orderData, outputOrderFilePath);
}

// Method to process and store product data
private static void processAndStoreProductData(String productFilePath, String outputProductFilePath) {
    ProductDataProcessor productManager = new ProductDataProcessor();
    
    // Step 1: Read the product data
    List<product> productData = productManager.readData(productFilePath);
    System.out.println("Data read from file: " + productData.size() + " products");
    
    // Step 2: Remove duplicates
    productManager.removeDuplicates(productData);
    System.out.println("After removing duplicates: " + productData.size() + " products");
    
    // Step 3: Remove rows with missing data
    productData = productManager.removeRowsWithMissingData(productData);
    System.out.println("After removing rows with missing data: " + productData.size() + " products");
    
    // Step 4: Get a random sample (returns a sublist)
    productData = productManager.getRandomSample(productData, 200);
    System.out.println("After sampling: " + productData.size() + " products");
    
    // Step 5: Analyze the data
    productManager.analyzeData(productData);
    
    // Step 6: Store the processed data
    productManager.storeData(productData, outputProductFilePath);
}


    // Data management menu
    private static void dataManagementMenu(String customerFilePath, String orderFilePath, String productFilePath,
                                           String outputCustomerFilePath, String outputOrderFilePath, String outputProductFilePath) {
        Scanner scanner = new Scanner(System.in);
        
        while (true) {
            System.out.println("\nData Management Menu:");
            System.out.println("1. Manage Customer Data");
            System.out.println("2. Manage Product Data");
            System.out.println("3. Manage Order Data");
            System.out.println("4. Manage All Data");
            System.out.println("5. Log Out");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume the newline character
            
            switch (choice) {
                case 1:
                    processAndStoreCustomerData(customerFilePath, outputCustomerFilePath);
                    break;
                case 2:
                    processAndStoreProductData(productFilePath, outputProductFilePath);
                    break;
                case 3:
                    processAndStoreOrderData(orderFilePath, outputOrderFilePath);
                    break;
                case 4:
                    processAndStoreCustomerData(customerFilePath, outputCustomerFilePath);
                    processAndStoreProductData(productFilePath, outputProductFilePath);
                    processAndStoreOrderData(orderFilePath, outputOrderFilePath);
                    break;
                case 5:
                    System.out.println("Exiting Data Management Menu...");
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }
}
