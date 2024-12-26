package src.main.java.dataprocessingtest;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import src.main.java.model.Order;

public class OrderDataProcessor extends DataProcessor<Order> {

    // Parse each line of the CSV to create an Order object
    @Override
    protected Order parseLine(String line) {
        String[] values = line.split(",");
        if (values.length == 12) {  // Assuming CSV structure: OrderID, CustomerID, ProductID, SellerID, Quantity, OrderDate, ShippingCost, DiscountAmount, PaymentMethod, TotalAmount, DeliveryStatus, ReviewRating
            return new Order(values[0], values[1], values[2], values[3], Integer.parseInt(values[4]),
                    values[5], Double.parseDouble(values[6]), Double.parseDouble(values[7]), values[8],
                    Double.parseDouble(values[9]), values[10], Integer.parseInt(values[11]));
        }
        return null;  // Return null if the line doesn't match the expected format
    }
    
       @Override
    public boolean isValid(Order order) {
        // An order is valid if it has non-null and valid fields
        return order != null &&
               order.getOrderId() != null && !order.getOrderId().isEmpty() &&
               order.getCustomerId() != null && !order.getCustomerId().isEmpty() &&
               order.getProductId() != null && !order.getProductId().isEmpty() &&
               order.getQuantity() > 0 &&
               order.getTotalAmount() > 0;
    }

    // Override removeDuplicates to remove duplicate orders based on the orderId
    @Override
    public void removeDuplicates(List<Order> data) {
        Set<String> uniqueOrderIds = new HashSet<>();
        List<Order> uniqueData = data.stream()
                                     .filter(order -> order != null && uniqueOrderIds.add(order.getOrderId()))
                                     .collect(Collectors.toList());
        data.clear();  // Clear the original list
        data.addAll(uniqueData);  // Add the unique orders back to the list
        System.out.println("Processed " + data.size() + " unique orders.");
    }

    // Convert an Order object to a CSV string
    @Override
    protected String convertToCSV(Order item) {
        // Convert the order attributes to a CSV string
        return item.getOrderId() + "," +
               item.getCustomerId() + "," +
               item.getProductId() + "," +
               item.getSellerId() + "," +
               item.getQuantity() + "," +
               item.getOrderDate() + "," +
               item.getShippingCost() + "," +
               item.getDiscountAmount() + "," +
               item.getPaymentMethod() + "," +
               item.getTotalAmount() + "," +
               item.getDeliveryStatus() + "," +
               item.getReviewRating();
    }

    // Provide the header for the CSV file
    @Override
    protected String getHeader() {
        return "OrderID,CustomerID,ProductID,SellerID,Quantity,OrderDate,ShippingCost,DiscountAmount,PaymentMethod,TotalAmount,DeliveryStatus,ReviewRating";  // The header for the order CSV
    }

    @Override
public void analyzeData(List<Order> data) {
    
    if (data == null || data.isEmpty()) {
        System.out.println("No data available to analyze.");
        return;
    }

    // Total Revenue
    double totalRevenue = data.stream().mapToDouble(o -> o.getTotalAmount()).sum();
    System.out.println("\nTotal Revenue: $" + totalRevenue);

    // Number of orders per payment method
    Map<String, Long> ordersByPaymentMethod = data.stream()
            .collect(Collectors.groupingBy(Order::getPaymentMethod, Collectors.counting()));
    System.out.println("\nOrders by Payment Method: " + ordersByPaymentMethod);

    // Average discount and shipping cost
    double avgDiscount = data.stream().mapToDouble(Order::getDiscountAmount).average().orElse(0);
    double avgShippingCost = data.stream().mapToDouble(Order::getShippingCost).average().orElse(0);
    System.out.println("\nAverage Discount: $" + avgDiscount);
    System.out.println("Average Shipping Cost: $" + avgShippingCost);

    // Frequency of ratings
    Map<Integer, Long> ratingFrequency = data.stream()
            .collect(Collectors.groupingBy(Order::getReviewRating, Collectors.counting()));
    System.out.println("\nRating Frequency: " + ratingFrequency);

    // Frequency of delivery status
    Map<String, Long> deliveryStatusFrequency = data.stream()
            .collect(Collectors.groupingBy(Order::getDeliveryStatus, Collectors.counting()));
    System.out.println("\nDelivery Status Frequency: " + deliveryStatusFrequency);

    // Best and worst reviews
    int bestRating = data.stream().mapToInt(Order::getReviewRating).max().orElse(0);
    int worstRating = data.stream().mapToInt(Order::getReviewRating).min().orElse(0);
    long bestReviewsCount = data.stream().filter(o -> o.getReviewRating() == bestRating).count();
    long worstReviewsCount = data.stream().filter(o -> o.getReviewRating() == worstRating).count();
    System.out.println("\nNumber of Best Reviews: " + bestReviewsCount);
    System.out.println("Number of Worst Reviews: " + worstReviewsCount);

    // Order with the highest total amount
    Order highestOrder = data.stream().max(Comparator.comparingDouble(Order::getTotalAmount)).orElse(null);
    if (highestOrder != null) {
        System.out.println("\nOrder with Highest Total Amount: " + highestOrder.getOrderId());
    }

    // Maximum and Minimum Discount by Product
    Map<String, Double> maxDiscountByProduct = data.stream()
            .collect(Collectors.groupingBy(Order::getProductId, 
                    Collectors.collectingAndThen(Collectors.maxBy(Comparator.comparingDouble(Order::getDiscountAmount)), 
                            o -> o.map(Order::getDiscountAmount).orElse(0.0))));
    Map<String, Double> minDiscountByProduct = data.stream()
            .collect(Collectors.groupingBy(Order::getProductId, 
                    Collectors.collectingAndThen(Collectors.minBy(Comparator.comparingDouble(Order::getDiscountAmount)), 
                            o -> o.map(Order::getDiscountAmount).orElse(0.0))));
    System.out.println("\nMaximum Discount by Product: " + maxDiscountByProduct);
    System.out.println("\nMinimum Discount by Product: " + minDiscountByProduct);

    // Average rating by product
    Map<String, Double> avgRatingPerProduct = data.stream()
            .collect(Collectors.groupingBy(Order::getProductId, Collectors.averagingInt(Order::getReviewRating)));
    System.out.println("\nAverage Rating by Product: " + avgRatingPerProduct);

    // Most frequently bought product
    String mostBoughtProduct = data.stream()
            .collect(Collectors.groupingBy(Order::getProductId, Collectors.summingInt(Order::getQuantity)))
            .entrySet().stream().max(Map.Entry.comparingByValue())
            .map(Map.Entry::getKey).orElse("No Product");
    System.out.println("\nMost Frequently Bought Product: " + mostBoughtProduct);

    // Mode of quantity for products
    Map<String, Integer> modeQuantityByProduct = data.stream()
            .collect(Collectors.groupingBy(Order::getProductId, 
                    Collectors.collectingAndThen(Collectors.groupingBy(Order::getQuantity, Collectors.counting()), 
                            m -> m.entrySet().stream().max(Map.Entry.comparingByValue()).get().getKey())));
    System.out.println("\nMode Quantity by Product: " + modeQuantityByProduct);

    // Monthly Sales
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    Map<String, Double> monthlySales = data.stream()
            .collect(Collectors.groupingBy(o -> {
                try {
                    return LocalDate.parse(o.getOrderDate(), formatter).getMonth().toString();
                } catch (Exception e) {
                    return "Unknown";
                }
            }, Collectors.summingDouble(Order::getTotalAmount)));
    System.out.println("Monthly Sales: " + monthlySales);
}

}
