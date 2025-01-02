package src.main.java.ReportGenerator;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import src.main.java.dataprocessingtest.*;
import src.main.java.model.*;

public class ReportGenerator {

    private CustomerDataProcessor customerDataProcessor;
    private OrderDataProcessor OrderDataProcessor;
    private ProductDataProcessor productDataProcessor;

    public ReportGenerator() {
        // Initialize the processors for customer, order, and product data
        this.customerDataProcessor = new CustomerDataProcessor();
        this.OrderDataProcessor = new OrderDataProcessor();
        this.productDataProcessor = new ProductDataProcessor();
    }

   

    public void generateOrderReport(List<Order> orders) {
         
        OrderDataProcessor.analyzeData(orders);

        // Create HTML content in a StringBuilder (instead of directly writing to the file)
        StringBuilder htmlContent = new StringBuilder();
        
        htmlContent.append("<html><head><title>Order Data Analysis Report</title></head><body>");
        htmlContent.append("<h2>Order Analysis</h2>");
        

        // Revenue and payment methods
        htmlContent.append("<h3>Revenue and Payment Methods</h3>");
        Map<String, Long> paymentMethods = OrderDataProcessor.getOrdersByPaymentMethod();
       
        if (paymentMethods != null && !paymentMethods.isEmpty()) {
            htmlContent.append("<ul>");
            for (Map.Entry<String, Long> entry : paymentMethods.entrySet()) {
                htmlContent.append(String.format("<li>%s: %d orders</li>%n", entry.getKey(), entry.getValue()));
            }
            htmlContent.append("</ul>");
        } else {
            htmlContent.append("<p>No payment methods data available.</p>");
        }
    
        // Average discount and shipping cost
        htmlContent.append("<h3>Average Discount and Shipping Cost</h3>");
        Double avgDiscount = OrderDataProcessor.getAvgDiscount();
        Double avgShippingCost = OrderDataProcessor.getAvgShippingCost();
        htmlContent.append("<ul>");
        htmlContent.append(String.format("<li>Average Discount: $%.2f</li>", avgDiscount != null ? avgDiscount : 0.0));
        htmlContent.append(String.format("<li>Average Shipping Cost: $%.2f</li></ul>%n", avgShippingCost != null ? avgShippingCost : 0.0));
    
        // Ratings and reviews
        htmlContent.append("<h3>Ratings and Reviews</h3>");
        Map<Integer, Long> ratingDistribution = OrderDataProcessor.getRatingFrequency();
        if (ratingDistribution != null && !ratingDistribution.isEmpty()) {
            htmlContent.append("<ul>");
            for (Map.Entry<Integer, Long> entry : ratingDistribution.entrySet()) {
                htmlContent.append(String.format("<li>%d-star: %d reviews</li>%n", entry.getKey(), entry.getValue()));
            }
            htmlContent.append("</ul>");
        } else {
            htmlContent.append("<p>No rating distribution data available.</p>");
        }
    
        // Best and Worst Reviews
        htmlContent.append("<h3>Best and Worst Reviews</h3>");
        Integer bestRating = OrderDataProcessor.getBestRating();
        Integer worstRating = OrderDataProcessor.getWorstRating();
        Long bestReviewsCount = OrderDataProcessor.getBestReviewsCount();
        Long worstReviewsCount = OrderDataProcessor.getWorstReviewsCount();
    
        if (bestRating != null && worstRating != null) {
            htmlContent.append(String.format("<ul><li>Best Rating: %d (from %d reviews)</li>", bestRating, bestReviewsCount != null ? bestReviewsCount : 0L));
            htmlContent.append(String.format("<li>Worst Rating: %d (from %d reviews)</li></ul>%n", worstRating, worstReviewsCount != null ? worstReviewsCount : 0L));
        } else {
            htmlContent.append("<p>No rating data available for best and worst reviews.</p>");
        }
    
        // Highest Order
        htmlContent.append("<h3>Highest Order</h3>");
        Order highestOrder = OrderDataProcessor.getHighestOrder();
        if (highestOrder != null) {
            htmlContent.append(String.format("<ul><li>Order ID: %s</li><li>Total Amount: $%.2f</li><li>Payment Method: %s</li></ul>%n", 
                            highestOrder.getOrderId(), highestOrder.getTotalAmount(), highestOrder.getPaymentMethod()));
        } else {
            htmlContent.append("<ul><li>No data available for the highest order.</li></ul>");
        } 
    
        // Maximum Discount by Product
        htmlContent.append("<h3>Maximum Discount by Product</h3>");
        Map<String, Double> maxDiscounts = OrderDataProcessor.getMaxDiscountByProduct();
        if (maxDiscounts != null && !maxDiscounts.isEmpty()) {
            htmlContent.append("<ul>");
            for (Map.Entry<String, Double> entry : maxDiscounts.entrySet()) {
                htmlContent.append(String.format("<li>Product ID: %s, Maximum Discount: $%.2f</li>%n", entry.getKey(), entry.getValue()));
            }
            htmlContent.append("</ul>");
        } else {
            htmlContent.append("<p>No data available for maximum discount by product.</p>");
        }
    
        // Monthly Sales
        htmlContent.append("<h3>Monthly Sales</h3>");
        Map<String, Double> monthlySales = OrderDataProcessor.getMonthlySales();
        if (monthlySales != null && !monthlySales.isEmpty()) {
            htmlContent.append("<ul>");
            for (Map.Entry<String, Double> entry : monthlySales.entrySet()) {
                htmlContent.append(String.format("<li>%s: $%.2f</li>%n", entry.getKey(), entry.getValue()));
            }
            htmlContent.append("</ul>");
        } else {
            htmlContent.append("<p>No data available for monthly sales.</p>");
        }
    
        htmlContent.append("</body></html>");
    
        // Write the generated HTML content to a file
        try (FileWriter writer = new FileWriter("order_data_analysis_report.html")) {
            writer.write(htmlContent.toString());
            System.out.println("HTML report generated successfully!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void generateCustomerReport(List<Customer> customers) {
        customerDataProcessor.analyzeData(customers);
    
        // Create HTML content in a StringBuilder (instead of directly writing to the file)
        StringBuilder htmlContent = new StringBuilder();
    
        htmlContent.append("<html><head><title>Customer Data Analysis Report</title></head><body>");
        htmlContent.append("<h2>Customer Analysis</h2>");
    
        // Gender Distribution
        htmlContent.append("<h3>Customer Gender Distribution</h3>");
        Map<String, Long> genderCount = customerDataProcessor.getGenderCount();
        if (genderCount != null && !genderCount.isEmpty()) {
            htmlContent.append("<ul>");
            for (Map.Entry<String, Long> entry : genderCount.entrySet()) {
                htmlContent.append(String.format("<li>%s: %d customers</li>%n", entry.getKey(), entry.getValue()));
            }
            htmlContent.append("</ul>");
        } else {
            htmlContent.append("<p>No gender distribution data available.</p>");
        }
    
        // Age Group Distribution
        htmlContent.append("<h3>Customer Age Group Distribution</h3>");
        Map<String, Long> ageGroupCount = customerDataProcessor.getAgeGroupCount();
        if (ageGroupCount != null && !ageGroupCount.isEmpty()) {
            htmlContent.append("<ul>");
            for (Map.Entry<String, Long> entry : ageGroupCount.entrySet()) {
                htmlContent.append(String.format("<li>%s: %d customers</li>%n", entry.getKey(), entry.getValue()));
            }
            htmlContent.append("</ul>");
        } else {
            htmlContent.append("<p>No age group distribution data available.</p>");
        }
    
        // Top 5 Cities
        htmlContent.append("<h3>Top 5 Cities by Customer Count</h3>");
        List<Map.Entry<String, Long>> topCities = customerDataProcessor.getTopCities();
        if (topCities != null && !topCities.isEmpty()) {
            htmlContent.append("<ul>");
            for (Map.Entry<String, Long> entry : topCities) {
                htmlContent.append(String.format("<li>%s: %d customers</li>%n", entry.getKey(), entry.getValue()));
            }
            htmlContent.append("</ul>");
        } else {
            htmlContent.append("<p>No city distribution data available.</p>");
        }
    
        htmlContent.append("</body></html>");
    
        // Write the generated HTML content to a file
        try (FileWriter writer = new FileWriter("customer_data_analysis_report.html")) {
            writer.write(htmlContent.toString());
            System.out.println("Customer HTML report generated successfully!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void generateProductReport(List<product> products) {
    productDataProcessor.analyzeData(products);

    // Create HTML content in a StringBuilder (instead of directly writing to the file)
    StringBuilder htmlContent = new StringBuilder();

    htmlContent.append("<html><head><title>Product Data Analysis Report</title></head><body>");
    htmlContent.append("<h2>Product Analysis</h2>");

    // Categories
    htmlContent.append("<h3>Product Categories</h3>");
    List<String> categories = productDataProcessor.getCategories();
    if (categories != null && !categories.isEmpty()) {
        htmlContent.append("<ul>");
        for (String category : categories) {
            htmlContent.append(String.format("<li>%s</li>%n", category));
        }
        htmlContent.append("</ul>");
    } else {
        htmlContent.append("<p>No category data available.</p>");
    }

    // Brands
    htmlContent.append("<h3>Product Brands</h3>");
    List<String> brands = productDataProcessor.getBrands();
    if (brands != null && !brands.isEmpty()) {
        htmlContent.append("<ul>");
        for (String brand : brands) {
            htmlContent.append(String.format("<li>%s</li>%n", brand));
        }
        htmlContent.append("</ul>");
    } else {
        htmlContent.append("<p>No brand data available.</p>");
    }

    // Product Count by Category
    htmlContent.append("<h3>Product Count by Category</h3>");
    Map<String, Long> productCountByCategory = productDataProcessor.getProductCountByCategory();
    if (productCountByCategory != null && !productCountByCategory.isEmpty()) {
        htmlContent.append("<ul>");
        for (Map.Entry<String, Long> entry : productCountByCategory.entrySet()) {
            htmlContent.append(String.format("<li>%s: %d products</li>%n", entry.getKey(), entry.getValue()));
        }
        htmlContent.append("</ul>");
    } else {
        htmlContent.append("<p>No product count by category data available.</p>");
    }

    // Cheapest and Most Expensive Products
    htmlContent.append("<h3>Cheapest and Most Expensive Products</h3>");
    Optional<product> cheapestProduct = productDataProcessor.getCheapestProduct();
    Optional<product> mostExpensiveProduct = productDataProcessor.getMostExpensiveProduct();
    htmlContent.append("<ul>");
    htmlContent.append(String.format("<li>Cheapest Product: %s ($%.2f)</li>", 
                                     cheapestProduct.map(product::getproductName).orElse("Not Found"),
                                     cheapestProduct.map(product::getprice).orElse(0.0)));
    htmlContent.append(String.format("<li>Most Expensive Product: %s ($%.2f)</li></ul>%n", 
                                     mostExpensiveProduct.map(product::getproductName).orElse("Not Found"),
                                     mostExpensiveProduct.map(product::getprice).orElse(0.0)));

    // Cheapest and Most Expensive Products by Category
    htmlContent.append("<h3>Cheapest and Most Expensive Products by Category</h3>");
    Map<String, Map<String, String>> cheapestAndMostExpensiveByCategory = productDataProcessor.getCheapestAndMostExpensiveByCategory();
    if (cheapestAndMostExpensiveByCategory != null && !cheapestAndMostExpensiveByCategory.isEmpty()) {
        htmlContent.append("<ul>");
        for (Map.Entry<String, Map<String, String>> categoryEntry : cheapestAndMostExpensiveByCategory.entrySet()) {
            htmlContent.append(String.format("<li>%s: Cheapest - %s, Most Expensive - %s</li>%n", 
                                              categoryEntry.getKey(),
                                              categoryEntry.getValue().get("Cheapest"),
                                              categoryEntry.getValue().get("Most Expensive")));
        }
        htmlContent.append("</ul>");
    } else {
        htmlContent.append("<p>No data available for cheapest and most expensive products by category.</p>");
    }

    htmlContent.append("</body></html>");

    // Write the generated HTML content to a file
    try (FileWriter writer = new FileWriter("product_data_analysis_report.html")) {
        writer.write(htmlContent.toString());
        System.out.println("Product HTML report generated successfully!");
    } catch (IOException e) {
        e.printStackTrace();
    }
}


    
    
}
