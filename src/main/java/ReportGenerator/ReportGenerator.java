package src.main.java.ReportGenerator;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

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
        // Ensure data is analyzed before generating the report
        OrderDataProcessor.analyzeData(orders);

        // Retrieve the necessary data from the processor
        Map<String, Long> ordersByPaymentMethod = OrderDataProcessor.getOrdersByPaymentMethod();
        Double avgDiscount = OrderDataProcessor.getAvgDiscount();
        Double avgShippingCost = OrderDataProcessor.getAvgShippingCost();
        Map<Integer, Long> ratingFrequency = OrderDataProcessor.getRatingFrequency();
        int bestRating = OrderDataProcessor.getBestRating();
        int worstRating = OrderDataProcessor.getWorstRating();
        Long bestReviewsCount = OrderDataProcessor.getBestReviewsCount();
        Long worstReviewsCount = OrderDataProcessor.getWorstReviewsCount();
        Order highestOrder = OrderDataProcessor.getHighestOrder();
        Map<String, Double> maxDiscountByProduct = OrderDataProcessor.getMaxDiscountByProduct();
        Map<String, Double> minDiscountByProduct = OrderDataProcessor.getMinDiscountByProduct();
        Map<String, Double> monthlySales = OrderDataProcessor.getMonthlySales();
        Map<String, Double> avgRatingPerProduct = OrderDataProcessor.getAvgRatingPerProduct();
        String mostBoughtProduct = OrderDataProcessor.getMostBoughtProduct();
        Map<String, Integer> modeQuantityByProduct = OrderDataProcessor.getModeQuantityByProduct();

        // Create HTML content in a StringBuilder
        StringBuilder htmlContent = new StringBuilder();
        htmlContent.append("<html><head><title>Order Data Analysis Report</title></head><body>");
        htmlContent.append("<h2>Order Data Analysis Report</h2>");

        // Revenue and payment methods
        htmlContent.append("<h3>Revenue and Payment Methods</h3>");
        if (ordersByPaymentMethod != null && !ordersByPaymentMethod.isEmpty()) {
            htmlContent.append("<ul>");
            for (Map.Entry<String, Long> entry : ordersByPaymentMethod.entrySet()) {
                htmlContent.append(String.format("<li>%s: %d orders</li>%n", entry.getKey(), entry.getValue()));
            }
            htmlContent.append("</ul>");
        } else {
            htmlContent.append("<p>No payment methods data available.</p>");
        }

        // Average discount and shipping cost
        htmlContent.append("<h3>Average Discount and Shipping Cost</h3>");
        htmlContent.append("<ul>");
        htmlContent.append(String.format("<li>Average Discount: $%.2f</li>", avgDiscount != null ? avgDiscount : 0.0));
        htmlContent.append(String.format("<li>Average Shipping Cost: $%.2f</li></ul>%n",
                avgShippingCost != null ? avgShippingCost : 0.0));

        // Ratings and reviews
        htmlContent.append("<h3>Ratings and Reviews</h3>");
        if (ratingFrequency != null && !ratingFrequency.isEmpty()) {
            htmlContent.append("<ul>");
            for (Map.Entry<Integer, Long> entry : ratingFrequency.entrySet()) {
                htmlContent.append(String.format("<li>%d-star: %d reviews</li>%n", entry.getKey(), entry.getValue()));
            }
            htmlContent.append("</ul>");
        } else {
            htmlContent.append("<p>No rating distribution data available.</p>");
        }

        // Best and worst reviews
        htmlContent.append("<h3>Best and Worst Reviews</h3>");
        if (bestRating != 0 && worstRating != 0) {
            htmlContent.append(String.format("<ul><li>Best Rating: %d (from %d reviews)</li>", bestRating,
                    bestReviewsCount != null ? bestReviewsCount : 0L));
            htmlContent.append(String.format("<li>Worst Rating: %d (from %d reviews)</li></ul>%n", worstRating,
                    worstReviewsCount != null ? worstReviewsCount : 0L));
        } else {
            htmlContent.append("<p>No rating data available for best and worst reviews.</p>");
        }

        // Highest order
        htmlContent.append("<h3>Highest Order</h3>");
        if (highestOrder != null) {
            htmlContent.append(String.format(
                    "<ul><li>Order ID: %s</li><li>Total Amount: $%.2f</li><li>Payment Method: %s</li></ul>%n",
                    highestOrder.getOrderId(), highestOrder.getTotalAmount(), highestOrder.getPaymentMethod()));
        } else {
            htmlContent.append("<ul><li>No data available for the highest order.</li></ul>");
        }

        // Maximum discount by product
        htmlContent.append("<h3>Maximum Discount by Product</h3>");
        if (maxDiscountByProduct != null && !maxDiscountByProduct.isEmpty()) {
            htmlContent.append("<ul>");
            maxDiscountByProduct.entrySet().stream()
                    .limit(3) // Limit the output to the top 3
                    .forEach(entry -> htmlContent.append(String.format(
                            "<li>Product ID: %s, Maximum Discount: $%.2f</li>%n", entry.getKey(), entry.getValue())));
            htmlContent.append("</ul>");
        } else {
            htmlContent.append("<p>No data available for maximum discount by product.</p>");
        }

        // Minimum discount by product
        htmlContent.append("<h3>Minimum Discount by Product</h3>");
        if (minDiscountByProduct != null && !minDiscountByProduct.isEmpty()) {
            htmlContent.append("<ul>");
            minDiscountByProduct.entrySet().stream()
                    .limit(3) // Limit the output to the top 3
                    .forEach(entry -> htmlContent.append(String.format(
                            "<li>Product ID: %s, Minimum Discount: $%.2f</li>%n", entry.getKey(), entry.getValue())));
            htmlContent.append("</ul>");
        } else {
            htmlContent.append("<p>No data available for minimum discount by product.</p>");
        }

        // Average rating by product
        htmlContent.append("<h3>Average Rating by Product</h3>");
        if (avgRatingPerProduct != null && !avgRatingPerProduct.isEmpty()) {
            htmlContent.append("<ul>");
            avgRatingPerProduct.entrySet().stream()
                    .limit(3) // Limit the output to the top 3
                    .forEach(
                            entry -> htmlContent.append(String.format("<li>Product ID: %s, Average Rating: %.2f</li>%n",
                                    entry.getKey(), entry.getValue())));
            htmlContent.append("</ul>");
        } else {
            htmlContent.append("<p>No data available for average rating by product.</p>");
        }

        // Most frequently bought product
        htmlContent.append("<h3>Most Frequently Bought Product</h3>");
        htmlContent.append("<p>Product ID: ").append(mostBoughtProduct).append("</p>");

        // Mode of quantity for products
        htmlContent.append("<h3>Mode of Quantity for Products</h3>");
        if (modeQuantityByProduct != null && !modeQuantityByProduct.isEmpty()) {
            htmlContent.append("<ul>");
            modeQuantityByProduct.entrySet().stream()
                    .limit(3) // Limit the output to the top 3
                    .forEach(entry -> htmlContent.append(String.format("<li>Product ID: %s, Mode Quantity: %d</li>%n",
                            entry.getKey(), entry.getValue())));
            htmlContent.append("</ul>");
        } else {
            htmlContent.append("<p>No data available for mode of quantity by product.</p>");
        }

        // Monthly sales
        htmlContent.append("<h3>Monthly Sales</h3>");
        if (monthlySales != null && !monthlySales.isEmpty()) {
            htmlContent.append("<ul>");
            for (Map.Entry<String, Double> entry : monthlySales.entrySet()) {
                htmlContent.append(String.format("<li>%s: $%.2f</li>%n", entry.getKey(), entry.getValue()));
            }
            htmlContent.append("</ul>");
        } else {
            htmlContent.append("<p>No data available for monthly sales.</p>");
        }

        htmlContent.append("<h3>Order Statistics</h3>");
        htmlContent.append("<ul>");

        // Calculate mean and standard deviation for review ratings
        List<Double> reviewRatings = orders.stream().map(Order::getReviewRating).map(Integer::doubleValue)
                .collect(Collectors.toList());
        double meanReviewRating = calculateMean(reviewRatings);
        htmlContent.append(String.format("<li>Mean Review Rating: %.2f</li>", meanReviewRating));

        // Calculate mean and standard deviation for total amounts
        List<Double> totalAmounts = orders.stream().map(Order::getTotalAmount).collect(Collectors.toList());
        double meanTotalAmount = calculateMean(totalAmounts);
        double stdDevTotalAmount = calculateStandardDeviation(totalAmounts);
        htmlContent.append(String.format("<li>Mean Total Amount: %.2f</li>", meanTotalAmount));
        htmlContent.append(String.format("<li>Standard Deviation of Total Amount: %.2f</li>", stdDevTotalAmount));

        // Calculate mean discount amount
        List<Double> discountAmounts = orders.stream().map(Order::getDiscountAmount).collect(Collectors.toList());
        double meanDiscountAmount = calculateMean(discountAmounts);
        htmlContent.append(String.format("<li>Mean Discount Amount: %.2f</li>", meanDiscountAmount));

        // Calculate mean shipping cost
        List<Double> shippingCosts = orders.stream().map(Order::getShippingCost).collect(Collectors.toList());
        double meanShippingCost = calculateMean(shippingCosts);
        htmlContent.append(String.format("<li>Mean Shipping Cost: %.2f</li>", meanShippingCost));

        htmlContent.append("</ul>");

        htmlContent.append("<h3> insights</h3>");
        htmlContent.append("<p>The dataset reveals consistent customer engagement across diverse payment methods. Reviews indicate a wide variation in customer satisfaction, emphasizing the need for quality improvements in low-rated products.</p>");
        htmlContent.append("<h3> Conclusion</h3>");
        htmlContent.append("<p>The analysis provides actionable insights into sales and customer behavior, emphasizing the importance of strategic promotions, tailored discount strategies, and enhanced customer satisfaction. Further efforts should explore customer feedback and cross-category behavior to fine-tune marketing approaches.</p>");
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
        Map<String, Long> genderCount = customerDataProcessor.getGenderCount();
        List<Map.Entry<String, Long>> topCities = customerDataProcessor.getTopCities();
        Map<String, Long> ageGroupCount = customerDataProcessor.getAgeGroupCount();
        StringBuilder htmlContent = new StringBuilder();

        htmlContent.append("<html><head><title>Customer Data Analysis Report</title></head><body>");
        htmlContent.append("<h2>Customer Data Analysis Report</h2>");

        // Gender Distribution
        htmlContent.append("<h3>Customer Gender Distribution</h3>");
        if (genderCount != null && !genderCount.isEmpty()) {
            htmlContent.append("<ul>");
            for (Map.Entry<String, Long> entry : genderCount.entrySet()) {
                htmlContent.append(String.format("<li>%s: %d customers</li>%n", entry.getKey(), entry.getValue()));
            }
            htmlContent.append("</ul>");
            htmlContent.append("<p>Insights: ");
            htmlContent.append(
                    "The data shows a higher engagement from Female and Other gender customers compared to Male customers. ");
            htmlContent.append(
                    "This suggests an opportunity to focus marketing and product offerings on these demographics while exploring reasons for lower Male engagement.");
            htmlContent.append("<h3>Insights:</h3> ");
            htmlContent.append("The data shows a higher engagement from Female and Other gender customers compared to Male customers. ");
            htmlContent.append("This suggests an opportunity to focus marketing and product offerings on these demographics while exploring reasons for lower Male engagement.");
            htmlContent.append("</p>");
        } else {
            htmlContent.append("<p>No gender distribution data available.</p>");
        }

        // Age Group Distribution
        htmlContent.append("<h3>Customer Age Group Distribution</h3>");
        if (ageGroupCount != null && !ageGroupCount.isEmpty()) {
            htmlContent.append("<ul>");
            for (Map.Entry<String, Long> entry : ageGroupCount.entrySet()) {
                htmlContent.append(String.format("<li>%s: %d customers</li>%n", entry.getKey(), entry.getValue()));
            }
            htmlContent.append("</ul>");
            htmlContent.append("<p>Insights: ");
            htmlContent.append("The 26-35 and 36-45 age groups form the core customer base. ");
            htmlContent.append(
                    "However, there is a balanced engagement across all age groups, suggesting a diverse customer base. ");
            htmlContent.append(
                    "Focus on creating tailored offerings for the 26-45 age group while maintaining inclusivity for others.");
            htmlContent.append("</p>");
        } else {
            htmlContent.append("<p>No age group distribution data available.</p>");
        }

        // Top 5 Cities by Customer Count
        htmlContent.append("<h3>Top 5 Cities by Customer Count</h3>");
        if (topCities != null && !topCities.isEmpty()) {
            htmlContent.append("<ul>");
            for (Map.Entry<String, Long> entry : topCities) {
                htmlContent.append(String.format("<li>%s: %d customers</li>%n", entry.getKey(), entry.getValue()));
            }
            htmlContent.append("</ul>");
            htmlContent.append("<h3>Insights:</h3> ");
            htmlContent.append("Bangalore leads the customer count, followed closely by Pune. ");
            htmlContent.append(
                    "These cities could be prioritized for marketing campaigns, customer loyalty programs, and expanded services. ");
            htmlContent.append(
                    "Kolkata, Delhi, and Chennai also show significant engagement, indicating strong potential in metropolitan areas.");
            htmlContent.append("</p>");
        } else {
            htmlContent.append("<p>No city distribution data available.</p>");
        }

        htmlContent.append("<h3> Conclusion</h3>");
        htmlContent.append("<p>The analysis highlights key trends in gender, age, and geographic distribution among customers. Opportunities include targeting high-potential cities, designing age-specific campaigns, and fostering inclusivity in marketing strategies. Future studies could delve deeper into customer preferences, behavior patterns, and product-specific demand to refine strategies and maximize engagement.</p>");       
    
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
        // Analyze data using ProductDataProcessor
        productDataProcessor.analyzeData(products);

        // Retrieve data from the processor
        List<String> categories = productDataProcessor.getCategories();
        List<String> brands = productDataProcessor.getBrands();
        Map<String, Long> productCountByCategory = productDataProcessor.getProductCountByCategory();
        Optional<product> cheapestProduct = productDataProcessor.getCheapestProduct();
        Optional<product> mostExpensiveProduct = productDataProcessor.getMostExpensiveProduct();
        Map<String, Map<String, String>> priceExtremesByCategory = productDataProcessor
                .getCheapestAndMostExpensiveByCategory();

        // Create HTML content in a StringBuilder
        StringBuilder htmlContent = new StringBuilder();

        htmlContent.append("<html><head><title>Product Data Analysis Report</title></head><body>");
        htmlContent.append("<h1>Product Data Analysis Report</h1>");
        htmlContent.append("<h2>Data Overview</h2>");
        htmlContent.append("<ul>");
        htmlContent.append("<li>Total Records Read: ").append(products.size()).append(" products.</li>");
        htmlContent.append("<li>Unique Products Identified: ")
                .append(productDataProcessor.getProductCountByCategory().values().stream().reduce(0L, Long::sum))
                .append(".</li>");
        htmlContent.append("</ul>");

        htmlContent.append("<h2>Categories and Brands</h2>");
        htmlContent.append("<p><strong>Categories Identified:</strong> ").append(String.join(", ", categories))
                .append(".</p>");
        htmlContent.append("<p><strong>Brands Represented:</strong> ").append(String.join(", ", brands))
                .append(".</p>");
        htmlContent.append("<h3>Product Distribution by Category:</h3>");
        htmlContent.append("<ul>");
        for (Map.Entry<String, Long> entry : productCountByCategory.entrySet()) {
            htmlContent.append("<li>").append(entry.getKey()).append(": ").append(entry.getValue())
                    .append(" products.</li>");
        }
        htmlContent.append("</ul>");

        htmlContent.append("<h2>Product Pricing Analysis</h2>");
        htmlContent.append("<h3>Price Comparison</h3>");
        htmlContent.append("<p><strong>Cheapest Product:</strong> ")
                .append(cheapestProduct.map(product::getproductName).orElse("Not Found"))
                .append(".</p>");
        htmlContent.append("<p><strong>Most Expensive Product:</strong> ")
                .append(mostExpensiveProduct.map(product::getproductName).orElse("Not Found"))
                .append(".</p>");
        htmlContent.append("<h3>Price Extremes by Category:</h3>");
        htmlContent.append("<ul>");
        for (Map.Entry<String, Map<String, String>> entry : priceExtremesByCategory.entrySet()) {
            String category = entry.getKey();
            Map<String, String> prices = entry.getValue();
            htmlContent.append("<li><strong>").append(category).append(":</strong> ")
                    .append("Cheapest: ").append(prices.get("Cheapest"))
                    .append(", Most Expensive: ").append(prices.get("Most Expensive")).append(".</li>");
        }
        htmlContent.append("</ul>");

        // Product-related statistics
        
        // Calculate mean and standard deviation for product prices
        List<Double> productPrices = products.stream().map(product::getprice).collect(Collectors.toList());
        double meanProductPrice = calculateMean(productPrices);
        double stdDevProductPrice = calculateStandardDeviation(productPrices);
        htmlContent.append(String.format("<li>Mean Product Price: %.2f</li>", meanProductPrice));
        htmlContent.append(String.format("<li>Standard Deviation of Product Price: %.2f</li>", stdDevProductPrice));

        htmlContent.append("<h3>Product Statistics</h3>");
        htmlContent.append("<ul>");
        htmlContent.append(String.format("<li>Cheapest Product: %s</li>",
                productDataProcessor.getCheapestProduct()
                    .map(product::getproductName) // Use the class name or lambda as needed
                    .orElse("Not Found")));
        htmlContent.append(String.format("<li>Most Expensive Product: %s</li>",
                productDataProcessor.getMostExpensiveProduct()
                    .map(product::getproductName)
                    .orElse("Not Found")));
        htmlContent.append("<li>Product Count by Category: <ul>");
        productDataProcessor.getProductCountByCategory()
                .forEach((category, count) -> htmlContent.append(String.format("<li>%s: %d</li>", category, count)));
        htmlContent.append("</ul></li>");
        htmlContent.append("<li>Cheapest and Most Expensive Products by Category: <ul>");
        productDataProcessor.getCheapestAndMostExpensiveByCategory()
                .forEach((category, priceMap) -> htmlContent
                        .append(String.format("<li>%s: Cheapest - %s, Most Expensive - %s</li>", category,
                                priceMap.get("Cheapest"), priceMap.get("Most Expensive"))));
        htmlContent.append("</ul></li>");
        htmlContent.append("</ul>");

        htmlContent.append("<h2>Insights and Recommendations</h2>");
        htmlContent.append("<ul>");
        htmlContent.append(
                "<li><strong>Targeted Promotions:</strong> Focus on high-demand categories with discounts for younger demographics.</li>");
        htmlContent.append(
                "<li><strong>Luxury Market Focus:</strong> Develop strategies to target premium customers in categories like Beauty.</li>");
        htmlContent
                .append("<li><strong>Dynamic Pricing:</strong> Use pricing strategies tailored to demand trends.</li>");
        htmlContent.append("</ul>");

        htmlContent.append("<h3>Conclusion</h3>");
        htmlContent.append("<p>The analysis provides actionable insights into product performance, highlighting opportunities to refine pricing strategies, focus on high-demand demographics, and expand profitable subcategories. Future efforts could include detailed analysis of customer feedback, cross-category purchase behavior, and market trends to refine business strategies further.</p>");


        htmlContent.append("</body></html>");

        // Write the HTML report to a file
        try (FileWriter writer = new FileWriter("product_data_analysis_report.html")) {
            writer.write(htmlContent.toString());
            System.out.println("Product HTML report generated successfully!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Helper method to calculate mean
    private double calculateMean(List<Double> values) {
        if (values.isEmpty())
            return 0.0;
        double sum = values.stream().mapToDouble(Double::doubleValue).sum();
        return sum / values.size();
    }

    // Helper method to calculate standard deviation
    private double calculateStandardDeviation(List<Double> values) {
        if (values.isEmpty())
            return 0.0;
        double mean = calculateMean(values);
        double variance = values.stream().mapToDouble(x -> Math.pow(x - mean, 2)).sum() / values.size();
        return Math.sqrt(variance);
    }
}
