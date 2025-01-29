package src.main.java.dataprocessing;

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
    private Map<String, Long> ordersByPaymentMethod;
    private Map<String, Double> totalRevenueByProduct;
    private Map<String, Long> ordersByStatus;
    private double totalRevenue;
    private double avgDiscount;
    private double avgShippingCost;
    private Map<Integer, Long> ratingFrequency;
    private Map<String, Long> deliveryStatusFrequency;
    private int bestRating;
    private int worstRating;
    private long bestReviewsCount;
    private long worstReviewsCount;
    private Order highestOrder;
    private Map<String, Double> maxDiscountByProduct;
    private Map<String, Double> minDiscountByProduct;
    private Map<String, Double> avgRatingPerProduct;
    private String mostBoughtProduct;
    private Map<String, Integer> modeQuantityByProduct;
    private Map<String, Double> monthlySales;
    private double[] quantityCoefficients;
    private double[] shippingCostCoefficients;
    private double[] discountAmountCoefficients;

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
        this.totalRevenue = data.stream().mapToDouble(o -> o.getTotalAmount()).sum();
        System.out.println("Total Revenue: " + totalRevenue); // Debug output

        // Orders by payment method
        this.ordersByPaymentMethod = data.stream()
        .collect(Collectors.groupingBy(Order::getPaymentMethod, Collectors.counting()));
        System.out.println("\nOrders by Payment Method: " + ordersByPaymentMethod);

        // Average discount and shipping cost
        this.avgDiscount = data.stream().mapToDouble(Order::getDiscountAmount).average().orElse(0);
        this.avgShippingCost = data.stream().mapToDouble(Order::getShippingCost).average().orElse(0);
        System.out.println("Average Discount: " + avgDiscount + ", Average Shipping Cost: " + avgShippingCost); // Debug output

        // Rating frequency
        this.ratingFrequency = data.stream()
                .collect(Collectors.groupingBy(Order::getReviewRating, Collectors.counting()));
        System.out.println("Rating Frequency: " + ratingFrequency); // Debug output

        // Delivery status frequency
        this.deliveryStatusFrequency = data.stream()
                .collect(Collectors.groupingBy(Order::getDeliveryStatus, Collectors.counting()));
        System.out.println("Delivery Status Frequency: " + deliveryStatusFrequency); // Debug output

        // Best and worst reviews
        this.bestRating = data.stream().mapToInt(Order::getReviewRating).max().orElse(0);
        this.worstRating = data.stream().mapToInt(Order::getReviewRating).min().orElse(0);
        this.bestReviewsCount = data.stream().filter(o -> o.getReviewRating() == bestRating).count();
        this.worstReviewsCount = data.stream().filter(o -> o.getReviewRating() == worstRating).count();
        System.out.println("Best Rating: " + bestRating + ", Worst Rating: " + worstRating); // Debug output
        System.out.println("Best Reviews Count: " + bestReviewsCount + ", Worst Reviews Count: " + worstReviewsCount); // Debug output

        // Order with the highest total amount
        this.highestOrder = data.stream().max(Comparator.comparingDouble(Order::getTotalAmount)).orElse(null);
        if (highestOrder == null) {
            System.out.println("No highest order found.");
        } else {
            System.out.println("\nHighest Order: " + highestOrder.getOrderId() + " with Total Amount: " + highestOrder.getTotalAmount()); // Debug output
        }

        // Maximum discount by product
        System.out.println("\nMaximum Discount By Product(TOP3)");
        this.maxDiscountByProduct = data.stream()
        .collect(Collectors.groupingBy(Order::getProductId,
                Collectors.collectingAndThen(Collectors.maxBy(Comparator.comparingDouble(Order::getDiscountAmount)),
                        o -> o.map(Order::getDiscountAmount).orElse(0.0))));
        maxDiscountByProduct.entrySet().stream()
        .limit(3)
        .forEach(entry -> System.out.print("Product: " + entry.getKey() + ", Max Discount: " + entry.getValue() + "; "));


        // Minimum discount by product
        System.out.println("\nMinimum Discount By Product(TOP3)");
        this.minDiscountByProduct = data.stream()
        .collect(Collectors.groupingBy(Order::getProductId,
                Collectors.collectingAndThen(Collectors.minBy(Comparator.comparingDouble(Order::getDiscountAmount)),
                        o -> o.map(Order::getDiscountAmount).orElse(0.0))));
        minDiscountByProduct.entrySet().stream()
        .limit(3) 
        .forEach(entry -> System.out.print("Product: " + entry.getKey() + ", Min Discount: " + entry.getValue() + "; "));
        System.out.println("}");


        // Average rating by product
        System.out.println("\nAverage rating by product(TOP3)");
        this.avgRatingPerProduct = data.stream()
        .collect(Collectors.groupingBy(Order::getProductId, Collectors.averagingInt(Order::getReviewRating)));
        avgRatingPerProduct.entrySet().stream()
        .limit(3) // Limit the output to the first 10 entries
        .forEach(entry -> System.out.print("Product: " + entry.getKey() + ", Avg Rating: " + entry.getValue() + "; "));
        System.out.println("}");


        // Most frequently bought product
        this.mostBoughtProduct = data.stream()
                .collect(Collectors.groupingBy(Order::getProductId, Collectors.summingInt(Order::getQuantity)))
                .entrySet().stream().max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey).orElse("No Product");
        System.out.println("\nMost Bought Product: " + mostBoughtProduct); 

        // Mode of quantity for products
        System.out.println("\nMode of quantity for products(TOP3)");
        this.modeQuantityByProduct = data.stream()
        .collect(Collectors.groupingBy(Order::getProductId,
                Collectors.collectingAndThen(Collectors.groupingBy(Order::getQuantity, Collectors.counting()),
                        m -> m.entrySet().stream().max(Map.Entry.comparingByValue()).get().getKey())));
        modeQuantityByProduct.entrySet().stream()
        .limit(3) 
        .forEach(entry -> System.out.print("Product: " + entry.getKey() + ", Mode Quantity: " + entry.getValue() + "; "));
        System.out.println("}");


        // Monthly sales
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        this.monthlySales = data.stream()
                .collect(Collectors.groupingBy(o -> {
                    try {
                        return LocalDate.parse(o.getOrderDate(), formatter).getMonth().toString();
                    } catch (Exception e) {
                        return "Unknown";
                    }
                }, Collectors.summingDouble(Order::getTotalAmount)));
        System.out.println("\nMonthly Sales: " + monthlySales); // Debug output

        // Regression Analysis
        List<Double> quantities = data.stream().map(Order::getQuantity).mapToDouble(i -> i).boxed().collect(Collectors.toList());
        List<Double> shippingCosts = data.stream().map(Order::getShippingCost).collect(Collectors.toList());
        List<Double> discountAmounts = data.stream().map(Order::getDiscountAmount).collect(Collectors.toList());
        List<Double> totalAmounts = data.stream().map(Order::getTotalAmount).collect(Collectors.toList());

        // Simple regression with totalAmount as the target variable
        quantityCoefficients = performSimpleLinearRegression(quantities, totalAmounts);
        shippingCostCoefficients = performSimpleLinearRegression(shippingCosts, totalAmounts);
        discountAmountCoefficients = performSimpleLinearRegression(discountAmounts, totalAmounts);
    }

    private double[] performSimpleLinearRegression(List<Double> x, List<Double> y) {
        // Ensure data size matches
        if (x.size() != y.size()) throw new IllegalArgumentException("Size of x and y must match.");
        
        // Calculate means
        double meanX = x.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
        double meanY = y.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);

        // Calculate the regression coefficients (slope and intercept)
        double numerator = 0.0;
        double denominator = 0.0;
        for (int i = 0; i < x.size(); i++) {
            numerator += (x.get(i) - meanX) * (y.get(i) - meanY);
            denominator += (x.get(i) - meanX) * (x.get(i) - meanX);
        }
        double slope = numerator / denominator;
        double intercept = meanY - slope * meanX;

        return new double[]{slope, intercept};
        
       
    }

