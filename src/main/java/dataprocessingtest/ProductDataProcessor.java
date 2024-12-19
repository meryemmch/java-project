package src.main.java.dataprocessingtest;


import src.main.java.model.product;


public class ProductDataProcessor extends DataProcessor<product> {

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
}
