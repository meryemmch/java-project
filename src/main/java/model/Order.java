package src.main.java.model;

public class Order {
    private String orderId;
    private String customerId;
    private String productId;
    private String sellerId;
    private int quantity;
    private String orderDate;
    private double shippingCost;
    private double discountAmount;
    private String paymentMethod;
    private double totalAmount;
    private String deliveryStatus;
    private int reviewRating;

    // Constructor

    public Order(String orderId, String customerId, String productId, String sellerId, int quantity,
                 String orderDate, double shippingCost, double discountAmount, String paymentMethod,
                 double totalAmount, String deliveryStatus, int reviewRating) {
        this.orderId = orderId;
        this.customerId = customerId;
        this.productId = productId;
        this.sellerId = sellerId;
        this.quantity = quantity;
        this.orderDate = orderDate;
        this.shippingCost = shippingCost;
        this.discountAmount = discountAmount;
        this.paymentMethod = paymentMethod;
        this.totalAmount = totalAmount;
        this.deliveryStatus = deliveryStatus;
        this.reviewRating = reviewRating;
    }

    // Getters
    public String getOrderId() {
        return orderId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public String getProductId() {
        return productId;
    }

    public String getSellerId() {
        return sellerId;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public double getShippingCost() {
        return shippingCost;
    }

    public double getDiscountAmount() {
        return discountAmount;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public String getDeliveryStatus() {
        return deliveryStatus;
    }

    public int getReviewRating() {
        return reviewRating;
    }

    // Setters
    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public void setSellerId(String sellerId) {
        this.sellerId = sellerId;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public void setShippingCost(double shippingCost) {
        this.shippingCost = shippingCost;
    }

    public void setDiscountAmount(double discountAmount) {
        this.discountAmount = discountAmount;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public void setDeliveryStatus(String deliveryStatus) {
        this.deliveryStatus = deliveryStatus;
    }

    public void setReviewRating(int reviewRating) {
        this.reviewRating = reviewRating;
    }
}