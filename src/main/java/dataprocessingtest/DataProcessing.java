package src.main.java.dataprocessingtest;

import java.util.List;



public interface DataProcessing<T> {

    void removeDuplicates(List<T> data);
       

    boolean isValid(T item);

   
    List<T> removeRowsWithMissingData(List<T> data);


    List<T> getRandomSample(List<T> data, int numRows) ;


    /*
     Method to handle outliers (you can make it more generic for different types of outliers)
    
     List<T> handleOutliers(List<T> data, Object... thresholds);
    
    Method to normalize the data (can be extended with different normalizations)
    
    void normalizeData(List<T> data);
     */


}
