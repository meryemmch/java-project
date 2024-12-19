package src.main.java.dataprocessingtest;

import java.util.List;

public interface DataStoring<T> {
    void storeData(List<T> data, String outputFilePath);  // Method signature that the implementing class must override
}
