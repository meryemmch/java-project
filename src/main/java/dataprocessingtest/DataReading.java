package src.main.java.dataprocessingtest;

import java.util.List;


public interface DataReading<T> {
    List<T> readData(String filePath);
}
