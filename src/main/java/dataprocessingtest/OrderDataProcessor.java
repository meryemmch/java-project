package src.main.java.dataprocessingtest;



import java.util.HashSet;
import java.util.List;
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
}
