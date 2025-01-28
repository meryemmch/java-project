package src.main.java.dataprocessingtest;


import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import src.main.java.model.product;


public class ProductDataProcessor extends DataProcessor<product> {
    private List<String> categories;
    private List<String> brands;
    private Map<String, Long> productCountByCategory;
    private Optional<product> cheapestProduct;
    private Optional<product> mostExpensiveProduct;
    private Map<String, Map<String, String>> cheapestAndMostExpensiveByCategory;


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

    @Override
    public void analyzeData(List<product> data) {
        System.out.println("Analyzing Product data...");
    
        // Unique categories
        this.categories = data.stream()
                .map(product::getcategory)
                .distinct()
                .collect(Collectors.toList());
        System.out.println("\nCategories: " + categories);
    
        // Unique brands
        this.brands = data.stream()
                .map(product::getbrand)
                .distinct()
                .collect(Collectors.toList());
        System.out.println("Brands: " + brands);
    
        // Product count by category
        this.productCountByCategory = data.stream()
                .collect(Collectors.groupingBy(product::getcategory, Collectors.counting()));
        System.out.println("\nProduct Count by Category: " + productCountByCategory);
    
        // Cheapest and most expensive product
        this.cheapestProduct = data.stream()
                .min(Comparator.comparingDouble(product::getprice));
        this.mostExpensiveProduct = data.stream() // FIX: Assign to the class field.
                .max(Comparator.comparingDouble(product::getprice));
    
        System.out.println("\nCheapest Product: " + cheapestProduct.map(product::getproductName).orElse("Not Found"));
        System.out.println("\nMost Expensive Product: " + mostExpensiveProduct.map(product::getproductName).orElse("Not Found"));
    
        // Cheapest and most expensive product by category
        this.cheapestAndMostExpensiveByCategory = new HashMap<>();
        data.stream()
                .collect(Collectors.groupingBy(product::getcategory))
                .forEach((category, products) -> {
                    Optional<product> cheapest = products.stream()
                            .min(Comparator.comparingDouble(product::getprice));
                    Optional<product> mostExpensive = products.stream()
                            .max(Comparator.comparingDouble(product::getprice));
    
                    Map<String, String> priceMap = new HashMap<>();
                    priceMap.put("Cheapest", cheapest.map(product::getproductName).orElse("Not Found"));
                    priceMap.put("Most Expensive", mostExpensive.map(product::getproductName).orElse("Not Found"));
                    cheapestAndMostExpensiveByCategory.put(category, priceMap);
                });
        System.out.println("\nCheapest and Most Expensive Products by Category: " + cheapestAndMostExpensiveByCategory);
    }
    

    public List<String> getCategories() {
        return categories;
    }

    public List<String> getBrands() {
        return brands;
    }

    public Map<String, Long> getProductCountByCategory() {
        return productCountByCategory;
    }

    public Optional<product> getCheapestProduct() {
        return cheapestProduct;
    }

    public Optional<product> getMostExpensiveProduct() {
        return mostExpensiveProduct;
    }

    public Map<String, Map<String, String>> getCheapestAndMostExpensiveByCategory() {
        return cheapestAndMostExpensiveByCategory;
    }
}
