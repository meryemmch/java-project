package src.main.java.dataprocessingtest;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


public abstract class DataProcessor<T> implements DataProcessing<T>, DataReading<T>, DataStoring<T>, Analyzable<T> {

    // Common method for reading CSV data (generic)
    @Override
    public List<T> readData(String filePath) {
        List<T> data = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            br.readLine();  // Skip the header line if there is one
            
            // Read lines and parse them using the specific parsing logic of the subclass
            while ((line = br.readLine()) != null) {
                T item = parseLine(line);  // Subclass will implement this method
                if (item != null) {
                    data.add(item);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return data;
    }

    // Abstract method for specific parsing logic, to be implemented by subclasses
    protected abstract T parseLine(String line);

    @Override
    public boolean isValid(T data) {
        return data != null;
    }
    
     @Override
    public void removeDuplicates(List<T> data) {
        Set<T> uniqueData = new HashSet<>(data);  // A HashSet automatically removes duplicates
        data.clear();  // Clear the original list
        data.addAll(uniqueData);  // Add only unique elements back to the list
        System.out.println("Processed " + data.size() + " unique items.");
    }
   
    @Override
    public List<T> removeRowsWithMissingData(List<T> data) {
        return data.stream()
                   .filter(this::isValid)  // Keep only valid rows
                   .collect(Collectors.toList());
    }

    
    @Override
    public List<T> getRandomSample(List<T> data, int numRows) {
        Collections.shuffle(data);  // Shuffle the list to randomize the selection
        return data.subList(0, Math.min(numRows, data.size()));  // Return the first 'numRows' items
    }

    
    @Override
    public void analyzeData(List<T> data) {
        System.out.println("Analyzing data...");
        // Implementing analysis logic here 
    }

    @Override
    public void storeData(List<T> data, String filePath) {
        // Call the subclass-specific implementation for writing data to file
        writeFile(data, filePath);
    }

    // Common method to open BufferedWriter and handle file writing
    protected void writeFile(List<T> data, String filePath) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            // Write the header line for the CSV (this could be customized if needed)
            writer.write(getHeader());
            writer.newLine();  // New line after header

            // Iterate through each item in the list and write to the file
            for (T item : data) {
                // Convert each item to its CSV format using the subclass-specific method
                String line = convertToCSV(item);
                writer.write(line);
                writer.newLine();  // New line after each item
            }

            System.out.println("Data has been stored to CSV.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Abstract method to be implemented by subclass for converting objects to CSV strings
    protected abstract String convertToCSV(T item);

    // Optional: Abstract method to provide a header row for the CSV file (if needed)
    protected abstract String getHeader();
}
