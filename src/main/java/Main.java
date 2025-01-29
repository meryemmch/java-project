package src.main.java;

import java.util.List;
import java.util.Scanner;
import src.main.java.userManager.*;
import src.main.java.ReportGenerator.ReportGenerator;
import src.main.java.dataprocessing.CustomerDataProcessor;
import src.main.java.dataprocessing.OrderDataProcessor;
import src.main.java.dataprocessing.ProductDataProcessor;
import src.main.java.model.*;

public class Main {
    
    
    public static void main(String[] args) {
        String customerFilePath = "resources/customers.csv";
        String orderFilePath = "resources/order.csv";
        String productFilePath = "resources/products.csv";
        String outputCustomerFilePath = "resources/outputcustomer.csv"; // File path for storing processed customer data
        String outputOrderFilePath = "resources/outputorder.csv";
        String outputProductFilePath = "resources/outputproduct.csv";
        Scanner scanner = new Scanner(System.in);
        // Start the user authentication menu
        userAuthenticationMenu(scanner,customerFilePath, orderFilePath, productFilePath, outputCustomerFilePath, outputOrderFilePath, outputProductFilePath);
        scanner.close();
    }
    
    // User authentication menu (sign-up, log-in, or exit)
    private static void userAuthenticationMenu(Scanner scanner,String customerFilePath, String orderFilePath, String productFilePath,
                                               String outputCustomerFilePath, String outputOrderFilePath, String outputProductFilePath) {
        
        while (true) {
            System.out.println("1. Sign Up");
            System.out.println("2. Log In");
            System.out.println("3. Log out");
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
                        System.out.println("\nLogin successful!");
                        dataManagementMenu(scanner,customerFilePath, orderFilePath, productFilePath, outputCustomerFilePath, outputOrderFilePath, outputProductFilePath);
                    } else {
                        System.out.println("\nLogin failed. Try again.");
                    }
                    break;
                case 3:
                    // Exit the program
                    System.out.println("\nExiting...");
                    System.out.println("\nLogout completed successfully.");
                    return;
                default:
                    System.out.println("\nInvalid choice. Please try again.");
            }
        }
    }

  // Process and store order data
  private static List<Order> processAndStoreOrderData(String orderFilePath, String outputOrderFilePath) {
    OrderDataProcessor orderManager = new OrderDataProcessor();

    // Step 1: Read the order data
    List<Order> orderData = orderManager.readData(orderFilePath);
    System.out.println("\nData read from file: " + orderData.size() + " orders");

    // Step 2: Remove duplicates
    orderManager.removeDuplicates(orderData);
    System.out.println("After removing duplicates: " + orderData.size() + " orders");

    // Step 3: Remove rows with missing data
    orderData = orderManager.removeRowsWithMissingData(orderData);
    System.out.println("After removing rows with missing data: " + orderData.size() + " orders");

    // Step 4: Get a random sample (returns a sublist)
    orderData = orderManager.getRandomSample(orderData, 200);
    System.out.println("After sampling: " + orderData.size() + " orders");
    System.out.println("\n");

    // Step 5: Analyze the data
    orderManager.analyzeData(orderData);

    // Step 6: Store the processed data
    orderManager.storeData(orderData, outputOrderFilePath);

    return orderData; // Return the processed order data
}

// Process and store customer data
private static List<Customer> processAndStoreCustomerData(String customerFilePath, String outputCustomerFilePath) {
    CustomerDataProcessor customerManager = new CustomerDataProcessor();

    // Step 1: Read the customer data
    List<Customer> customerData = customerManager.readData(customerFilePath);
    System.out.println("\nData read from file: " + customerData.size() + " customers");

    // Step 2: Remove duplicates
    customerManager.removeDuplicates(customerData);
    System.out.println("After removing duplicates: " + customerData.size() + " customers");

    // Step 3: Remove rows with missing data
    customerData = customerManager.removeRowsWithMissingData(customerData);
    System.out.println("After removing rows with missing data: " + customerData.size() + " customers");

    // Step 4: Get a random sample (returns a sublist)
    customerData = customerManager.getRandomSample(customerData, 200);
    System.out.println("After sampling: " + customerData.size() + " customers");
    System.out.println("\n");

    // Step 5: Analyze the data
    customerManager.analyzeData(customerData);

    // Step 6: Store the processed data
    customerManager.storeData(customerData, outputCustomerFilePath);

    return customerData; // Return the processed customer data
}

