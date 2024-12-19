package src.main.java.dataprocessingtest;


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
