package features;

import java.util.*;

public class SearchFrequency 
{
    private static TreeMap<String, Integer> searchFrequencyMap = new TreeMap<>();

    // In the search frequency count for the given car name
    public static void incrementSearchFrequency(String carName) {
        searchFrequencyMap.put(carName, searchFrequencyMap.getOrDefault(carName, 0) + 1);
    }

    // Update search frequency based on the carList from ByteBuds
    public static void updateSearchFrequency(Set<String> carList) {
        for (String car : carList) {
            incrementSearchFrequency(car);
        }
    }

    // Sort carList based on search frequency count
    public static List<String> sortCarListByFrequency(Set<String> carList) {
        List<String> sortedCarList = new ArrayList<>(searchFrequencyMap.keySet());

        // Sort the list based on search frequency count
        sortedCarList.sort(Comparator.comparingInt(searchFrequencyMap::get).reversed());

        return sortedCarList;
    }

    // Display the most searched cars
    public static List<String> displayMostSearchedCars(Set<String> carList) {
        List<String> sortedCarList = sortCarListByFrequency(searchFrequencyMap.keySet());


        return sortedCarList;
    }
}
