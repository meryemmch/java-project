package src.main.java.dataprocessingtest;
import java.util.*;
public interface Analyzable<T> {
    void analyzeData(List<T> data);
}