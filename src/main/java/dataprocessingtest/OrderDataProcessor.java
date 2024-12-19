package src.main.java.dataprocessingtest;



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
