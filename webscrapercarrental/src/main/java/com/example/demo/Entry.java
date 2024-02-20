package com.example.demo;

import com.fasterxml.jackson.databind.ObjectMapper;
import features.*;
import htmlparser.AvisBudgetParser;
import htmlparser.CarRentalParser;
import model.Car_Info;
import webcrawling.AvisCanadaCrawl;
import webcrawling.BudgetCanadaCrawl;
import webcrawling.CarRentalWebCrawl;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;


public class Entry {

   static  Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {


        

        while (true) 
        {
            System.out.println("Select an option:");
            System.out.println("1. Perform Crawling");
            System.out.println("2. Perform Parsing");
            System.out.println("3. Exit");

            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    performCrawling(); // to perfrom webcrawl from 3 websites
                    break;

                case 2:
                    if (checkHtmlFiles()) 
                    {
                        List<Car_Info> carInfoList = performParsing();
                        filter_Car_Deals(carInfoList);
                    } else {
                        System.out.println("No HTML files available for parsing.");
                    }
                    break;

                case 3:
                    System.out.println("Exiting program. Goodbye!");
                    System.exit(0);

                default:
                    System.out.println("Invalid choice. Please select again.");
            }
        }
    }

    private static boolean files_Exist_InAvis() {
        File avis_Folder = new File("AvisFiles");

        if (avis_Folder.exists() && avis_Folder.isDirectory()) {
            File[] filess = avis_Folder.listFiles();

            if (filess != null) {
                for (File filee : filess) {
                    if (filee.isFile() && filee.getName().endsWith(".html")) {
                        return true; // Found at least one HTML file
                    }
                }
            }
        }

        return false; // No HTML files found in the "AvisFiles" folder
    }

    private static boolean files_Exist_InBudget() {
        File budget_Folder = new File("BudgetFiles");

        if (budget_Folder.exists() && budget_Folder.isDirectory()) {
            File[] filess = budget_Folder.listFiles();

            if (filess != null) {
                for (File filee : filess) {
                    if (filee.isFile() && filee.getName().endsWith(".html")) {
                        return true; // Found at least one HTML file
                    }
                }
            }
        }

        return false; // No HTML files found in the "AvisFiles" folder
    }

    private static boolean files_Exist_InCarRental() {
        File carrentalFolder = new File("CarRentalFiles");

        if (carrentalFolder.exists() && carrentalFolder.isDirectory()) {
            File[] filess = carrentalFolder.listFiles();

            if (filess != null) {
                for (File filee : filess) {
                    if (filee.isFile() && filee.getName().endsWith(".html")) {
                        return true; // Found at least one HTML file
                    }
                }
            }
        }

        return false; // No HTML files found in the "AvisFiles" folder
    }

    private static boolean checkHtmlFiles() {
        // Check if (AvisFiles, BudgetFiles, CarRentalFiles) present or not
       
       
        if (files_Exist_InAvis() || files_Exist_InBudget() || files_Exist_InCarRental()) {
            return true;
        } else {
            return false;
        }
       
    }


    private static void filter_Car_Deals(List<Car_Info> car_Info_List) {
       

        System.out.println("**************************************************");
        System.out.println("*                  PARSING                      *");
        System.out.println("*            CAR DEALS FILTER MENU                *");
        System.out.println("**************************************************");

        String refine_Selection;
        do {
            System.out.println("Do you want to filter the car deals? (y/n): ");
            refine_Selection = scanner.next().toLowerCase();
        } 
        while (!DataValidation.validate_User_Response(refine_Selection));

        List<Car_Info> process_Filter = new ArrayList<>();
        while (refine_Selection.equals("y")) {
//            processFilter.addAll(carInfoList);
            process_Filter = car_Info_List;
            process_Filter.sort(Comparator.comparingDouble(Car_Info::getPrice));
            String option = "1";

            boolean validInput = false;

            while (!validInput) {
                try {
                    do {
                        System.out.println("\nSelect option to filter the deals:\n1. Display deals by price (LOW - HIGH)\n2. Sort by Car Name\n3. Car Price\n4. Sort by Transmission Type\n5. Sort by Passenger Capacity (HIGH - LOW)\n6. Sort by Luggage Capacity (HIGH - LOW)\n7. Show Car Count Analysis\n8. Exit");
                        option = scanner.next();
                    } while (!DataValidation.validate_Integer(Integer.parseInt(option)));
                    validInput = true;
                }catch (NumberFormatException ex){
                    System.out.println("Invalid input. Please enter a valid response.");
                }
            }
            switch (Integer.parseInt(option)) {
                case 1:
//                  it will print all cars available from 3 websites on that date  u given
                    display_Car_List(process_Filter);
                    break;
                case 2:
                	// get car types u want
                    System.out.println("The available Car Companies:");
                    Set<String> car_List = car_Info_List.stream()
                            .map(ele -> {
                                return ele.getName().split(" ")[0];
                              
                            })
                            .collect(Collectors.toSet());


                    System.out.println(car_List);

                    List<String> most_Searched_Cars = SearchFrequency.displayMostSearchedCars(car_List);

                    if (!most_Searched_Cars.isEmpty()){
                        System.out.println("Most Searched Cars:");
                        for (String car : most_Searched_Cars) {
                            System.out.println(car);
                        }
                    }

                    try {
                    	// for spell checking
                        SpellChecking.initializeDictionary("JsonData/filtered_car_deals.json");
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    boolean check;
                    String preferred_Car_Name;
                    do {
                        do {
                            System.out.println("Enter your preferred Car Company from above given list: ");
                            preferred_Car_Name = scanner.next().toLowerCase();
                            
                        } while (!DataValidation.validate_City_Name(preferred_Car_Name));

                        check = SpellChecking.checkSpelling(preferred_Car_Name);
                        if (!check) {
                            System.out.println("No such Car exists. Please try again any other from the given list...");
                        }
                    } while (!check);

                    SearchFrequency.incrementSearchFrequency(preferred_Car_Name); // increasing search frequncy by 1 for searched car

                    process_Filter = filterBy_CarName(car_Info_List, preferred_Car_Name);
                    display_Car_List(process_Filter); // prints list of cars

                    String s;
                    do {
                        System.out.println("Do you want to see Page Rank of websites for the given Car Model (Y/N): ");
                        s = scanner.next();
                    } while (!DataValidation.validate_User_Response(s));

                    if (s.equalsIgnoreCase("y")) {
                        try {
                            PageRanking.showRanking(preferred_Car_Name);
                            // in pageranking we use invertedindexing such that we can find words more quickly
//                            
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    }
                    break;
                case 3:
                    String preferred_Price_Range;

                    do {
                        System.out.println("Enter preferred price range type (50-200): ");
                        preferred_Price_Range = scanner.next().toLowerCase();
                    } while (!DataValidation.validate_Range_Input(preferred_Price_Range));

                    process_Filter = filterByPriceRange(car_Info_List, preferred_Price_Range);
                    display_Car_List(process_Filter);
                    break;
                case 4:

                    String preferredTransmission;
                    do {
                        System.out.println("Enter preferred transmission type (A or M): ");
                        preferredTransmission = scanner.next().toUpperCase();
                    } while (!DataValidation.validate_TTypes(preferredTransmission));

                    process_Filter = switch (preferredTransmission.toUpperCase()) {
                        case "A" -> filterBy_Transmission(car_Info_List, "Automatic");
                        case "M" -> filterBy_Transmission(car_Info_List, "Manual");
                        default -> process_Filter;
                    };
//                    processFilter = filterByTransmission(carInfoList, preferredTransmission);
                    display_Car_List(process_Filter);

                    break;
                case 5:
                    int preferredPassengerCapacity;
                    do {
                        System.out.println("Enter preferred passenger capacity: ");
                        preferredPassengerCapacity = scanner.nextInt();
                    } while (!DataValidation.validate_Integer(Integer.parseInt(option)));
                    process_Filter = filterBy_Passenger_Capacity(car_Info_List, preferredPassengerCapacity);
                    display_Car_List(process_Filter);

                    break;
                case 6:
                    int preferredLuggageCapacity;
                    do {
                        System.out.println("Enter preferred luggage capacity: ");
                        preferredLuggageCapacity = scanner.nextInt();
                    } while (!DataValidation.validate_Integer(Integer.parseInt(option)));
                    process_Filter = filterBy_Luggage_Capacity(car_Info_List, preferredLuggageCapacity);
                    display_Car_List(process_Filter);
                    break;
                case 7:
                    fetch_Car_Analysis(car_Info_List); // car count analysis

                    break;
                case 8:
                    refine_Selection = "no";
                    break;
                default:
                    System.out.println("Invalid option. Please enter a valid option.");
            }
        }
    }

    private static void fetch_Car_Analysis(List<Car_Info> car_Info_List) {
        Map<String, Integer> frequencyMap = FrequencyCount.getFrequencyCount("JsonData/filtered_car_deals.json");

        // Print the frequency count
        for (Map.Entry<String, Integer> entry : frequencyMap.entrySet()) {
            System.out.println("Total Available \"" + entry.getKey() + "\" cars: \"" + entry.getValue() + "\"");
        }
    }

    private static List<Car_Info> filterByPriceRange(List<Car_Info> car_Info_List, String preferred_Price_Range) {
        String[] priceRange = preferred_Price_Range.split("-");

        if (priceRange.length != 2) {
            // Handle invalid price range input
            throw new IllegalArgumentException("Invalid price range format");
        }

        int minPrice = Math.min(Integer.parseInt(priceRange[0].trim()),Integer.parseInt(priceRange[1].trim()));
        int maxPrice = Math.max(Integer.parseInt(priceRange[0].trim()),Integer.parseInt(priceRange[1].trim()));

        return car_Info_List.stream()
                .filter(car -> {
                    try {
                        double carPrice = car.getPrice();
                        return carPrice >= minPrice && carPrice <= maxPrice;
                    } catch (NumberFormatException e) {
                        // Handle invalid price format for a car
                        return false;
                    }
                })
                .sorted(Comparator.comparingDouble(Car_Info::getPrice))
                .collect(Collectors.toList());
    }

    private static int get_User_Selection(int maxOption) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Select an option (or Enter 0 to select all): ");
        while (!scanner.hasNextInt()) {
            System.out.println("\nInvalid input. Please enter a valid option.");
            scanner.next();
        }
        int selectedOption = scanner.nextInt();
        return selectedOption;
    }

    private static List<Car_Info> filterBy_CarName(List<Car_Info> car_Info_List, String preferred_CarName) {
        try {
            SpellChecking.initializeDictionary("JsonData/filtered_car_deals.json");
            WordCompletion.initializeDictionaryFromJsonFile("JsonData/filtered_car_deals.json");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        List<String> suggestions = WordCompletion.getSuggestions(preferred_CarName.toLowerCase());

        if (!suggestions.isEmpty()) {
            System.out.println("Suggestions:");

            for (int i = 0; i < suggestions.size(); i++) {

                System.out.println((i + 1) + ". " + suggestions.get(i));
            }

            // Assuming you have a method to get user input, e.g., getUserSelection
            int selectedOption = get_User_Selection(suggestions.size());


            // Check if the selected option is valid
            if (selectedOption >= 1 && selectedOption <= suggestions.size()) {
                preferred_CarName = suggestions.get(selectedOption - 1);
            } else if (selectedOption == 0) {
//                System.out.println(preferredCarName);
                // User selected all options, so no need to change preferredCarName
            } else {
                System.out.println("Invalid selection. Using original input.");
            }
        }

        try {
//          PageRanking2.showRanking(preferredCarName);
   //     	PageRanking.pageRank(preferredCarName);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
//        if (!check) {
//        }

        String finalPreferredCarName = preferred_CarName;
        return car_Info_List.stream()
                .filter(car -> car.getName().equalsIgnoreCase(finalPreferredCarName) || car.getName().toLowerCase().contains(finalPreferredCarName))
                .sorted(Comparator.comparingDouble(Car_Info::getPrice))
                .toList();
    }

    private static List<Car_Info> filterBy_Transmission(List<Car_Info> car_Info_List, String preferred_Transmission) {
        return car_Info_List.stream()
                .filter(car -> car.getTransmissionType().equalsIgnoreCase(preferred_Transmission))
                .sorted(Comparator.comparingDouble(Car_Info::getPrice))
                .toList();
    }

    private static List<Car_Info> filterBy_Passenger_Capacity(List<Car_Info> car_Info_List, int preferred_Passenger_Capacity)
    {

        Optional<Car_Info> maxPassengerCapacity = car_Info_List.stream()
                .max(Comparator.comparingInt(Car_Info::getPassengerCapacity));

        if (preferred_Passenger_Capacity > maxPassengerCapacity.get().getPassengerCapacity()) {
            preferred_Passenger_Capacity = maxPassengerCapacity.get().getPassengerCapacity();
        }
        int finalPreferredPassengerCapacity = preferred_Passenger_Capacity;

        return car_Info_List.stream()
                .filter(car -> car.getPassengerCapacity() >= finalPreferredPassengerCapacity)
                .sorted(Comparator.comparingDouble(Car_Info::getPrice))
                .toList();
    }

    private static List<Car_Info> filterBy_Luggage_Capacity(List<Car_Info> car_Info_List, int preferred_Luggage_Capacity) 
    {


        Optional<Car_Info> max_Total_Car = car_Info_List.stream()
                .max(Comparator.comparingInt(car -> car.getLargeBag() + car.getSmallBag()));

        if (preferred_Luggage_Capacity > max_Total_Car.get().getLargeBag() + max_Total_Car.get().getSmallBag()) {
            preferred_Luggage_Capacity = max_Total_Car.get().getLargeBag() + max_Total_Car.get().getSmallBag();
        }
        int finalPreferredLuggageCapacity = preferred_Luggage_Capacity;
        return car_Info_List.stream()
                .filter(car -> car.getLargeBag() + car.getSmallBag() >= finalPreferredLuggageCapacity)
                .sorted(Comparator.comparingDouble(Car_Info::getPrice))
                .toList();
    }

    private static void display_Car_List(List<Car_Info> car_Info_List) {
        // Display table header with borders
        System.out.println("+-------------------------+----------------------------------------+-------------------+------------------------+------------------------+--------------------------+--------------------+");
        System.out.println("|      Car Group          |          Car Model                     |    Rent Price     |   Passenger Capacity   |    Luggage Capacity    |     Transmission Type    |    Rental Company  |");
        System.out.println("+-------------------------+----------------------------------------+-------------------+------------------------+------------------------+--------------------------+--------------------+");

        // Display table rows with borders
        for (Car_Info car_Info : car_Info_List) {
            System.out.printf("| %-23s | %-38s | $%-16.2f | %-22s | %-22s | %-24s | %-18s |\n",
                    car_Info.getCarGroup(), car_Info.getName(), car_Info.getPrice(),
                    car_Info.getPassengerCapacity(), car_Info.getLargeBag() + car_Info.getSmallBag(),
                    car_Info.getTransmissionType(), car_Info.getRentalCompany());
        }


        // Display table footer with borders
        System.out.println("+-------------------------+----------------------------------------+-------------------+------------------------+------------------------+--------------------------+--------------------+");
    }

    private static void saveCarInfoToJson(List<Car_Info> car_Info_List, String file_name) {
        ObjectMapper objectMapper = new ObjectMapper();  //objectmapper constructor to save data in json format
        String directoryPath = "JsonData/";

        try {
            File directory = new File(directoryPath);

            // Create the directory if it doesn't exist
            if (!directory.exists()) {
                directory.mkdirs();
            }

            // Create the file in the specified directory with the provided filename
            File file = new File(directory, file_name + ".json");

//            System.out.println(carInfoList);
            // Write carInfoList to JSON file
            try {
                objectMapper.writeValue(file, car_Info_List);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
//            System.out.println("Filtered car deals saved to: " + file.getAbsolutePath());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static List<Car_Info> performParsing() {
        List<Car_Info> list = new ArrayList<>();
        list.addAll(AvisBudgetParser.parseFiles());
        list.addAll(CarRentalParser.fetchAllCarRentalDeals());
        
        saveCarInfoToJson(list, "filtered_car_deals");

        return list;
    }

    private static void performCrawling() {
        AvisCanadaCrawl.initDriver();
        BudgetCanadaCrawl.initDriver();
        CarRentalWebCrawl.initDriver();

        Scanner scanner = new Scanner(System.in);
        String response;
        do {
            // Ask if pickup and drop-off locations are the same
            String sameLocationResponse;
            do {
                System.out.print("Are pickup and drop-off locations the same? (y/n): ");
                sameLocationResponse = scanner.nextLine().toLowerCase();
            } while (!DataValidation.validate_User_Response(sameLocationResponse));

            // Get pickup location
            String pickupLocation;
            do {
                System.out.print("Enter pickup location: ");
                pickupLocation = scanner.nextLine();
            } while (!DataValidation.validate_City_Name(pickupLocation));

            String finalSelectedPickupLoc = AvisCanadaCrawl.resolveLocation(pickupLocation, "PicLoc_value", "PicLoc_dropdown");
            BudgetCanadaCrawl.resolveLocation(finalSelectedPickupLoc.split(",")[0], "PicLoc_value", "PicLoc_dropdown");
            CarRentalWebCrawl.handlePickUpLocation(finalSelectedPickupLoc);
            CarRentalWebCrawl.handlePickUpLocation(finalSelectedPickupLoc.split(",")[0]);
            BudgetCanadaCrawl.resolveLocation(pickupLocation,"PicLoc_value", "PicLoc_dropdown");

//             Get drop-off location if locations are different
            String dropOffLocation;
            do {
                dropOffLocation = sameLocationResponse.equals("n") ? getDropOffLocation(scanner) : pickupLocation;
            } while (!DataValidation.validate_City_Name(dropOffLocation));

            if (sameLocationResponse.equals("n")){
                String finalSelectedDropOffLoc = AvisCanadaCrawl.resolveLocation(dropOffLocation, "DropLoc_value", "DropLoc_dropdown");
                BudgetCanadaCrawl.resolveLocation(finalSelectedDropOffLoc, "DropLoc_value", "DropLoc_dropdown");
                CarRentalWebCrawl.handleDropOffLocation(finalSelectedDropOffLoc);
            }


            // Get pickup date
            String pickupDate;
            do {
                System.out.print("Enter pickup date (DD/MM/YYYY): ");
                pickupDate = scanner.nextLine();
            } while (!DataValidation.validate_Date(pickupDate));

            // Get drop-off date
            String returnDate;
            do {
                System.out.print("Enter return date (DD/MM/YYYY): ");
                returnDate = scanner.nextLine();
            } while (!DataValidation.validate_Date(returnDate));

            CarRentalWebCrawl.resolveDate(pickupDate, returnDate);

            pickupDate = convertDateFormat(pickupDate);
            returnDate = convertDateFormat(returnDate);

            AvisCanadaCrawl.resolveDate(pickupDate, returnDate);
            BudgetCanadaCrawl.resolveDate(pickupDate, returnDate);

//            System.out.println("Do you have a specific time in mind to pick and return the car: ");

            String pickupTime;
            do {
                System.out.print("Enter pickup time (HH:MM AM/PM): ");
                pickupTime = scanner.nextLine();
            } while (!DataValidation.validate_Time(pickupTime));

            // Get drop-off date
            String returnTime;
            do {
                System.out.print("Enter return time (HH:MM AM/PM): ");
                returnTime = scanner.nextLine();
            } while (!DataValidation.validate_Time(returnTime));
             
            
            try {
                AvisCanadaCrawl.resolveTime(pickupTime, returnTime);
                BudgetCanadaCrawl.resolveTime(pickupTime, returnTime);
                CarRentalWebCrawl.resolveTime(pickupTime, returnTime);
                AvisCanadaCrawl.fetchCarDeals();
               
                BudgetCanadaCrawl.fetchCarDeals();
            }catch (Exception ex){

            }

             

            // Ask if the user wants to continue
            System.out.print("Do you want to continue? (yes/no): ");
            response = scanner.nextLine();
            if (response.equalsIgnoreCase("yes")) {
                AvisCanadaCrawl.resetDriver();
                BudgetCanadaCrawl.resetDriver();
                CarRentalWebCrawl.resetDriver();
            }
        } while (response.equalsIgnoreCase("yes"));

        AvisCanadaCrawl.closeDriver();
        BudgetCanadaCrawl.closeDriver();
        CarRentalWebCrawl.closeDriver();
    }

    public static String convertDateFormat(String inputDate) {
        SimpleDateFormat originalFormat = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat targetFormat = new SimpleDateFormat("MM/dd/yyyy");

        try {
            Date date = originalFormat.parse(inputDate);
            return targetFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace(); // Handle the ParseException as needed
            return null; // Return null or throw an exception based on your error handling strategy
        }
    }

    private static String getDropOffLocation(Scanner scanner) {
        System.out.print("Enter drop-off location: ");
        return scanner.nextLine();
    }
}