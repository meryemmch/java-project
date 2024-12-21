package src.main.java.dataprocessingtest;


import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import src.main.java.model.Customer;

public class CustomerDataProcessor extends DataProcessor<Customer> {

    // Parse each line of the CSV to create a Customer object
    @Override
    protected Customer parseLine(String line) {
    String[] values = line.trim().split(",");
    
    // Check if the line has the correct number of columns (9 values for a customer)
    if (values.length == 9) {
        try {
            return new Customer(values[0], values[1], values[2], values[3], values[4], values[5], values[7],values[8]);
        } catch (Exception e) {
            // Handle any potential parsing errors (e.g., if the data types are wrong)
            System.out.println("Error parsing line: " + line);
            e.printStackTrace();
        }
    }
    return null;  // Return null if the line doesn't match the expected format
}

        @Override
    public boolean isValid(Customer customer) {
        // A valid customer must have a non-null and non-empty customerId, email, and name
        return customer != null &&
               customer.getCustomerId() != null && !customer.getCustomerId().isEmpty() &&
               customer.getEmail() != null && !customer.getEmail().isEmpty() &&
               customer.getName() != null && !customer.getName().isEmpty();
    }

    // Override removeDuplicates to remove duplicate customers based on the customerId
    @Override
    public void removeDuplicates(List<Customer> data) {
        Set<String> uniqueIds = new HashSet<>();
        List<Customer> uniqueData = data.stream()
                                        .filter(customer -> customer != null && uniqueIds.add(customer.getCustomerId()))
                                        .collect(Collectors.toList());
        data.clear();  // Clear the original list
        data.addAll(uniqueData);  // Add the unique customers back to the list
        System.out.println("Processed " + data.size() + " unique customers.");
    }


    // Convert a Customer object to a CSV string
    @Override
    protected String convertToCSV(Customer item) {
        // Convert the customer attributes to a CSV string
        return item.getCustomerId() + "," +
               item.getName() + "," +
               item.getEmail() + "," +
               item.getPhoneNumber() + "," +
               item.getCity() + "," +
               item.getState() + "," +
               item.getGender() + "," +
               item.getAgeGroup();
    }

    // Provide the header for the CSV file
    @Override
    protected String getHeader() {
        return "CustomerID,Name,Email,PhoneNumber,City,State,Gender,AgeGroup";  // The header for the customer CSV
    }
}
