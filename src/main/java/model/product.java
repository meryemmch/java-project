package src.main.java.model;

public class product {
    private String productId;
    private String productName;
    private String category;
    private String subCategory;
    private String brand;
    private double price;
    

    public product(String productId, String productName, String category, String subCategory, String brand,double price
                 ) {
        this.productId = productId;
        this.productName = productName;
        this.category = category;
        this.subCategory = subCategory;
        this.brand = brand;
        this.price = price;
    }
    //Getters and Setters
    public String getproductId() {
        return productId;
    }

    public void setproductId(String productId) {
        this.productId= productId;
    }

    public String getproductName() {
        return productName;
    }

    public void setproductName(String productName) {
        this.productName= productName;
    }

    public String getcategory() {
        return category;
    }

    public void setcategory(String category) {
        this.category = category;
    }
    public double getprice() {
        return price;
    }

    public void setprice(double price) {
        this.price= price;
    }

    public String getsubCategory() {
        return subCategory;
    }

    public void setsubCategory(String subCategory) {
        this.subCategory = subCategory;
    }
    public String getbrand() {
        return brand;
    }

    public void setprice(String brand) {
        this.brand= brand;
    }

}