// Process and store product data
private static List<product> processAndStoreProductData(String productFilePath, String outputProductFilePath) {
    ProductDataProcessor productManager = new ProductDataProcessor();

    // Step 1: Read the product data
    List<product> productData = productManager.readData(productFilePath);
    System.out.println("\nData read from file: " + productData.size() + " products");

    // Step 2: Remove duplicates
    productManager.removeDuplicates(productData);
    System.out.println("After removing duplicates: " + productData.size() + " products");

    // Step 3: Remove rows with missing data
    productData = productManager.removeRowsWithMissingData(productData);
    System.out.println("After removing rows with missing data: " + productData.size() + " products");

    // Step 4: Get a random sample (returns a sublist)
    productData = productManager.getRandomSample(productData, 200);
    System.out.println("After sampling: " + productData.size() + " products");
    System.out.println("\n");

    // Step 5: Analyze the data
    productManager.analyzeData(productData);

    // Step 6: Store the processed data
    productManager.storeData(productData, outputProductFilePath);

    return productData; // Return the processed product data
}

   private static void dataManagementMenu(Scanner scanner,String customerFilePath, String orderFilePath, String productFilePath,
                                       String outputCustomerFilePath, String outputOrderFilePath, String outputProductFilePath) {

    // Declare the data variables as final
    final List<Customer>[] customerData = new List[]{null};
    final List<Order>[] orderData = new List[]{null};
    final List<product>[] productData = new List[]{null};

    while (true) {
        System.out.println("\nData Management Menu:");
        System.out.println("1. Manage Customer Data");
        System.out.println("2. Manage Product Data");
        System.out.println("3. Manage Order Data");
        System.out.println("4. Manage All Data");
        System.out.println("5. Exit");
        System.out.print("Choose an option: ");
        int choice = scanner.nextInt();
        scanner.nextLine(); // Consume the newline character

        switch (choice) {
            case 1:
                if (customerData[0] == null) {
                    customerData[0] = processAndStoreCustomerData(customerFilePath, outputCustomerFilePath);
                }
                askToGenerateReport(scanner,"customer", () -> customerDataReport(customerData[0], outputCustomerFilePath), customerData[0] != null);
                break;
            case 2:
                if (productData[0] == null) {
                    productData[0] = processAndStoreProductData(productFilePath, outputProductFilePath);
                }
                askToGenerateReport(scanner,"product", () -> productDataReport(productData[0], outputProductFilePath), productData[0] != null);
                break;
            case 3:
                if (orderData[0] == null) {
                    orderData[0] = processAndStoreOrderData(orderFilePath, outputOrderFilePath);
                }
                askToGenerateReport(scanner,"order", () -> orderDataReport(orderData[0], outputOrderFilePath), orderData[0] != null);
                break;
            case 4:
                if (customerData[0] == null) {
                    customerData[0] = processAndStoreCustomerData(customerFilePath, outputCustomerFilePath);
                }
                askToGenerateReport(scanner,"customer", () -> customerDataReport(customerData[0], outputCustomerFilePath), customerData[0] != null);

                if (productData[0] == null) {
                    productData[0] = processAndStoreProductData(productFilePath, outputProductFilePath);
                }
                askToGenerateReport(scanner,"product", () -> productDataReport(productData[0], outputProductFilePath), productData[0] != null);

                if (orderData[0] == null) {
                    orderData[0] = processAndStoreOrderData(orderFilePath, outputOrderFilePath);
                }
                askToGenerateReport(scanner,"order", () -> orderDataReport(orderData[0], outputOrderFilePath), orderData[0] != null);
                break;
            case 5:
                System.out.println("\nExiting Data Management Menu...");
                return;
            default:
                System.out.println("\nInvalid choice. Please try again.");
        }
    }
}



    
private static void orderDataReport(List<Order> orderData, String outputOrderFilePath){
        ReportGenerator reportGenerator = new ReportGenerator();
        reportGenerator.generateOrderReport(orderData);
        System.out.println("\nOrder report generation completed.");
};
   
private static void customerDataReport(List<Customer> customerData, String outputCustomerFilePath){
        ReportGenerator reportGenerator = new ReportGenerator();
        reportGenerator.generateCustomerReport(customerData);
        System.out.println("\nCustomer report generation completed.");
};
   
private static void productDataReport(List<product> productData, String outputProductFilePath){
        ReportGenerator reportGenerator = new ReportGenerator();
        reportGenerator.generateProductReport(productData);
        System.out.println("\nProduct report generation completed.");
};


private static void askToGenerateReport(Scanner scanner,String dataType, Runnable generateReportAction, boolean isDataAvailable) {
    if (!isDataAvailable) {
        System.out.println("\nNo data available to generate the report for " + dataType + ".");
        return;
    }
    String choice;
    do {
        System.out.println("\nWould you like to generate a report for " + dataType + " data? (yes/no)");
        choice = scanner.nextLine().trim().toLowerCase();

        if (choice.equals("yes")) {
            System.out.println("\nGenerating " + dataType + " data report...");
            generateReportAction.run(); // Execute the report generation action
            break; // Return to the main menu
        } else if (choice.equals("no")) {
            System.out.println("\nSkipping report generation for " + dataType + " data.");
            break; // Return to the main menu
        } else {
            System.out.println("\nInvalid choice. Please type 'yes' or 'no'.");
        }
    } while (true); // Loop until the user gives a valid input
}
}
