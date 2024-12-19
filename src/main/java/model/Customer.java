package src.main.java.model;

public class Customer extends Person {
    private String customerId;
    private String city;
    private String state;
    private String gender;
    private String ageGroup;

    // Constructor
    public Customer(String customerId, String name, String email, String phoneNumber, 
                    String city, String state,  String gender, String ageGroup) {
        super(name, email, phoneNumber);
        this.customerId = customerId;
        this.city = city;
        this.state = state;
        this.gender = gender;
        this.ageGroup = ageGroup;
    }

    // Getters and Setters
    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAgeGroup() {
        return ageGroup;
    }

    public void setAgeGroup(String ageGroup) {
        this.ageGroup = ageGroup;
    }
}
