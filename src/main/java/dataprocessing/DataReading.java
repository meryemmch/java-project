package src.main.java.dataprocessing;

import java.util.List;


public interface DataReading<T> {
    List<T> readData(String filePath);
}
