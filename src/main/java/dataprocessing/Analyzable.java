package src.main.java.dataprocessing;
import java.util.*;
public interface Analyzable<T> {
    void analyzeData(List<T> data);
}