public Map<String, Long> getOrdersByPaymentMethod() {
    return ordersByPaymentMethod;
}
public Map<String, Double> gettotalRevenueByProduct() {
    return totalRevenueByProduct;
}
public Map<String, Long> getordersByStatus() {
    return ordersByStatus;
}

public double getTotalRevenue() {
    return totalRevenue;
}

public double getAvgDiscount() {
    return avgDiscount;
}

public double getAvgShippingCost() {
    return avgShippingCost;
}

public Map<Integer, Long> getRatingFrequency() {
    return ratingFrequency;
}

public Map<String, Long> getDeliveryStatusFrequency() {
    return deliveryStatusFrequency;
}

public int getBestRating() {
    return bestRating;
}

public int getWorstRating() {
    return worstRating;
}

public long getBestReviewsCount() {
    return bestReviewsCount;
}

public long getWorstReviewsCount() {
    return worstReviewsCount;
}

public Order getHighestOrder() {
    return highestOrder;
}

public Map<String, Double> getMaxDiscountByProduct() {
    return maxDiscountByProduct;
}

public Map<String, Double> getMinDiscountByProduct() {
    return minDiscountByProduct;
}

public Map<String, Double> getAvgRatingPerProduct() {
    return avgRatingPerProduct;
}

public String getMostBoughtProduct() {
    return mostBoughtProduct;
}

public Map<String, Integer> getModeQuantityByProduct() {
    return modeQuantityByProduct;
}

public Map<String, Double> getMonthlySales() {
    return monthlySales;
}

public double[] getQuantityCoefficients() {
    return quantityCoefficients;
}

public double[] getShippingCostCoefficients() {
    return shippingCostCoefficients;
}

public double[] getDiscountAmountCoefficients() {
    return discountAmountCoefficients;
}


}
