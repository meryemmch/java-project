package src.main.java.dataprocessingtest;


import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import src.main.java.model.product;


public class ProductDataProcessor extends DataProcessor<product> {

    // Parse each line of the CSV to create a product object
    @Override
    protected product parseLine(String line) {
        String[] values = line.split(",");
        if (values.length == 6) {
            // Assuming the CSV structure: ProductID, ProductName, Category, SubCategory, Brand, Price
            return new product(values[0], values[1], values[2], values[3], values[4], Double.parseDouble(values[5]));
        }
        return null;  // Return null if the line doesn't match the expected format
    }
    
      @Override
    public boolean isValid(product item) {
        // A product is valid if it has non-null fields and price > 0
        return item != null &&
               item.getproductId() != null && !item.getproductId().isEmpty() &&
               item.getproductName() != null && !item.getproductName().isEmpty() &&
               item.getprice() > 0;
    }

    // Override removeDuplicates to remove duplicate products based on productId
    @Override
    public void removeDuplicates(List<product> data) {
        Set<String> uniqueProductIds = new HashSet<>();
        List<product> uniqueData = data.stream()
                                      .filter(product -> product != null && uniqueProductIds.add(product.getproductId()))
                                      .collect(Collectors.toList());
        data.clear();  // Clear the original list
        data.addAll(uniqueData);  // Add the unique products back to the list
        System.out.println("Processed " + data.size() + " unique products.");
    }

    // Convert a product object to a CSV string
    @Override
    protected String convertToCSV(product item) {
        // Convert the product attributes to a CSV string
        return item.getproductId() + "," +
               item.getproductName() + "," +
               item.getcategory() + "," +
               item.getsubCategory() + "," +
               item.getbrand() + "," +
               item.getprice();
    }

    // Provide the header for the CSV file
    @Override
    protected String getHeader() {
        return "ProductID,ProductName,Category,SubCategory,Brand,Price";  // The header for the product CSV
    }
}
